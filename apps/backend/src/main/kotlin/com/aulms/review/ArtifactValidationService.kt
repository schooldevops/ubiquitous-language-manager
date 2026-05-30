package com.aulms.review

import com.aulms.model.ArtifactSourceType
import com.aulms.model.ArtifactValidationRequest
import com.aulms.model.ArtifactValidationResult
import com.aulms.model.ArtifactValidationSummary
import com.aulms.model.ExpressionType
import com.aulms.model.PullRequestArtifactReviewRequest
import com.aulms.model.PullRequestArtifactReviewResult
import com.aulms.model.ValidationIssue
import com.aulms.model.ValidationSeverity
import com.aulms.rule.PhysicalSpecValidationRequest
import com.aulms.rule.RuleEngine
import com.aulms.rule.RuleValidationRequest
import com.aulms.search.SearchNormalizer
import com.aulms.term.TermRepository
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml

@Service
class ArtifactValidationService(
    private val ruleEngine: RuleEngine,
    private val termRepository: TermRepository,
) {
    fun validateOpenApi(request: ArtifactValidationRequest): ArtifactValidationResult =
        validate(request.copy(sourceType = ArtifactSourceType.OPENAPI))

    fun validateDdl(request: ArtifactValidationRequest): ArtifactValidationResult =
        validate(request.copy(sourceType = ArtifactSourceType.DDL))

    fun validateCode(request: ArtifactValidationRequest): ArtifactValidationResult {
        val sourceType = if (request.sourceType in codeSourceTypes) {
            request.sourceType
        } else {
            resolveSourceType(request)
        }
        return validate(request.copy(sourceType = sourceType))
    }

    fun validatePullRequest(request: PullRequestArtifactReviewRequest): PullRequestArtifactReviewResult {
        val results = request.files.map { file ->
            validate(
                ArtifactValidationRequest(
                    sourceType = file.sourceType ?: ArtifactSourceType.AUTO,
                    filePath = file.filePath,
                    content = file.content,
                    failOnWarning = request.failOnWarning,
                    includeSuggestions = request.includeSuggestions,
                ),
            )
        }
        val issues = results.flatMap { it.issues }
        val summary = ArtifactValidationSummary(
            errorCount = issues.count { it.severity == ValidationSeverity.ERROR },
            warningCount = issues.count { it.severity == ValidationSeverity.WARNING },
            infoCount = issues.count { it.severity == ValidationSeverity.INFO },
        )
        val exitCode = if (summary.errorCount > 0 || (request.failOnWarning == true && summary.warningCount > 0)) 1 else 0

        return PullRequestArtifactReviewResult(
            pullRequestId = request.pullRequestId,
            repository = request.repository,
            results = results,
            summary = summary,
            issues = issues,
            exitCode = exitCode,
        )
    }

    fun validate(request: ArtifactValidationRequest): ArtifactValidationResult {
        val sourceType = resolveSourceType(request)
        val expressions = when (sourceType) {
            ArtifactSourceType.OPENAPI -> OpenApiArtifactParser.parse(request.filePath, request.content)
            ArtifactSourceType.DDL -> DdlArtifactParser.parse(request.filePath, request.content)
            ArtifactSourceType.KOTLIN -> CodeArtifactParser.parseKotlin(request.filePath, request.content)
            ArtifactSourceType.JAVA -> CodeArtifactParser.parseJava(request.filePath, request.content)
            ArtifactSourceType.TYPESCRIPT -> CodeArtifactParser.parseTypeScript(request.filePath, request.content)
            ArtifactSourceType.SQL -> SqlArtifactParser.parse(request.filePath, request.content)
            ArtifactSourceType.TEST -> TestArtifactParser.parse(request.filePath, request.content)
            ArtifactSourceType.AUTO -> emptyList()
        }
        val issues = expressions
            .flatMap { expression -> validateExpression(sourceType, expression) }
            .distinctBy { "${it.severity}:${it.source}:${it.location}:${it.inputExpression}:${it.recommendedExpression}" }

        val summary = ArtifactValidationSummary(
            errorCount = issues.count { it.severity == ValidationSeverity.ERROR },
            warningCount = issues.count { it.severity == ValidationSeverity.WARNING },
            infoCount = issues.count { it.severity == ValidationSeverity.INFO },
        )
        val exitCode = if (summary.errorCount > 0 || (request.failOnWarning == true && summary.warningCount > 0)) 1 else 0

        return ArtifactValidationResult(
            filePath = request.filePath,
            sourceType = sourceType,
            checkedCount = expressions.size,
            summary = summary,
            issues = if (request.includeSuggestions == false) issues.map { it.copy(recommendedExpression = null) } else issues,
            exitCode = exitCode,
        )
    }

    private fun resolveSourceType(request: ArtifactValidationRequest): ArtifactSourceType {
        if (request.sourceType != ArtifactSourceType.AUTO) return request.sourceType
        val path = request.filePath.lowercase()
        return when {
            path.endsWith(".yaml") || path.endsWith(".yml") -> ArtifactSourceType.OPENAPI
            path.endsWith(".ddl") -> ArtifactSourceType.DDL
            path.endsWith(".sql") -> ArtifactSourceType.SQL
            path.endsWith(".kt") -> ArtifactSourceType.KOTLIN
            path.endsWith(".java") -> ArtifactSourceType.JAVA
            path.endsWith(".ts") || path.endsWith(".tsx") -> ArtifactSourceType.TYPESCRIPT
            path.endsWith(".feature") -> ArtifactSourceType.TEST
            else -> ArtifactSourceType.OPENAPI
        }
    }

    private companion object {
        val codeSourceTypes = setOf(
            ArtifactSourceType.KOTLIN,
            ArtifactSourceType.JAVA,
            ArtifactSourceType.TYPESCRIPT,
            ArtifactSourceType.SQL,
            ArtifactSourceType.TEST,
        )
    }

    private fun validateExpression(sourceType: ArtifactSourceType, expression: ExtractedArtifactExpression): List<ValidationIssue> {
        val expressionIssues = ruleEngine.validateExpression(
            RuleValidationRequest(
                source = sourceType.value,
                inputExpression = expression.value,
                expressionType = expression.expressionType,
                location = expression.location,
                developmentUsage = true,
            ),
        )
        if (expression.expressionType != ExpressionType.DB_COLUMN || expression.physicalType == null) {
            return expressionIssues
        }

        val termId = findDbColumnTermId(expression.value) ?: return expressionIssues
        val physicalIssues = ruleEngine.validatePhysicalSpec(
            PhysicalSpecValidationRequest(
                source = sourceType.value,
                termId = termId,
                physicalType = expression.physicalType,
                digits = expression.digits ?: 0,
                decimalPoint = expression.decimalPoint ?: 0,
                location = expression.location,
            ),
        )
        return expressionIssues + physicalIssues
    }

    private fun findDbColumnTermId(columnName: String): String? {
        val normalized = SearchNormalizer.normalize(columnName)
        return termRepository.searchDocuments().firstOrNull { document ->
            SearchNormalizer.normalize(document.term.englishAbbreviation) == normalized ||
                document.expressions.any {
                    it.expressionType == ExpressionType.DB_COLUMN &&
                        SearchNormalizer.normalize(it.expressionValue) == normalized
                }
        }?.term?.termId
    }
}

