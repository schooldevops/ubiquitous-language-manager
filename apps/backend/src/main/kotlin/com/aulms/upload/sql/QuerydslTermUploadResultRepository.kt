package com.aulms.upload.sql

import com.aulms.model.TermUploadBatchSummary
import com.aulms.model.TermUploadResult
import com.aulms.model.TermUploadRow
import com.aulms.model.TermUploadStatus
import com.aulms.persistence.PostgresSequences
import com.querydsl.sql.SQLQueryFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
@Profile("postgres")
@Transactional
class QuerydslTermUploadResultRepository(
    private val queryFactory: SQLQueryFactory,
    private val sequences: PostgresSequences,
) {
    private val u = QTermUploadRow.termUploadRow

    fun nextBatchId(today: String): String =
        "UPL-%s-%04d".format(today, sequences.next("term_upload_batch_seq"))

    fun saveRow(
        batchId: String,
        lineNo: Int,
        termId: String?,
        status: TermUploadStatus,
        rawJson: String,
        errorMessage: String?,
        uploadedAt: OffsetDateTime,
    ) {
        val success = status != TermUploadStatus.FAILED
        queryFactory.insert(u)
            .set(u.uploadBatchId, batchId)
            .set(u.lineNo, lineNo)
            .set(u.termId, termId)
            .set(u.status, status.value)
            .set(u.rawJson, rawJson)
            .set(u.errorMessage, errorMessage)
            .set(u.registeredAt, if (success) uploadedAt else null)
            .set(u.failedAt, if (success) null else uploadedAt)
            .set(u.uploadedAt, uploadedAt)
            .execute()
    }

    fun appendError(batchId: String, lineNo: Int, message: String) {
        val existing = queryFactory.select(u.errorMessage).from(u)
            .where(u.uploadBatchId.eq(batchId).and(u.lineNo.eq(lineNo))).fetchFirst()
        val merged = if (existing.isNullOrBlank()) message else "$existing | $message"
        queryFactory.update(u)
            .where(u.uploadBatchId.eq(batchId).and(u.lineNo.eq(lineNo)))
            .set(u.errorMessage, merged)
            .execute()
    }

    fun result(batchId: String): TermUploadResult {
        val tuples = queryFactory.select(*u.all()).from(u)
            .where(u.uploadBatchId.eq(batchId)).orderBy(u.lineNo.asc()).fetch()
        val rows = tuples.map {
            TermUploadRow(
                lineNo = it.get(u.lineNo)!!,
                status = TermUploadStatus.forValue(it.get(u.status)!!),
                termId = it.get(u.termId),
                errorMessage = it.get(u.errorMessage),
                registeredAt = it.get(u.registeredAt),
                failedAt = it.get(u.failedAt),
            )
        }
        val uploadedAt = tuples.firstOrNull()?.get(u.uploadedAt) ?: OffsetDateTime.now()
        return TermUploadResult(
            uploadBatchId = batchId,
            uploadedAt = uploadedAt,
            totalRows = rows.size,
            inserted = rows.count { it.status == TermUploadStatus.INSERTED },
            updated = rows.count { it.status == TermUploadStatus.UPDATED },
            failed = rows.count { it.status == TermUploadStatus.FAILED },
            rows = rows,
        )
    }

    fun listBatches(): List<TermUploadBatchSummary> {
        val ids = queryFactory.select(u.uploadBatchId, u.uploadedAt.max())
            .from(u).groupBy(u.uploadBatchId).orderBy(u.uploadedAt.max().desc()).fetch()
        return ids.map { tuple ->
            val batchId = tuple.get(u.uploadBatchId)!!
            val r = result(batchId)
            TermUploadBatchSummary(
                uploadBatchId = batchId,
                uploadedAt = r.uploadedAt,
                totalRows = r.totalRows,
                inserted = r.inserted,
                updated = r.updated,
                failed = r.failed,
            )
        }
    }
}
