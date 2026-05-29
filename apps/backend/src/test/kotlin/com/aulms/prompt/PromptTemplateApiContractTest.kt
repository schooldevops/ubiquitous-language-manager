package com.aulms.prompt

import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PromptTemplateApiContractTest(@Autowired private val mockMvc: MockMvc) {
    @Test
    fun `list get versions and preview vibe coding prompt template`() {
        mockMvc.perform(get("/prompt-templates").param("type", "VibeCoding").param("status", "Active"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[0].templateId").value("PT-VIBE-001"))
            .andExpect(jsonPath("$.items[0].version").value("1.0.0"))

        mockMvc.perform(get("/prompt-templates/PT-VIBE-001"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("사내 데이터 사전 기반 개발 규칙"))
            .andExpect(jsonPath("$.variables[*].name", hasItem("termMappings")))
            .andExpect(jsonPath("$.versionPolicy", containsString("MAJOR")))
            .andExpect(jsonPath("$.histories[*].changeType", hasItem("Created")))

        mockMvc.perform(get("/prompt-templates/PT-VIBE-001/versions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[0].templateId").value("PT-VIBE-001"))
            .andExpect(jsonPath("$.items[0].version").value("1.0.0"))

        val previewRequest = """
            {
              "requirementText": "고객별 주문 내역을 조회하는 API 만들어줘.",
              "termMappings": [
                {
                  "concept": "고객번호",
                  "standardTerm": "고객번호",
                  "englishName": "Customer Number",
                  "dbColumn": "CUST_NO",
                  "apiField": "customerNumber",
                  "codeVariable": "customerNumber",
                  "status": "Approved"
                },
                {
                  "concept": "주문번호",
                  "standardTerm": "주문번호",
                  "englishName": "Order Number",
                  "dbColumn": "ORD_NO",
                  "apiField": "orderNumber",
                  "codeVariable": "orderNumber",
                  "status": "Approved"
                }
              ],
              "candidateTerms": [],
              "additionalContext": "Kotlin Spring Boot와 OpenAPI 기반으로 생성한다."
            }
        """.trimIndent()

        mockMvc.perform(
            post("/prompt-templates/PT-VIBE-001/preview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(previewRequest),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.templateId").value("PT-VIBE-001"))
            .andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.renderedPrompt", containsString("고객별 주문 내역을 조회하는 API 만들어줘.")))
            .andExpect(jsonPath("$.renderedPrompt", containsString("| 고객번호 | 고객번호 | Customer Number | CUST_NO | customerNumber | customerNumber | Approved |")))
            .andExpect(jsonPath("$.renderedPrompt", containsString("서버 API는 반드시 OpenAPI Spec을 먼저 작성")))
            .andExpect(jsonPath("$.injectedVariables", hasItem("termMappings")))
    }

    @Test
    fun `return not found for unknown prompt template`() {
        mockMvc.perform(get("/prompt-templates/PT-UNKNOWN"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("PROMPT_TEMPLATE_NOT_FOUND"))
    }
}