private data class ExtractedArtifactExpression(
    val value: String,
    val expressionType: ExpressionType,
    val location: String,
    val physicalType: String? = null,
    val digits: Int? = null,
    val decimalPoint: Int? = null,
)

private object TextLocation {
    fun lineOffsets(content: String): List<Int> =
        listOf(0) + content.withIndex().filter { it.value == '\n' }.map { it.index + 1 }

    fun lineNumber(content: String, offsets: List<Int>, index: Int): Int {
        val bounded = index.coerceIn(0, content.length)
        return offsets.indexOfLast { it <= bounded } + 1
    }
}

private object OpenApiArtifactParser {
    fun parse(filePath: String, content: String): List<ExtractedArtifactExpression> {
        val root = runCatching { Yaml().load<Any>(content) }.getOrNull() ?: return emptyList()
        val expressions = mutableListOf<ExtractedArtifactExpression>()
        collectProperties(root, "#", filePath, expressions)
        return expressions.distinctBy { it.location }
    }

    @Suppress("UNCHECKED_CAST")
    private fun collectProperties(
        node: Any?,
        pointer: String,
        filePath: String,
        expressions: MutableList<ExtractedArtifactExpression>,
    ) {
        when (node) {
            is Map<*, *> -> {
                val properties = node["properties"]
                if (properties is Map<*, *>) {
                    properties.keys.filterIsInstance<String>().forEach { propertyName ->
                        expressions.add(
                            ExtractedArtifactExpression(
                                value = propertyName,
                                expressionType = ExpressionType.API_FIELD,
                                location = "$filePath$pointer/properties/$propertyName",
                            ),
                        )
                    }
                }
                node.forEach { (key, value) ->
                    if (key is String) {
                        collectProperties(value, "$pointer/$key", filePath, expressions)
                    }
                }
            }
            is List<*> -> node.forEachIndexed { index, value ->
                collectProperties(value, "$pointer/$index", filePath, expressions)
            }
        }
    }
}

