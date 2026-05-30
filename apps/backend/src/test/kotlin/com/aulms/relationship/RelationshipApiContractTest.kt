package com.aulms.relationship

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
class RelationshipApiContractTest(@Autowired private val mockMvc: MockMvc) {
    @Test
    fun `get customer number related terms`() {
        mockMvc.perform(get("/relationships/terms/T-000001"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.query").value("T-000001"))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("고객명")))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("고객상태코드")))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("주문번호")))
            .andExpect(jsonPath("$.items[*].relationshipType", hasItem("usedWith")))
            .andExpect(jsonPath("$.paths[0].nodes[*].label", hasItem("고객번호")))
    }

    @Test
    fun `get customer domain approved terms`() {
        mockMvc.perform(get("/relationships/domains/고객/terms"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("고객번호")))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("고객명")))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("고객상태코드")))
    }

    @Test
    fun `get systems using customer number column`() {
        mockMvc.perform(get("/relationships/columns/CUST_NO/systems"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.columnName").value("CUST_NO"))
            .andExpect(jsonPath("$.items[*].systemCode", hasItem("DICTIONARY")))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("고객번호")))
            .andExpect(jsonPath("$.items[0].apiFields[*]", hasItem("customerNumber")))
    }

    @Test
    fun `get forbidden and deprecated usages`() {
        mockMvc.perform(get("/relationships/deprecated"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[*].expression", hasItem("CUST_ID")))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("고객번호")))
            .andExpect(jsonPath("$.items[*].recommendedExpression", hasItem("CUST_NO")))
    }
}
