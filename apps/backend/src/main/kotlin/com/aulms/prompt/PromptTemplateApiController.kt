package com.aulms.prompt

import com.aulms.api.PromptTemplateApi
import com.aulms.model.PromptTemplatePreviewRequest
import com.aulms.model.PromptTemplateStatus
import com.aulms.model.PromptTemplateType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PromptTemplateApiController(private val service: PromptTemplateService) : PromptTemplateApi {
    override fun listPromptTemplates(type: PromptTemplateType?, status: PromptTemplateStatus?) =
        ResponseEntity.ok(service.listPromptTemplates(type, status))

    override fun getPromptTemplate(templateId: String) =
        ResponseEntity.ok(service.getPromptTemplate(templateId))

    override fun listPromptTemplateVersions(templateId: String) =
        ResponseEntity.ok(service.listPromptTemplateVersions(templateId))

    override fun previewPromptTemplate(templateId: String, promptTemplatePreviewRequest: PromptTemplatePreviewRequest) =
        ResponseEntity.ok(service.previewPromptTemplate(templateId, promptTemplatePreviewRequest))
}
