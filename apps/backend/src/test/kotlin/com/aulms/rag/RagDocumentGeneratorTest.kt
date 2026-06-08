package com.aulms.rag

import com.aulms.term.InMemoryTermRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class RagDocumentGeneratorTest {
    private val generator = RagDocumentGenerator(InMemoryTermRepository())

    @Test
    fun `customer number document contains standard fields aliases and relationships`() {
        val customerDocument = generator.renderTermDocument(
            InMemoryTermRepository().searchDocuments().first { it.term.termId == "T-000001" },
        )

        assertThat(customerDocument).contains("# 고객번호")
        assertThat(customerDocument).contains("Customer Number")
        assertThat(customerDocument).contains("CUST_NO")
        assertThat(customerDocument).contains("customerNumber")
        assertThat(customerDocument).contains("고객ID")
        assertThat(customerDocument).contains("customerId")
        assertThat(customerDocument).contains("CUST_ID")
        assertThat(customerDocument).contains("주문번호")
        assertThat(customerDocument).contains("고객번호를 입력하면 주문 목록을 조회한다.")
    }

    @Test
    fun `batch writes term markdown files`(@TempDir tempDir: Path) {
        val files = generator.generateTermDocuments(tempDir)
        val customerFile = tempDir.resolve("T-000001-customer-number.md")

        assertThat(files).anyMatch { it.fileName.toString() == "T-000001-customer-number.md" }
        assertThat(Files.exists(customerFile)).isTrue()
        assertThat(Files.readString(customerFile)).contains("DB 컬럼명 | CUST_NO")
    }
}
