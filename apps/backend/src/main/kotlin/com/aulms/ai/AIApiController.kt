package com.aulms.ai

import com.aulms.api.AIApi
import com.aulms.model.DevelopmentAssistRequest
import com.aulms.model.TermRecommendationRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AIApiController(
    private val service: DevelopmentAssistService,
    private val termRecommendationService: TermRecommendationService,
) : AIApi {
    override fun developmentAssist(developmentAssistRequest: DevelopmentAssistRequest) =
        ResponseEntity.ok(service.developmentAssist(developmentAssistRequest))

    override fun recommendTermDraft(termRecommendationRequest: TermRecommendationRequest) =
        ResponseEntity.ok(termRecommendationService.recommend(termRecommendationRequest))
}
