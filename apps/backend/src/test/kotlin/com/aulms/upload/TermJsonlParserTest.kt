package com.aulms.upload

import com.aulms.model.AliasType
import com.aulms.model.ExpressionType
import com.aulms.model.TermStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TermJsonlParserTest {
    private val parser = TermJsonlParser()

    @Test
    fun `parses hierarchical row with expressions aliases relationships`() {
        val jsonl = """
            {"termId":"T-000001","termNumber":"TERM-000001","domainName":"고객","usageType":"표준항목","koreanName":"고객번호","englishName":"Customer Number","englishAbbreviation":"CUST_NO","businessDefinition":"def","physicalType":"VARCHAR","digits":20,"decimalPoint":0,"status":"Approved","owner":"steward","version":"1.0","expressions":[{"expressionId":36,"expressionType":"Korean","expressionValue":"고객번호","isStandard":true,"language":"ko","style":"standard"}],"aliases":{"synonyms":[{"aliasId":"A-000001","aliasName":"고객ID","aliasType":"Synonym","recommendationAction":"x","reason":"y"}],"forbidden":[{"aliasId":"A-000006","aliasName":"CUST_ID","aliasType":"Forbidden","recommendationAction":"x","reason":"y"}]},"relationships":[{"relationshipId":7,"relationshipType":"usedWith","targetTermId":"T-000002","description":"d"}]}
        """.trimIndent()

        val rows = parser.parse(jsonl)

        assertEquals(1, rows.size)
        val term = assertNotNull(rows[0].term)
        assertEquals("T-000001", term.termId)
        assertEquals(TermStatus.Approved, term.status)
        assertEquals(1, term.expressions.size)
        assertEquals(ExpressionType.Korean, term.expressions[0].expressionType)
        assertEquals(2, term.aliases.size)
        assertEquals(AliasType.Forbidden, term.aliases.first { it.aliasId == "A-000006" }.aliasType)
        assertEquals(1, term.relationships.size)
        assertEquals("T-000002", term.relationships[0].targetTermId)
        assertNull(rows[0].parseError)
    }

    @Test
    fun `broken json line becomes parse error row`() {
        val rows = parser.parse("{not json}")
        assertEquals(1, rows.size)
        assertNull(rows[0].term)
        assertNotNull(rows[0].parseError)
    }

    @Test
    fun `blank lines are skipped`() {
        val rows = parser.parse("\n   \n")
        assertEquals(0, rows.size)
    }

    @Test
    fun `row missing termId becomes parse error`() {
        val rows = parser.parse("""{"koreanName":"x"}""")
        assertEquals(1, rows.size)
        assertNull(rows[0].term)
        assertNotNull(rows[0].parseError)
    }
}
