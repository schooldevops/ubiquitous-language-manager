package com.aulms.impact

import com.aulms.api.ImpactApi
import com.aulms.model.ImpactChangeType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ImpactApiController(private val service: ImpactAnalysisService) : ImpactApi {
    override fun getTermImpact(termId: String, changeType: ImpactChangeType?, includeTwoHop: Boolean) =
        ResponseEntity.ok(service.getTermImpact(termId, changeType, includeTwoHop))
}
