package com.aulms.ai

import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@AutoConfigureMockMvc
// 계약 테스트는 결정적 규칙기반(heuristic) 결과를 검증한다. 로컬 Ollama 등 LLM 가용 여부에
// 영향받지 않도록 알 수 없는 provider 로 강제해 LLM 경로를 비활성화한다.
@TestPropertySource(properties = ["aulms.llm.provider=disabled"])
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

    @Test
    fun `term recommendation reuses existing approved term when exact standard term exists`() {
        val request = """
            {
              "koreanName": "주문번호",
              "mode": "TERM_CREATE"
            }
        """.trimIndent()

        mockMvc.perform(post("/ai/term-recommendation").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.normalizedKoreanName").value("주문번호"))
            .andExpect(jsonPath("$.recommendation.domainName").value("주문"))
            .andExpect(jsonPath("$.recommendation.englishName").value("Order Number"))
            .andExpect(jsonPath("$.recommendation.englishAbbreviation").value("ORD_NO"))
            .andExpect(jsonPath("$.ragMatches[*].source", hasItem("Exact")))
            .andExpect(jsonPath("$.warnings[0]", containsString("동일한 표준 용어")))
    }

    @Test
    fun `term recommendation gathers rag and graph context before inferring new candidate`() {
        val request = """
            {
              "koreanName": "고객선호배송시간대",
              "mode": "CANDIDATE_CREATE",
              "currentDomainName": "고객"
            }
        """.trimIndent()

        mockMvc.perform(post("/ai/term-recommendation").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.recommendation.domainName").value("고객"))
            .andExpect(jsonPath("$.recommendation.englishName").value("Customer Preferred Delivery Time Slot"))
            .andExpect(jsonPath("$.recommendation.englishAbbreviation").value("CUST_PREF_DLV_TM_SLOT"))
            .andExpect(jsonPath("$.graphContext.inferredDomainName").value("고객"))
            .andExpect(jsonPath("$.graphContext.relatedTerms[*]", hasItems("고객번호", "고객명")))
            .andExpect(jsonPath("$.llmReasoning", containsString("RAG 근거")))
    }
}
