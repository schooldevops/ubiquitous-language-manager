package com.aulms.upload.sql

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.ColumnMetadata
import com.querydsl.sql.RelationalPathBase
import java.sql.Types
import java.time.OffsetDateTime

class QTermUploadRow(variable: String) : RelationalPathBase<QTermUploadRow>(QTermUploadRow::class.java, forVariable(variable), "public", "term_upload_row") {
    val uploadRowId: NumberPath<Long> = createNumber("uploadRowId", Long::class.javaObjectType)
    val uploadBatchId: StringPath = createString("uploadBatchId")
    val lineNo: NumberPath<Int> = createNumber("lineNo", Int::class.javaObjectType)
    val termId: StringPath = createString("termId")
    val status: StringPath = createString("status")
    val rawJson: StringPath = createString("rawJson")
    val errorMessage: StringPath = createString("errorMessage")
    val registeredAt: DateTimePath<OffsetDateTime> = createDateTime("registeredAt", OffsetDateTime::class.java)
    val failedAt: DateTimePath<OffsetDateTime> = createDateTime("failedAt", OffsetDateTime::class.java)
    val uploadedAt: DateTimePath<OffsetDateTime> = createDateTime("uploadedAt", OffsetDateTime::class.java)

    init {
        addMetadata(uploadRowId, ColumnMetadata.named("upload_row_id").withIndex(1).ofType(Types.BIGINT))
        addMetadata(uploadBatchId, ColumnMetadata.named("upload_batch_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(lineNo, ColumnMetadata.named("line_no").withIndex(3).ofType(Types.INTEGER))
        addMetadata(termId, ColumnMetadata.named("term_id").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(status, ColumnMetadata.named("status").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(rawJson, ColumnMetadata.named("raw_json").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(errorMessage, ColumnMetadata.named("error_message").withIndex(7).ofType(Types.VARCHAR))
        addMetadata(registeredAt, ColumnMetadata.named("registered_at").withIndex(8).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
        addMetadata(failedAt, ColumnMetadata.named("failed_at").withIndex(9).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
        addMetadata(uploadedAt, ColumnMetadata.named("uploaded_at").withIndex(10).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val termUploadRow = QTermUploadRow("term_upload_row")
    }
}
