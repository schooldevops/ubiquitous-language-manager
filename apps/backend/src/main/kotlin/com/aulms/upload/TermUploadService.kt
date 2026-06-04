package com.aulms.upload

import com.aulms.model.TermUploadResult
import com.aulms.model.TermUploadStatus
import com.aulms.upload.sql.QuerydslTermUploadResultRepository
import com.aulms.upload.sql.QuerydslTermUpsertRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class UploadCounts(val inserted: Int, val updated: Int, val failed: Int)

@Service
@Profile("postgres")
class TermUploadService(
    private val parser: TermJsonlParser,
    private val processor: TermUploadRowProcessor,
    private val resultRepo: QuerydslTermUploadResultRepository,
    private val upsertRepo: QuerydslTermUpsertRepository,
) {
    private val dateFmt = DateTimeFormatter.ofPattern("yyyyMMdd")
    private val log = org.slf4j.LoggerFactory.getLogger(TermUploadService::class.java)

    /** 컨트롤러용: 처리 후 결과 DTO 반환. */
    fun upload(content: String): TermUploadResult {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val batchId = resultRepo.nextBatchId(now.format(dateFmt))
        processInto(batchId, content, now)
        upsertRepo.realignSequences()
        return resultRepo.result(batchId)
    }

    /** 테스트/집계용 진입점. */
    fun process(content: String): UploadCounts {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val batchId = resultRepo.nextBatchId(now.format(dateFmt))
        val counts = processInto(batchId, content, now)
        upsertRepo.realignSequences()
        return counts
    }

    private fun processInto(batchId: String, content: String, now: OffsetDateTime): UploadCounts {
        val rows = parser.parse(content)
        var inserted = 0
        var updated = 0
        var failed = 0
        val succeeded = mutableListOf<Pair<Int, ParsedTerm>>()

        // pass1: term 본문
        rows.forEach { row ->
            if (row.term == null) {
                failed += 1
                resultRepo.saveRow(batchId, row.lineNo, null, TermUploadStatus.FAILED, row.rawJson, row.parseError, now)
                return@forEach
            }
            try {
                val isInsert = processor.upsertTermBody(row.term)
                if (isInsert) inserted += 1 else updated += 1
                succeeded.add(row.lineNo to row.term)
                resultRepo.saveRow(
                    batchId, row.lineNo, row.term.termId,
                    if (isInsert) TermUploadStatus.INSERTED else TermUploadStatus.UPDATED,
                    row.rawJson, null, now,
                )
            } catch (ex: Exception) {
                failed += 1
                resultRepo.saveRow(batchId, row.lineNo, row.term.termId, TermUploadStatus.FAILED, row.rawJson, ex.message, now)
            }
        }

        // pass2: 관계 (term 적재 후). 관계 실패는 해당 행의 error_message에 기록 + 로그.
        succeeded.forEach { (lineNo, term) ->
            if (term.relationships.isNotEmpty()) {
                try {
                    processor.upsertRelationships(term.termId, term.relationships)
                } catch (ex: Exception) {
                    log.warn("관계 등록 실패 term={} line={}: {}", term.termId, lineNo, ex.message)
                    resultRepo.appendError(batchId, lineNo, "관계 등록 실패: ${ex.message}")
                }
            }
        }
        return UploadCounts(inserted, updated, failed)
    }

    fun result(batchId: String): TermUploadResult = resultRepo.result(batchId)
    fun listBatches() = resultRepo.listBatches()
}
