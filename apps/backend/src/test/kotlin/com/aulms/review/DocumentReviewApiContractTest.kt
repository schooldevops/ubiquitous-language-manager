package com.aulms.review

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
class DocumentReviewApiContractTest(@Autowired private val mockMvc: MockMvc) {
    @Test
    fun `review document recommends standardized sentence and mappings`() {
        val request = """
            {
              "documentText": "고객 ID를 입력하면 주문 리스트를 조회한다.",
              "domainNames": ["고객", "주문"],
              "options": {
                "includeCandidateTerms": true,
                "includeValidationIssues": true,
                "normalizeSentences": true
              }
            }
        """.trimIndent()

        mockMvc.perform(post("/reviews/document").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.originalText").value("고객 ID를 입력하면 주문 리스트를 조회한다."))
            .andExpect(jsonPath("$.recommendedText").value("고객번호를 입력하면 주문 목록을 조회한다."))
            .andExpect(jsonPath("$.detectedTerms[*].expression", hasItem("고객 ID")))
            .andExpect(jsonPath("$.detectedTerms[*].expression", hasItem("주문 리스트")))
            .andExpect(jsonPath("$.standardMappings[*].standardTerm", hasItem("고객번호")))
            .andExpect(jsonPath("$.standardMappings[*].standardTerm", hasItem("주문목록")))
            .andExpect(jsonPath("$.standardMappings[?(@.standardTerm == '고객번호')].dbColumn", hasItem("CUST_NO")))
            .andExpect(jsonPath("$.standardMappings[?(@.standardTerm == '고객번호')].apiField", hasItem("customerNumber")))
            .andExpect(jsonPath("$.validationIssues[*].inputExpression", hasItem("고객 ID")))
    }
}
