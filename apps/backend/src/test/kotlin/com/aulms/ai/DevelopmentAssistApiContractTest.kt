package com.aulms.ai

import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class DevelopmentAssistApiContractTest(@Autowired private val mockMvc: MockMvc) {
    @Test
    fun `development assist maps customer order requirement to approved terms and artifacts`() {
        val request = """
            {
              "requirementText": "고객별 주문 내역을 조회하는 API 만들어줘.",
              "targetArtifacts": ["DTO", "OPENAPI_SCHEMA", "SQL_EXAMPLE"],
              "domainNames": ["고객", "주문"]
            }
        """.trimIndent()

        mockMvc.perform(post("/ai/development-assist").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.requirementText").value("고객별 주문 내역을 조회하는 API 만들어줘."))
            .andExpect(jsonPath("$.extractedConcepts[*].concept", hasItem("고객번호")))
            .andExpect(jsonPath("$.termMappings[*].standardTerm", hasItem("고객번호")))
            .andExpect(jsonPath("$.termMappings[*].standardTerm", hasItem("주문번호")))
            .andExpect(jsonPath("$.termMappings[*].standardTerm", hasItem("주문일자")))
            .andExpect(jsonPath("$.termMappings[*].standardTerm", hasItem("주문금액")))
            .andExpect(jsonPath("$.termMappings[*].standardTerm", hasItem("주문상태코드")))
            .andExpect(jsonPath("$.termMappings[*].apiField", hasItem("customerNumber")))
            .andExpect(jsonPath("$.termMappings[*].apiField", hasItem("orderNumber")))
            .andExpect(jsonPath("$.termMappings[*].apiField", hasItem("orderDate")))
            .andExpect(jsonPath("$.termMappings[*].apiField", hasItem("orderAmount")))
            .andExpect(jsonPath("$.termMappings[*].apiField", hasItem("orderStatusCode")))
            .andExpect(jsonPath("$.candidateTerms.length()").value(0))
            .andExpect(jsonPath("$.generatedArtifacts[*].artifactType", hasItem("DTO")))
            .andExpect(jsonPath("$.generatedArtifacts[*].artifactType", hasItem("OPENAPI_SCHEMA")))
            .andExpect(jsonPath("$.generatedArtifacts[*].artifactType", hasItem("SQL_EXAMPLE")))
            .andExpect(jsonPath("$.generatedArtifacts[0].content", containsString("val customerNumber: String")))
            .andExpect(jsonPath("$.generatedArtifacts[0].content", containsString("val orderAmount: BigDecimal")))
    }

    @Test
    fun `development assist separates unknown term as candidate and warns non standard alias`() {
        val request = """
            {
              "requirementText": "고객ID로 고객 선호 배송 시간대를 저장하는 API 만들어줘.",
              "targetArtifacts": ["DTO"],
              "domainNames": ["고객"]
            }
        """.trimIndent()

        mockMvc.perform(post("/ai/development-assist").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.termMappings[*].standardTerm", hasItem("고객번호")))
            .andExpect(jsonPath("$.candidateTerms[*].candidateTerm", hasItem("고객선호배송시간대")))
            .andExpect(jsonPath("$.candidateTerms[0].approvalRequired").value(true))
            .andExpect(jsonPath("$.warnings[*].inputExpression", hasItem("고객ID")))
            .andExpect(jsonPath("$.warnings[*].standardTerm", hasItem("고객번호")))
    }
}
