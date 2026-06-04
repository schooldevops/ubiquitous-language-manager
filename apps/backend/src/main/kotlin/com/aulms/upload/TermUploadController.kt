package com.aulms.upload

import com.aulms.api.TermUploadApi
import com.aulms.model.TermUploadBatchListResponse
import com.aulms.model.TermUploadResult
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Profile("postgres")
class TermUploadController(private val service: TermUploadService) : TermUploadApi {

    override fun uploadTerms(file: MultipartFile): ResponseEntity<TermUploadResult> {
        val content = file.bytes.toString(Charsets.UTF_8)
        return ResponseEntity.ok(service.upload(content))
    }

    override fun getTermUploadBatch(uploadBatchId: String): ResponseEntity<TermUploadResult> =
        ResponseEntity.ok(service.result(uploadBatchId))

    override fun listTermUploadBatches(): ResponseEntity<TermUploadBatchListResponse> =
        ResponseEntity.ok(TermUploadBatchListResponse(items = service.listBatches()))
}
