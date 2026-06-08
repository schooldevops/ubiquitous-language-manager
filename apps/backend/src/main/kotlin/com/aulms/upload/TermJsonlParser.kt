package com.aulms.upload

import com.aulms.model.AliasType
import com.aulms.model.ExpressionType
import com.aulms.model.RelationshipType
import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
import com.aulms.model.TermStatus
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class TermJsonlParser(private val objectMapper: ObjectMapper = ObjectMapper()) {

    fun parse(content: String): List<ParsedTermRow> {
        val result = mutableListOf<ParsedTermRow>()
        var lineNo = 0
        content.lineSequence().forEach { raw ->
            lineNo += 1
            val line = raw.trim()
            if (line.isEmpty()) return@forEach
            result.add(parseLine(lineNo, line))
        }
        return result
    }

    private fun parseLine(lineNo: Int, line: String): ParsedTermRow = try {
        val node = objectMapper.readTree(line)
        val termId = node.get("termId")?.takeIf { it.isTextual }?.asText()
            ?: throw IllegalArgumentException("termId 누락")
        ParsedTermRow(lineNo, line, toParsedTerm(termId, node), null)
    } catch (ex: Exception) {
        ParsedTermRow(lineNo, line, null, ex.message ?: "파싱 실패")
    }

    private fun toParsedTerm(termId: String, node: JsonNode): ParsedTerm = ParsedTerm(
        termId = termId,
        termNumber = node.textOrNull("termNumber"),
        domainName = node.textOrNull("domainName"),
        usageType = node.textOrNull("usageType"),
        koreanName = node.textOrNull("koreanName"),
        englishName = node.textOrNull("englishName"),
        englishAbbreviation = node.textOrNull("englishAbbreviation"),
        businessDefinition = node.textOrNull("businessDefinition"),
        usageContext = node.textOrNull("usageContext"),
        physicalType = node.textOrNull("physicalType"),
        digits = node.get("digits")?.takeIf { it.isInt }?.asInt(),
        decimalPoint = node.get("decimalPoint")?.takeIf { it.isInt }?.asInt(),
        status = node.textOrNull("status")?.let { TermStatus.forValue(it) },
        owner = node.textOrNull("owner"),
        version = node.textOrNull("version"),
        expressions = parseExpressions(termId, node.path("expressions")),
        aliases = parseAliases(termId, node.path("aliases")),
        relationships = parseRelationships(node.path("relationships")),
    )

    private fun parseExpressions(termId: String, node: JsonNode): List<TermExpression> =
        node.takeIf { it.isArray }?.map {
            TermExpression(
                expressionId = it.path("expressionId").asLong(),
                termId = termId,
                expressionType = ExpressionType.forValue(it.path("expressionType").asText()),
                expressionValue = it.path("expressionValue").asText(),
                isStandard = it.path("isStandard").asBoolean(true),
                language = it.textOrNull("language"),
                style = it.textOrNull("style"),
            )
        }.orEmpty()

    private fun parseAliases(termId: String, node: JsonNode): List<TermAlias> {
        if (node.isMissingNode || node.isNull) return emptyList()
        val groups = listOf("synonyms", "forbidden", "deprecated", "needsContext")
        return groups.flatMap { group ->
            node.path(group).takeIf { it.isArray }?.map { aliasNode(termId, it) }.orEmpty()
        }
    }

    private fun aliasNode(termId: String, node: JsonNode): TermAlias = TermAlias(
        aliasId = node.path("aliasId").asText(),
        termId = termId,
        aliasName = node.path("aliasName").asText(),
        aliasType = AliasType.forValue(node.path("aliasType").asText()),
        recommendationAction = node.path("recommendationAction").asText(""),
        reason = node.path("reason").asText(""),
    )

    private fun parseRelationships(node: JsonNode): List<ParsedRelationship> =
        node.takeIf { it.isArray }?.map {
            ParsedRelationship(
                relationshipId = it.path("relationshipId").asLong(),
                relationshipType = RelationshipType.forValue(it.path("relationshipType").asText()),
                targetTermId = it.path("targetTermId").asText(),
                description = it.path("description").asText(""),
            )
        }.orEmpty()

    private fun JsonNode.textOrNull(field: String): String? =
        get(field)?.takeIf { it.isTextual }?.asText()
}
