package com.aulms.candidate

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
class CandidateApiContractTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @Test
    fun `create review and promote candidate to approved term`() {
        val createRequest = """
            {
              "koreanName": "고객선호배송시간대",
              "englishName": "Customer Preferred Delivery Time Slot",
              "englishAbbreviation": "CUST_PREF_DLV_TM_SLOT",
              "domainName": "고객",
              "businessDefinition": "고객이 선호하는 배송 시간대",
              "usageContext": "배송 옵션 추천과 배송 요청 화면에서 사용",
              "physicalType": "VARCHAR",
              "digits": 20,
              "decimalPoint": 0,
              "requestedBy": "planner01"
            }
        """.trimIndent()

        val created = mockMvc.perform(post("/candidates").contentType(MediaType.APPLICATION_JSON).content(createRequest))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.status").value("Draft"))
            .andExpect(jsonPath("$.similarTerms[*].koreanName", hasItem("고객번호")))
            .andReturn()
        val candidateId = objectMapper.readTree(created.response.contentAsString).get("candidateId").asText()

        mockMvc.perform(get("/candidates").param("q", "배송시간대"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[*].candidateId", hasItem(candidateId)))

        val reviewRequest = """{"reviewer":"data.steward","decision":"Approve","reason":"중복 없음, 약어와 타입 적합"}"""
        mockMvc.perform(post("/candidates/$candidateId/review").contentType(MediaType.APPLICATION_JSON).content(reviewRequest))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Approved"))
            .andExpect(jsonPath("$.histories[*].status", hasItem("Approved")))

        val promoteRequest = """{"approver":"data.steward","owner":"고객도메인 데이터스튜어드","reason":"표준 용어로 승인"}"""
        val promoted = mockMvc.perform(post("/candidates/$candidateId/promote").contentType(MediaType.APPLICATION_JSON).content(promoteRequest))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.candidate.status").value("Promoted"))
            .andExpect(jsonPath("$.term.koreanName").value("고객선호배송시간대"))
            .andExpect(jsonPath("$.term.status").value("Approved"))
            .andExpect(jsonPath("$.term.expressions[*].expressionValue", hasItem("customerPreferredDeliveryTimeSlot")))
            .andReturn()
        val termId = objectMapper.readTree(promoted.response.contentAsString).get("term").get("termId").asText()

        mockMvc.perform(get("/terms/$termId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.englishAbbreviation").value("CUST_PREF_DLV_TM_SLOT"))
            .andExpect(jsonPath("$.expressions[*].expressionValue", hasItem("CUST_PREF_DLV_TM_SLOT")))
    }
}
