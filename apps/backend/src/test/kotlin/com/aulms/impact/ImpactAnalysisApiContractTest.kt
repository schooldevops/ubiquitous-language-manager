package com.aulms.impact

import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ImpactAnalysisApiContractTest(@Autowired private val mockMvc: MockMvc) {
    @Test
    fun `analyze customer number api field rename impact`() {
        mockMvc.perform(get("/impact/terms/T-000001?changeType=API_FIELD_RENAME&includeTwoHop=true"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.termId").value("T-000001"))
            .andExpect(jsonPath("$.standardTerm").value("고객번호"))
            .andExpect(jsonPath("$.changeType").value("API_FIELD_RENAME"))
            .andExpect(jsonPath("$.includeTwoHop").value(true))
            .andExpect(jsonPath("$.riskLevel").value("HIGH"))
            .andExpect(jsonPath("$.impactedTargets[*].targetType", hasItem("DB_COLUMN")))
            .andExpect(jsonPath("$.impactedTargets[*].targetType", hasItem("API_FIELD")))
            .andExpect(jsonPath("$.impactedTargets[*].targetType", hasItem("DTO")))
            .andExpect(jsonPath("$.impactedTargets[*].targetType", hasItem("DOCUMENT")))
            .andExpect(jsonPath("$.impactedTargets[*].targetType", hasItem("TEST_CASE")))
            .andExpect(jsonPath("$.impactedTargets[*].targetName", hasItem("CUST_NO")))
            .andExpect(jsonPath("$.impactedTargets[*].targetName", hasItem("customerNumber")))
            .andExpect(jsonPath("$.impactedTargets[*].targetName", hasItem("고객번호 기획서 표현")))
            .andExpect(jsonPath("$.impactedTargets[*].targetName", hasItem("고객번호 필수 검증")))
            .andExpect(jsonPath("$.recommendations[*].action", hasItem("OpenAPI v2 필드와 하위 호환 기간을 정의한다")))
    }
}