private object DdlArtifactParser {
    private val createTableRegex = Regex("""(?is)create\s+table\s+(?:if\s+not\s+exists\s+)?([A-Z0-9_."`]+)\s*\((.*?)\)\s*;""")
    private val columnRegex = Regex("""^\s*["`]?([A-Z][A-Z0-9_]*)["`]?\s+([A-Z][A-Z0-9_]*)(?:\s*\(\s*(\d+)(?:\s*,\s*(\d+))?\s*\))?.*$""", RegexOption.IGNORE_CASE)
    private val skippedPrefixes = setOf("CONSTRAINT", "PRIMARY", "FOREIGN", "UNIQUE", "CHECK", "KEY")

    fun parse(filePath: String, content: String): List<ExtractedArtifactExpression> {
        val lineOffsets = TextLocation.lineOffsets(content)
        return createTableRegex.findAll(content).flatMap { tableMatch ->
            splitColumnDefinitions(tableMatch.groupValues[2]).mapNotNull { definition ->
                val trimmed = definition.trim()
                val firstToken = trimmed.substringBefore(' ').uppercase()
                if (firstToken in skippedPrefixes) return@mapNotNull null
                val match = columnRegex.matchEntire(trimmed) ?: return@mapNotNull null
                val columnName = match.groupValues[1]
                val physicalType = normalizePhysicalType(match.groupValues[2])
                val digits = match.groupValues.getOrNull(3)?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: defaultDigits(physicalType)
                val decimalPoint = match.groupValues.getOrNull(4)?.takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0
                ExtractedArtifactExpression(
                    value = columnName,
                    expressionType = ExpressionType.DB_COLUMN,
                    location = "$filePath:${TextLocation.lineNumber(content, lineOffsets, tableMatch.range.first + tableMatch.value.indexOf(definition))}",
                    physicalType = physicalType,
                    digits = digits,
                    decimalPoint = decimalPoint,
                )
            }
        }.toList()
    }

    private fun splitColumnDefinitions(body: String): List<String> {
        val parts = mutableListOf<String>()
        val current = StringBuilder()
        var depth = 0
        body.forEach { char ->
            when (char) {
                '(' -> {
                    depth += 1
                    current.append(char)
                }
                ')' -> {
                    depth = (depth - 1).coerceAtLeast(0)
                    current.append(char)
                }
                ',' -> {
                    if (depth == 0) {
                        parts.add(current.toString())
                        current.clear()
                    } else {
                        current.append(char)
                    }
                }
                else -> current.append(char)
            }
        }
        if (current.isNotBlank()) parts.add(current.toString())
        return parts
    }

    private fun normalizePhysicalType(raw: String): String = when (raw.uppercase()) {
        "VARCHAR2" -> "VARCHAR"
        "DECIMAL" -> "NUMERIC"
        else -> raw.uppercase()
    }

    private fun defaultDigits(physicalType: String): Int = when (physicalType) {
        "DATE" -> 8
        "TIMESTAMP" -> 14
        else -> 0
    }

}

private object CodeArtifactParser {
    private val kotlinPropertyRegex = Regex("""\b(?:val|var)\s+([a-zA-Z_][a-zA-Z0-9_]*)\s*:""")
    private val javaFieldRegex = Regex("""(?m)^\s*(?:private|protected|public)?\s*(?:final\s+)?[A-Z_a-z][A-Z_a-z0-9<>, ?]*\s+([a-zA-Z_][a-zA-Z0-9_]*)\s*(?:=|;)""")
    private val tsPropertyRegex = Regex("""(?m)^\s*([a-zA-Z_][a-zA-Z0-9_]*)\??\s*:""")
    private val columnAnnotationRegex = Regex("@Column\\s*\\(\\s*name\\s*=\\s*\"([^\"]+)\"")
    private val jsonPropertyRegex = Regex("@JsonProperty\\s*\\(\\s*\"([^\"]+)\"")

