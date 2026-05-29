package com.aulms.term

import com.fasterxml.jackson.databind.ObjectMapper
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
class TermApiContractTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @Test
    fun `get customer number term`() {
        mockMvc.perform(get("/terms/T-000001"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.termId").value("T-000001"))
            .andExpect(jsonPath("$.koreanName").value("고객번호"))
            .andExpect(jsonPath("$.expressions[*].expressionValue", hasItem("customerNumber")))
            .andExpect(jsonPath("$.aliases[*].aliasName", hasItem("고객ID")))
    }

    @Test
    fun `list terms by customer id alias`() {
        mockMvc.perform(get("/terms").param("q", "customerId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[0].termId").value("T-000001"))
            .andExpect(jsonPath("$.items[0].apiFieldName").value("customerNumber"))
    }

    @Test
    fun `create and approve term`() {
        val request = """
            {
              "domainName": "고객",
              "usageType": "표준항목",
              "koreanName": "고객등급코드",
              "englishName": "Customer Grade Code",
              "englishAbbreviation": "CUST_GRD_CD",
              "businessDefinition": "고객의 등급을 분류하는 코드",
              "usageContext": "고객 조회와 혜택 계산에서 사용",
              "physicalType": "VARCHAR",
              "digits": 10,
              "decimalPoint": 0,
              "owner": "고객도메인 데이터스튜어드",
              "status": "Draft"
            }
        """.trimIndent()

        val result = mockMvc.perform(post("/terms").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.status").value("Draft"))
            .andReturn()
        val createdTermId = objectMapper.readTree(result.response.contentAsString).get("termId").asText()

        val approveRequest = """{"approver":"data.steward","reason":"검토 완료"}"""
        mockMvc.perform(post("/terms/$createdTermId/approve").contentType(MediaType.APPLICATION_JSON).content(approveRequest))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Approved"))
    }
}
