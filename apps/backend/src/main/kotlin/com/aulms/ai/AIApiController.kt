package com.aulms.ai

import com.aulms.api.AIApi
import com.aulms.model.DevelopmentAssistRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AIApiController(private val service: DevelopmentAssistService) : AIApi {
    override fun developmentAssist(developmentAssistRequest: DevelopmentAssistRequest) =
        ResponseEntity.ok(service.developmentAssist(developmentAssistRequest))
}