    fun parseKotlin(filePath: String, content: String): List<ExtractedArtifactExpression> =
        parseAnnotations(filePath, content) +
            parseMatches(filePath, content, kotlinPropertyRegex, ExpressionType.CODE_VARIABLE)

    fun parseJava(filePath: String, content: String): List<ExtractedArtifactExpression> =
        parseAnnotations(filePath, content) +
            parseMatches(filePath, content, javaFieldRegex, ExpressionType.CODE_VARIABLE)

    fun parseTypeScript(filePath: String, content: String): List<ExtractedArtifactExpression> =
        parseMatches(filePath, content, tsPropertyRegex, ExpressionType.CODE_VARIABLE)

    private fun parseAnnotations(filePath: String, content: String): List<ExtractedArtifactExpression> {
        val dbColumns = parseMatches(filePath, content, columnAnnotationRegex, ExpressionType.DB_COLUMN)
        val apiFields = parseMatches(filePath, content, jsonPropertyRegex, ExpressionType.API_FIELD)
        return dbColumns + apiFields
    }

    private fun parseMatches(
        filePath: String,
        content: String,
        regex: Regex,
        expressionType: ExpressionType,
    ): List<ExtractedArtifactExpression> {
        val offsets = TextLocation.lineOffsets(content)
        return regex.findAll(content).map { match ->
            ExtractedArtifactExpression(
                value = match.groupValues[1],
                expressionType = expressionType,
                location = "$filePath:${TextLocation.lineNumber(content, offsets, match.range.first)}",
            )
        }.toList()
    }
}

private object SqlArtifactParser {
    private val dbColumnRegex = Regex("""\b([A-Z][A-Z0-9_]*_(?:NO|ID|CD|DT|DTTM|AMT|NM|STS_CD|LIST))\b""")
    private val parameterRegex = Regex("""[#:$]\{?([a-z][a-zA-Z0-9_]*)}?""")

    fun parse(filePath: String, content: String): List<ExtractedArtifactExpression> {
        val offsets = TextLocation.lineOffsets(content)
        val columns = dbColumnRegex.findAll(content).map { match ->
            ExtractedArtifactExpression(
                value = match.groupValues[1],
                expressionType = ExpressionType.DB_COLUMN,
                location = "$filePath:${TextLocation.lineNumber(content, offsets, match.range.first)}",
            )
        }
        val parameters = parameterRegex.findAll(content).map { match ->
            ExtractedArtifactExpression(
                value = match.groupValues[1],
                expressionType = ExpressionType.CODE_VARIABLE,
                location = "$filePath:${TextLocation.lineNumber(content, offsets, match.range.first)}",
            )
        }
        return (columns.toList() + parameters.toList()).distinctBy { "${it.expressionType}:${it.value}:${it.location}" }
    }
}

private object TestArtifactParser {
    private val koreanTermRegex = Regex("""[가-힣]+(?:\s*ID|번호|명|상태코드|일자|일시|금액|목록)""")
    private val codeVariableRegex = Regex("""\b(customerId|customerNumber|orderNumber|orderDate|orderAmount|orderStatusCode)\b""")

    fun parse(filePath: String, content: String): List<ExtractedArtifactExpression> {
        val offsets = TextLocation.lineOffsets(content)
        val testWords = koreanTermRegex.findAll(content).map { match ->
            ExtractedArtifactExpression(
                value = match.value,
                expressionType = ExpressionType.TEST_WORD,
                location = "$filePath:${TextLocation.lineNumber(content, offsets, match.range.first)}",
            )
        }
        val codeVariables = codeVariableRegex.findAll(content).map { match ->
            ExtractedArtifactExpression(
                value = match.value,
                expressionType = ExpressionType.CODE_VARIABLE,
                location = "$filePath:${TextLocation.lineNumber(content, offsets, match.range.first)}",
            )
        }
        return (testWords.toList() + codeVariables.toList()).distinctBy { "${it.expressionType}:${it.value}:${it.location}" }
    }
}
