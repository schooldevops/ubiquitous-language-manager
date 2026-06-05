package com.aulms.term

import com.aulms.model.AliasType
import com.aulms.model.Term
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream

/**
 * 표준 용어 전체 내보내기.
 * - JSONL: 업로드 화면과 동일한 구조로 직렬화하여 그대로 재업로드 가능.
 * - XLSX: 한 행에 한 용어를 담은 엑셀 시트.
 *
 * OpenAPI 생성 인터페이스와 분리된 단독 컨트롤러로, 명세 재생성 없이 다운로드 전용 경로만 노출한다.
 */
@RestController
@RequestMapping("/api/terms/export")
class TermExportController(
    private val service: TermService,
    private val objectMapper: ObjectMapper,
) {

    @GetMapping("/jsonl", produces = ["application/x-ndjson"])
    fun exportJsonl(): ResponseEntity<ByteArray> {
        val body = allTerms().joinToString("\n") { objectMapper.writeValueAsString(toUploadShape(it)) }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"terms.jsonl\"")
            .contentType(MediaType.parseMediaType("application/x-ndjson"))
            .body(body.toByteArray(Charsets.UTF_8))
    }

    @GetMapping("/xlsx")
    fun exportXlsx(): ResponseEntity<ByteArray> {
        val bytes = buildWorkbook(allTerms())
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"terms.xlsx\"")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(bytes)
    }

    /** 모든 용어를 상세(표현/별칭 포함)까지 채워 반환. 페이지를 끝까지 순회한다. */
    private fun allTerms(): List<Term> {
        val pageSize = 200
        val result = mutableListOf<Term>()
        var page = 0
        while (true) {
            val items = service.listTerms(null, null, null, page, pageSize).items
            if (items.isEmpty()) break
            items.forEach { result.add(service.getTerm(it.termId)) }
            if (items.size < pageSize) break
            page += 1
        }
        return result
    }

    /** 업로드 JSONL 파서가 읽는 구조(별칭은 타입별 그룹)로 변환. */
    private fun toUploadShape(term: Term): Map<String, Any?> = linkedMapOf(
        "termId" to term.termId,
        "termNumber" to term.termNumber,
        "domainName" to term.domainName,
        "usageType" to term.usageType,
        "koreanName" to term.koreanName,
        "englishName" to term.englishName,
        "englishAbbreviation" to term.englishAbbreviation,
        "businessDefinition" to term.businessDefinition,
        "usageContext" to term.usageContext,
        "physicalType" to term.physicalType,
        "digits" to term.digits,
        "decimalPoint" to term.decimalPoint,
        "status" to term.status,
        "owner" to term.owner,
        "version" to term.version,
        "expressions" to term.expressions.map {
            linkedMapOf(
                "expressionId" to it.expressionId,
                "expressionType" to it.expressionType,
                "expressionValue" to it.expressionValue,
                "isStandard" to it.isStandard,
                "language" to it.language,
                "style" to it.style,
            )
        },
        "aliases" to linkedMapOf(
            "synonyms" to aliasGroup(term, AliasType.Synonym),
            "forbidden" to aliasGroup(term, AliasType.Forbidden),
            "deprecated" to aliasGroup(term, AliasType.Deprecated),
            "needsContext" to aliasGroup(term, AliasType.NeedsContext),
        ),
    )

    private fun aliasGroup(term: Term, type: AliasType): List<Map<String, Any?>> =
        term.aliases.filter { it.aliasType == type }.map {
            linkedMapOf(
                "aliasId" to it.aliasId,
                "aliasName" to it.aliasName,
                "aliasType" to it.aliasType,
                "recommendationAction" to it.recommendationAction,
                "reason" to it.reason,
            )
        }

    private fun buildWorkbook(terms: List<Term>): ByteArray {
        val headers = listOf(
            "termId", "termNumber", "도메인", "사용구분", "한글명", "영문명", "영문약어",
            "업무정의", "사용맥락", "물리타입", "자릿수", "소수점", "상태", "소유자", "버전",
            "DB컬럼", "API필드", "코드변수", "화면표시명", "유사어", "금지어",
        )
        XSSFWorkbook().use { wb: Workbook ->
            val sheet = wb.createSheet("표준용어")
            val header = sheet.createRow(0)
            val bold = wb.createCellStyle().apply {
                setFont(wb.createFont().apply { bold = true })
            }
            headers.forEachIndexed { i, title ->
                header.createCell(i).apply {
                    setCellValue(title)
                    cellStyle = bold
                }
            }
            terms.forEachIndexed { rowIdx, term ->
                val row = sheet.createRow(rowIdx + 1)
                val cells = listOf(
                    term.termId, term.termNumber, term.domainName, term.usageType,
                    term.koreanName, term.englishName, term.englishAbbreviation,
                    term.businessDefinition, term.usageContext ?: "", term.physicalType,
                    term.digits.toString(), term.decimalPoint.toString(), term.status.value,
                    term.owner, term.version,
                    expr(term, "DB_COLUMN"), expr(term, "API_FIELD"),
                    expr(term, "CODE_VARIABLE"), expr(term, "UI_LABEL"),
                    aliasNames(term, AliasType.Synonym), aliasNames(term, AliasType.Forbidden),
                )
                cells.forEachIndexed { i, value -> row.createCell(i).setCellValue(value) }
            }
            headers.indices.forEach { sheet.setColumnWidth(it, 18 * 256) }
            val out = ByteArrayOutputStream()
            wb.write(out)
            return out.toByteArray()
        }
    }

    private fun expr(term: Term, type: String): String =
        term.expressions.firstOrNull { it.expressionType.value == type }?.expressionValue ?: ""

    private fun aliasNames(term: Term, type: AliasType): String =
        term.aliases.filter { it.aliasType == type }.joinToString(", ") { it.aliasName }
}
