package com.aulms.upload

import com.aulms.upload.sql.QuerydslTermUpsertRepository
import com.aulms.upload.sql.QuerydslTermUploadResultRepository
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class TermUploadServiceTest {

    private val processor = mock<TermUploadRowProcessor>()
    private val resultRepo = mock<QuerydslTermUploadResultRepository>()
    private val upsertRepo = mock<QuerydslTermUpsertRepository>()
    private val parser = TermJsonlParser()
    private val service = TermUploadService(parser, processor, resultRepo, upsertRepo)

    private fun fullRow(termId: String) =
        """{"termId":"$termId","domainName":"고객","usageType":"표준항목","koreanName":"k","englishName":"E","englishAbbreviation":"AB","businessDefinition":"d","physicalType":"VARCHAR","digits":1,"decimalPoint":0,"status":"Draft","owner":"o","version":"1.0"}"""

    @Test
    fun `isolates failed row from successful rows`() {
        whenever(resultRepo.nextBatchId(any())).thenReturn("UPL-20260604-0001")
        whenever(processor.upsertTermBody(any())).thenAnswer { inv ->
            val term = inv.getArgument<ParsedTerm>(0)
            when (term.termId) {
                "T-2" -> throw IllegalStateException("boom")
                "T-3" -> false
                else -> true
            }
        }

        val content = listOf(fullRow("T-1"), fullRow("T-2"), fullRow("T-3")).joinToString("\n")
        val counts = service.process(content)

        assertEquals(1, counts.inserted)
        assertEquals(1, counts.updated)
        assertEquals(1, counts.failed)
    }
}
