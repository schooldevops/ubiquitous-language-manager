package com.aulms.dashboard

import com.aulms.api.DashboardApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DashboardApiController(private val service: DashboardService) : DashboardApi {
    override fun getDashboard(recentLimit: Int): ResponseEntity<com.aulms.model.DashboardSummary> =
        ResponseEntity.ok(service.getDashboard(recentLimit))
}
