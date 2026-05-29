package com.aulms.search

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
class SemanticSearchApiContractTest(@Autowired private val mockMvc: MockMvc) {
    @Test
    fun `semantic search recommends order date and order date time`() {
        val request = """
            {
              "query": "주문이 발생한 날짜",
              "domainNames": ["주문"],
              "statuses": ["Approved"],
              "limit": 5
            }
        """.trimIndent()

        mockMvc.perform(post("/search/semantic").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.query").value("주문이 발생한 날짜"))
            .andExpect(jsonPath("$.items[0].standardTerm").value("주문일자"))
            .andExpect(jsonPath("$.items[*].standardTerm", hasItem("주문일시")))
            .andExpect(jsonPath("$.items[0].dbColumn").value("ORD_DT"))
            .andExpect(jsonPath("$.items[0].apiField").value("orderDate"))
            .andExpect(jsonPath("$.items[0].recommendationReason").isNotEmpty)
            .andExpect(jsonPath("$.items[0].differenceDescription").value("주문일자는 날짜만 필요할 때 사용하고 주문일시는 시분초까지 필요할 때 사용함"))
    }
}
