package com.aulms.rule

import com.aulms.model.ExpressionType
import com.aulms.model.TermStatus
import com.aulms.model.ValidationSeverity
import com.aulms.term.TermCommand
import com.aulms.term.InMemoryTermRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RuleEngineTest {
    private val repository = InMemoryTermRepository()
    private val ruleEngine = RuleEngine(repository)

    @Test
    fun `customerId returns customerNumber recommendation`() {
        val issues = ruleEngine.validateExpression(
            RuleValidationRequest(
                source = "CODE",
                inputExpression = "customerId",
                expressionType = ExpressionType.CODE_VARIABLE,
                location = "CustomerOrderResponse.kt:12",
            ),
        )

        assertThat(issues).hasSize(1)
        assertThat(issues[0].severity).isEqualTo(ValidationSeverity.WARNING)
        assertThat(issues[0].standardTerm).isEqualTo("고객번호")
        assertThat(issues[0].recommendedExpression).isEqualTo("customerNumber")
    }

    @Test
    fun `forbidden db column returns error and standard db column`() {
        val issues = ruleEngine.validateExpression(
            RuleValidationRequest(
                source = "DDL",
                inputExpression = "CUST_ID",
                expressionType = ExpressionType.DB_COLUMN,
            ),
        )

        assertThat(issues).hasSize(1)
        assertThat(issues[0].severity).isEqualTo(ValidationSeverity.ERROR)
        assertThat(issues[0].standardTerm).isEqualTo("고객번호")
        assertThat(issues[0].recommendedExpression).isEqualTo("CUST_NO")
    }

    @Test
    fun `approved standard api field returns no issues`() {
        val issues = ruleEngine.validateExpression(
            RuleValidationRequest(
                source = "OPENAPI",
                inputExpression = "customerNumber",
                expressionType = ExpressionType.API_FIELD,
            ),
        )

        assertThat(issues).isEmpty()
    }

    @Test
    fun `standard term used as wrong expression type returns recommended expression`() {
        val issues = ruleEngine.validateExpression(
            RuleValidationRequest(
                source = "OPENAPI",
                inputExpression = "CUST_NO",
                expressionType = ExpressionType.API_FIELD,
            ),
        )

        assertThat(issues).hasSize(1)
        assertThat(issues[0].severity).isEqualTo(ValidationSeverity.ERROR)
        assertThat(issues[0].recommendedExpression).isEqualTo("customerNumber")
    }

    @Test
    fun `draft term in development artifact returns error`() {
        repository.create(
            TermCommand(
                domainName = "고객",
                usageType = "표준항목",
                koreanName = "고객선호배송시간대",
                englishName = "Customer Preferred Delivery Time Slot",
                englishAbbreviation = "CUST_PREF_DLV_TM_SLOT",
                businessDefinition = "고객이 선호하는 배송 시간대",
                usageContext = "배송 요청과 배송 옵션 추천에서 사용",
                physicalType = "VARCHAR",
                digits = 20,
                decimalPoint = 0,
                owner = "고객도메인 데이터스튜어드",
                status = TermStatus.Draft,
            ),
        )

        val issues = ruleEngine.validateExpression(
            RuleValidationRequest(
                source = "CODE",
                inputExpression = "고객선호배송시간대",
                expressionType = ExpressionType.Korean,
            ),
        )

        assertThat(issues).hasSize(1)
        assertThat(issues[0].severity).isEqualTo(ValidationSeverity.ERROR)
        assertThat(issues[0].reason).contains("Draft 용어")
    }

    @Test
    fun `physical specification mismatch returns errors`() {
        val issues = ruleEngine.validatePhysicalSpec(
            PhysicalSpecValidationRequest(
                source = "DDL",
                termId = "T-000001",
                physicalType = "NUMBER",
                digits = 30,
                decimalPoint = 2,
            ),
        )

        assertThat(issues).hasSize(3)
        assertThat(issues).allMatch { it.severity == ValidationSeverity.ERROR }
    }
}
