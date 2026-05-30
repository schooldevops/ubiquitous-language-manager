package com.aulms.artifact

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactValidationApiContractTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
    @Test
    fun `openapi customerId field recommends customerNumber`() {
        val request = mapOf(
            "sourceType" to "OPENAPI",
            "filePath" to "fixtures/artifacts/customer-openapi.yaml",
            "content" to fixture("fixtures/artifacts/customer-openapi.yaml"),
            "includeSuggestions" to true,
        )

        mockMvc.perform(
            post("/artifact-validations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sourceType").value("OPENAPI"))
            .andExpect(jsonPath("$.checkedCount").value(2))
            .andExpect(jsonPath("$.summary.warningCount").value(1))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("customerId")))
            .andExpect(jsonPath("$.issues[*].standardTerm", hasItem("고객번호")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("customerNumber")))
    }

    @Test
    fun `ddl CUST_ID column returns CUST_NO error`() {
        val request = mapOf(
            "sourceType" to "DDL",
            "filePath" to "fixtures/artifacts/customer-ddl.sql",
            "content" to fixture("fixtures/artifacts/customer-ddl.sql"),
            "includeSuggestions" to true,
        )

        mockMvc.perform(
            post("/artifact-validations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sourceType").value("DDL"))
            .andExpect(jsonPath("$.checkedCount").value(3))
            .andExpect(jsonPath("$.summary.errorCount").value(3))
            .andExpect(jsonPath("$.exitCode").value(1))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("CUST_ID")))
            .andExpect(jsonPath("$.issues[*].standardTerm", hasItem("고객번호")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("CUST_NO")))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("T-000007")))
    }

    @Test
    fun `kotlin customerId property recommends customerNumber`() {
        val request = mapOf(
            "sourceType" to "KOTLIN",
            "filePath" to "fixtures/artifacts/customer-dto.kt",
            "content" to fixture("fixtures/artifacts/customer-dto.kt"),
            "includeSuggestions" to true,
        )

        mockMvc.perform(
            post("/artifact-validations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sourceType").value("KOTLIN"))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("customerId")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("customerNumber")))
            .andExpect(jsonPath("$.issues[*].location", hasItem("fixtures/artifacts/customer-dto.kt:4")))
    }

    @Test
    fun `sql mapper validates db columns and parameters`() {
        val request = mapOf(
            "sourceType" to "SQL",
            "filePath" to "fixtures/artifacts/customer-mapper.sql",
            "content" to fixture("fixtures/artifacts/customer-mapper.sql"),
            "includeSuggestions" to true,
        )

        mockMvc.perform(
            post("/artifact-validations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sourceType").value("SQL"))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("CUST_ID")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("CUST_NO")))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("customerId")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("customerNumber")))
    }

    @Test
    fun `test sentence customer ID recommends customer number`() {
        val request = mapOf(
            "sourceType" to "TEST",
            "filePath" to "fixtures/artifacts/customer-order.feature",
            "content" to fixture("fixtures/artifacts/customer-order.feature"),
            "includeSuggestions" to true,
        )

        mockMvc.perform(
            post("/artifact-validations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sourceType").value("TEST"))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("고객 ID")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("고객번호")))
            .andExpect(jsonPath("$.issues[*].location", hasItem("fixtures/artifacts/customer-order.feature:3")))
    }

    @Test
    fun `review openapi endpoint returns artifact validation issues`() {
        val request = mapOf(
            "sourceType" to "AUTO",
            "filePath" to "fixtures/artifacts/customer-openapi.yaml",
            "content" to fixture("fixtures/artifacts/customer-openapi.yaml"),
        )

        mockMvc.perform(
            post("/reviews/openapi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sourceType").value("OPENAPI"))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("customerId")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("customerNumber")))
    }

    @Test
    fun `review ddl endpoint returns artifact validation issues`() {
        val request = mapOf(
            "sourceType" to "AUTO",
            "filePath" to "fixtures/artifacts/customer-ddl.sql",
            "content" to fixture("fixtures/artifacts/customer-ddl.sql"),
        )

        mockMvc.perform(
            post("/reviews/ddl")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sourceType").value("DDL"))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("CUST_ID")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("CUST_NO")))
    }

    @Test
    fun `review code endpoint infers kotlin source and returns artifact validation issues`() {
        val request = mapOf(
            "sourceType" to "AUTO",
            "filePath" to "fixtures/artifacts/customer-dto.kt",
            "content" to fixture("fixtures/artifacts/customer-dto.kt"),
        )

        mockMvc.perform(
            post("/reviews/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.sourceType").value("KOTLIN"))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("customerId")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("customerNumber")))
    }

    @Test
    fun `review pr endpoint aggregates file validation results`() {
        val request = mapOf(
            "pullRequestId" to "123",
            "repository" to "aulms",
            "files" to listOf(
                mapOf(
                    "filePath" to "fixtures/artifacts/customer-openapi.yaml",
                    "content" to fixture("fixtures/artifacts/customer-openapi.yaml"),
                ),
                mapOf(
                    "filePath" to "fixtures/artifacts/customer-dto.kt",
                    "content" to fixture("fixtures/artifacts/customer-dto.kt"),
                ),
            ),
        )

        mockMvc.perform(
            post("/reviews/pr")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.pullRequestId").value("123"))
            .andExpect(jsonPath("$.repository").value("aulms"))
            .andExpect(jsonPath("$.results.length()").value(2))
            .andExpect(jsonPath("$.issues[*].inputExpression", hasItem("customerId")))
            .andExpect(jsonPath("$.issues[*].recommendedExpression", hasItem("customerNumber")))
            .andExpect(jsonPath("$.summary.warningCount").value(2))
    }

    private fun fixture(path: String): String =
        ClassPathResource(path).inputStream.bufferedReader().use { it.readText() }
}
