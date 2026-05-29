package com.aulms.search

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
class SearchApiContractTest(@Autowired private val mockMvc: MockMvc) {
    @Test
    fun `exact search finds customer number by korean name db column and api field`() {
        listOf("고객번호", "CUST_NO", "customerNumber", "Customer-Number", "cust no").forEach { query ->
            mockMvc.perform(get("/search/exact").param("q", query))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.items[0].termId").value("T-000001"))
                .andExpect(jsonPath("$.items[0].standardTerm").value("고객번호"))
                .andExpect(jsonPath("$.items[0].englishName").value("Customer Number"))
                .andExpect(jsonPath("$.items[0].dbColumn").value("CUST_NO"))
                .andExpect(jsonPath("$.items[0].apiField").value("customerNumber"))
                .andExpect(jsonPath("$.items[0].status").value("Approved"))
        }
    }

    @Test
    fun `alias search recommends customer number for customer id`() {
        mockMvc.perform(get("/search/alias").param("q", "고객ID"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.query").value("고객ID"))
            .andExpect(jsonPath("$.items[0].termId").value("T-000001"))
            .andExpect(jsonPath("$.items[0].standardTerm").value("고객번호"))
            .andExpect(jsonPath("$.items[0].matchedExpressions[0].expressionValue").value("고객ID"))
            .andExpect(jsonPath("$.items[0].matchedExpressions[0].matchType").value("Alias"))
            .andExpect(jsonPath("$.items[0].recommendations[0].recommendedTerm").value("고객번호"))
            .andExpect(jsonPath("$.items[0].recommendations[0].recommendedExpression").value("customerNumber"))
            .andExpect(jsonPath("$.items[0].recommendations[0].severity").value("Warning"))
    }

    @Test
    fun `deprecated search returns forbidden reason and replacement db column`() {
        mockMvc.perform(get("/search/deprecated").param("q", "CUST_ID"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.query").value("CUST_ID"))
            .andExpect(jsonPath("$.items[0].deprecatedExpression").value("CUST_ID"))
            .andExpect(jsonPath("$.items[0].reason").value("기술 ID와 업무 고객번호가 혼동될 수 있음"))
            .andExpect(jsonPath("$.items[0].replacementTerm.termId").value("T-000001"))
            .andExpect(jsonPath("$.items[0].replacementTerm.dbColumn").value("CUST_NO"))
            .andExpect(jsonPath("$.items[0].recommendation.action").value("ReplaceDeprecatedTerm"))
            .andExpect(jsonPath("$.items[0].recommendation.recommendedExpression").value("CUST_NO"))
            .andExpect(jsonPath("$.items[0].recommendation.severity").value("Error"))
    }

    @Test
    fun `domain search returns approved order terms`() {
        mockMvc.perform(get("/search/domain/주문").param("status", "Approved"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.query").value("주문"))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("주문번호")))
            .andExpect(jsonPath("$.page.totalElements").value(6))
    }
}
