# Spring AI 용어 추천 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 용어 등록 "추천" 시 RAG/DB 검색으로 적당한 용어가 없으면 Spring AI(Anthropic Claude)로 표준 용어 후보를 추론하고, 실패 시 기존 규칙 휴리스틱으로 폴백한다.

**Architecture:** 기존 엔드포인트 `POST /ai/recommend-term-draft` 유지. exact 일치 → 기존 용어 재사용(LLM 미호출). 미일치 → `LlmTermDraftGenerator`(ChatModel) 호출, 예외/키없음/파싱실패 시 기존 catalog 휴리스틱 폴백. semantic 검색 임베딩은 sparse TF-IDF → Spring AI `EmbeddingModel`(transformers 로컬 ONNX) dense 벡터로 교체, `LocalEmbeddingService` 는 폴백 안전망으로 유지.

**Tech Stack:** Kotlin 1.9.25, Spring Boot 3.3.5, Java 21, Gradle KTS, Spring AI 1.0.x (`spring-ai-anthropic-spring-boot-starter`, `spring-ai-transformers-spring-boot-starter`), JUnit5 + kotlin-test, Mockito/mockito-kotlin.

---

## 설계 메모 (구현 전 필독)

- **SimpleVectorStore 미사용 결정**: 현 `SemanticVectorIndex` 는 status/domain 사후 필터 + `adjustedScore` 도메인 가중치를 적용한다. SimpleVectorStore 로 옮기면 이 필터/가중 로직이 복잡해진다. 따라서 커스텀 인덱스 구조는 유지하고 **벡터 표현만 sparse `Map<String,Double>` → dense `FloatArray`** 로 교체한다. (영속 VectorStore 도입은 별도 과제.)
- **임베딩 모델**: Anthropic 은 임베딩 API 없음. `spring-ai-transformers` 가 자동 구성하는 `EmbeddingModel`(기본 all-MiniLM-L6-v2 ONNX) 사용. 외부 키 불필요.
- **LLM 구조화 출력**: 생성 모델(`RecommendedTermDraft`)에 직접 바인딩하지 않고, 플레인 DTO `LlmDraftPayload` 로 JSON 파싱 후 매핑한다(생성 모델 변경 위험 회피).
- **폴백 원칙**: `LlmTermDraftGenerator` 는 키없음/호출실패/파싱실패 시 **예외를 던진다**. 폴백 결정은 `TermRecommendationService` 가 try/catch 로 수행.
- 기존 메서드 `TermRecommendationService.llmInferRecommendation` (`apps/backend/src/main/kotlin/com/aulms/ai/TermRecommendationService.kt:126`) 은 **이름만** `heuristicInferRecommendation` 으로 변경하고 내부 로직(catalog 기반)은 그대로 보존 → 폴백으로 재사용.

## 파일 구조

| 파일 | 책임 | 작업 |
|------|------|------|
| `apps/backend/build.gradle.kts` | 의존성 | 수정 — Spring AI BOM + anthropic + transformers + mockito-kotlin |
| `apps/backend/src/main/resources/application.yml` | 런타임 설정 | 신규 — anthropic 키/모델, 키없음 부팅 허용 |
| `apps/backend/src/main/kotlin/com/aulms/search/DenseEmbeddingService.kt` | Spring AI EmbeddingModel 래퍼 + cosine | 신규 |
| `apps/backend/src/main/kotlin/com/aulms/search/SemanticVectorIndex.kt` | dense 벡터 인덱스 | 수정 — sparse → dense |
| `apps/backend/src/main/kotlin/com/aulms/search/SemanticSearchService.kt` | semantic 검색 | 수정 — dense 임베딩 사용 |
| `apps/backend/src/main/kotlin/com/aulms/ai/LlmDraftPayload.kt` | LLM JSON 파싱 DTO | 신규 |
| `apps/backend/src/main/kotlin/com/aulms/ai/LlmTermDraftGenerator.kt` | ChatModel 호출 + 파싱 | 신규 |
| `apps/backend/src/main/kotlin/com/aulms/ai/TermRecommendationService.kt` | LLM 우선/휴리스틱 폴백 | 수정 |
| `apps/backend/src/test/kotlin/com/aulms/search/DenseEmbeddingServiceTest.kt` | 임베딩 래퍼 단위테스트 | 신규 |
| `apps/backend/src/test/kotlin/com/aulms/ai/LlmTermDraftGeneratorTest.kt` | generator 단위테스트 | 신규 |
| `apps/backend/src/test/kotlin/com/aulms/ai/TermRecommendationServiceTest.kt` | 분기/폴백 테스트 | 신규 |

---

## Task 1: Spring AI 의존성 추가

**Files:**
- Modify: `apps/backend/build.gradle.kts:32-41`

- [ ] **Step 1: Spring AI BOM + 스타터 + mockito-kotlin 추가**

`dependencies { ... }` 블록을 다음으로 교체:

```kotlin
dependencies {
    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.swagger.core.v3:swagger-annotations-jakarta:2.2.25")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.ai:spring-ai-anthropic-spring-boot-starter")
    implementation("org.springframework.ai:spring-ai-transformers-spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}
```

- [ ] **Step 2: 의존성 해석 검증**

Run: `cd apps/backend && ./gradlew dependencies --configuration runtimeClasspath -q 2>&1 | grep -i "spring-ai" | head`
Expected: `spring-ai-anthropic...`, `spring-ai-transformers...` 항목 출력 (해석 성공).

만약 해석 실패(GA 가 mavenCentral 에 없음 등)면: `apps/backend/settings.gradle.kts` 의 `dependencyResolutionManagement.repositories` 에 `maven { url = uri("https://repo.spring.io/milestone") }` 추가 후 재시도. (현재 `mavenCentral()` 만 있음.)

- [ ] **Step 3: 컴파일 통과 확인**

Run: `cd apps/backend && ./gradlew compileKotlin -q`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Commit**

```bash
git add apps/backend/build.gradle.kts apps/backend/settings.gradle.kts
git commit -m "build: add Spring AI anthropic + transformers starters"
```

---

## Task 2: application.yml 신규 (키 없이 부팅)

**Files:**
- Create: `apps/backend/src/main/resources/application.yml`

- [ ] **Step 1: 설정 파일 작성**

```yaml
spring:
  ai:
    anthropic:
      api-key: ${ANTHROPIC_API_KEY:}
      chat:
        options:
          model: claude-sonnet-4-6
          temperature: 0.2
          max-tokens: 1024
    embedding:
      transformer:
        onnx:
          model-output-name: last_hidden_state
```

> 주의: `api-key` 기본값을 빈 문자열로 둬서 키 미설정 시에도 부팅이 가능해야 한다(폴백 경로 보장). 모델명은 배포 환경의 사용 가능 모델로 조정.

- [ ] **Step 2: 키 없이 컨텍스트 로드 검증 테스트 작성**

Create: `apps/backend/src/test/kotlin/com/aulms/config/ApplicationContextLoadTest.kt`

```kotlin
package com.aulms.config

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationContextLoadTest {
    @Test
    fun `context loads without anthropic api key`() {
        // ANTHROPIC_API_KEY 미설정 상태에서 컨텍스트가 정상 기동해야 폴백 경로가 보장됨
    }
}
```

- [ ] **Step 3: 테스트 실행 (키 미설정 환경)**

Run: `cd apps/backend && unset ANTHROPIC_API_KEY && ./gradlew test --tests "com.aulms.config.ApplicationContextLoadTest" -q`
Expected: PASS. (실패 시 `application.yml` 의 `api-key` 기본값/자동구성 점검.)

- [ ] **Step 4: Commit**

```bash
git add apps/backend/src/main/resources/application.yml apps/backend/src/test/kotlin/com/aulms/config/ApplicationContextLoadTest.kt
git commit -m "feat: add application.yml with Spring AI config, boots without api key"
```

---

## Task 3: DenseEmbeddingService (Spring AI EmbeddingModel 래퍼)

**Files:**
- Create: `apps/backend/src/main/kotlin/com/aulms/search/DenseEmbeddingService.kt`
- Test: `apps/backend/src/test/kotlin/com/aulms/search/DenseEmbeddingServiceTest.kt`

- [ ] **Step 1: 실패 테스트 작성 (fake EmbeddingModel)**

```kotlin
package com.aulms.search

import org.junit.jupiter.api.Test
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.embedding.EmbeddingResponse
import org.springframework.ai.embedding.Embedding
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DenseEmbeddingServiceTest {
    private fun modelReturning(vararg vectors: FloatArray): EmbeddingModel =
        object : EmbeddingModel {
            var i = 0
            override fun call(request: org.springframework.ai.embedding.EmbeddingRequest): EmbeddingResponse {
                val out = request.instructions.map { Embedding(vectors[i++], 0) }
                return EmbeddingResponse(out)
            }
            override fun embed(document: org.springframework.ai.document.Document): FloatArray = vectors[i++]
        }

    @Test
    fun `embed delegates to model and returns vector`() {
        val service = DenseEmbeddingService(modelReturning(floatArrayOf(1f, 0f, 0f)))
        val v = service.embed("주문일자")
        assertEquals(listOf(1f, 0f, 0f), v.toList())
    }

    @Test
    fun `cosine of identical vectors is 1`() {
        val service = DenseEmbeddingService(modelReturning())
        val a = floatArrayOf(1f, 2f, 2f)
        assertEquals(1.0, service.cosine(a, a), 1e-9)
    }

    @Test
    fun `cosine of orthogonal vectors is 0`() {
        val service = DenseEmbeddingService(modelReturning())
        assertEquals(0.0, service.cosine(floatArrayOf(1f, 0f), floatArrayOf(0f, 1f)), 1e-9)
    }

    @Test
    fun `cosine handles zero vector without NaN`() {
        val service = DenseEmbeddingService(modelReturning())
        assertTrue(service.cosine(floatArrayOf(0f, 0f), floatArrayOf(1f, 1f)) == 0.0)
    }
}
```

- [ ] **Step 2: 테스트 실패 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.search.DenseEmbeddingServiceTest" -q`
Expected: FAIL — `DenseEmbeddingService` 미정의.

- [ ] **Step 3: 최소 구현**

```kotlin
package com.aulms.search

import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.stereotype.Component
import kotlin.math.sqrt

@Component
class DenseEmbeddingService(private val embeddingModel: EmbeddingModel) {
    fun embed(text: String): FloatArray = embeddingModel.embed(text)

    fun cosine(left: FloatArray, right: FloatArray): Double {
        if (left.isEmpty() || right.isEmpty() || left.size != right.size) return 0.0
        var dot = 0.0
        var leftNorm = 0.0
        var rightNorm = 0.0
        for (i in left.indices) {
            dot += left[i].toDouble() * right[i].toDouble()
            leftNorm += left[i].toDouble() * left[i].toDouble()
            rightNorm += right[i].toDouble() * right[i].toDouble()
        }
        return if (leftNorm == 0.0 || rightNorm == 0.0) 0.0 else dot / (sqrt(leftNorm) * sqrt(rightNorm))
    }
}
```

> `EmbeddingModel.embed(String): FloatArray` 시그니처는 Spring AI 1.0 기준. 버전 차이로 시그니처가 다르면(`List<Double>` 등) 반환 타입을 맞춰 조정.

- [ ] **Step 4: 테스트 통과 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.search.DenseEmbeddingServiceTest" -q`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add apps/backend/src/main/kotlin/com/aulms/search/DenseEmbeddingService.kt apps/backend/src/test/kotlin/com/aulms/search/DenseEmbeddingServiceTest.kt
git commit -m "feat: add DenseEmbeddingService wrapping Spring AI EmbeddingModel"
```

---

## Task 4: semantic 검색을 dense 벡터로 교체

**Files:**
- Modify: `apps/backend/src/main/kotlin/com/aulms/search/SemanticVectorIndex.kt`
- Modify: `apps/backend/src/main/kotlin/com/aulms/search/SemanticSearchService.kt:21-37`
- Test: `apps/backend/src/test/kotlin/com/aulms/search/SemanticSearchApiContractTest.kt` (기존 회귀)

- [ ] **Step 1: SemanticVectorIndex 를 dense 로 변경**

전체 파일을 다음으로 교체:

```kotlin
package com.aulms.search

import com.aulms.term.TermSearchDocument
import org.springframework.stereotype.Component

@Component
class SemanticVectorIndex(private val embeddingService: DenseEmbeddingService) {
    fun build(documents: List<TermSearchDocument>): List<SemanticVectorDocument> =
        documents.map { document ->
            val content = listOf(
                document.term.koreanName,
                document.term.englishName,
                document.term.englishAbbreviation,
                document.term.businessDefinition,
                document.term.usageContext.orEmpty(),
                document.expressions.joinToString(" ") { it.expressionValue },
                document.aliases.joinToString(" ") { "${it.aliasName} ${it.reason}" },
            ).joinToString(" ")
            SemanticVectorDocument(
                document = document,
                content = content,
                embedding = embeddingService.embed(content),
            )
        }
}

data class SemanticVectorDocument(
    val document: TermSearchDocument,
    val content: String,
    val embedding: FloatArray,
)
```

- [ ] **Step 2: SemanticSearchService 에서 dense 임베딩 사용**

`apps/backend/src/main/kotlin/com/aulms/search/SemanticSearchService.kt` 의 생성자 의존성과 `semanticSearch` 본문을 수정:

생성자 `private val embeddingService: LocalEmbeddingService,` → `private val embeddingService: DenseEmbeddingService,`

`semanticSearch` 내부 `val queryEmbedding = embeddingService.embed(request.query)` 는 그대로(타입만 `FloatArray` 로 변경됨). `.map { ... embeddingService.cosine(queryEmbedding, it.embedding) ... }` 호출부도 시그니처 일치로 그대로 동작.

> `adjustedScore`(:39) 의 도메인 가중치 휴리스틱은 **유지**(검색 품질 회귀 방지). dense 점수 분포가 달라 임계값/가중치 조정이 필요하면 회귀 테스트에서 확인 후 미세조정.

- [ ] **Step 3: 회귀 테스트 실행**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.search.SemanticSearchApiContractTest" --tests "com.aulms.search.SearchApiContractTest" -q`
Expected: PASS. 만약 dense 임베딩으로 순위 회귀가 발생하면, 실패 케이스의 기대 순위/점수를 확인하고 `adjustedScore` 가중치를 조정하여 기존 시나리오(주문일자 vs 주문일시 등)를 복구.

- [ ] **Step 4: 전체 컴파일/테스트 확인**

Run: `cd apps/backend && ./gradlew test -q`
Expected: PASS (LLM 키 의존 테스트는 아직 없음).

- [ ] **Step 5: Commit**

```bash
git add apps/backend/src/main/kotlin/com/aulms/search/SemanticVectorIndex.kt apps/backend/src/main/kotlin/com/aulms/search/SemanticSearchService.kt
git commit -m "feat: replace sparse TF-IDF with Spring AI dense embeddings in semantic search"
```

---

## Task 5: LlmTermDraftGenerator (ChatModel 호출 + JSON 파싱)

**Files:**
- Create: `apps/backend/src/main/kotlin/com/aulms/ai/LlmDraftPayload.kt`
- Create: `apps/backend/src/main/kotlin/com/aulms/ai/LlmTermDraftGenerator.kt`
- Test: `apps/backend/src/test/kotlin/com/aulms/ai/LlmTermDraftGeneratorTest.kt`

- [ ] **Step 1: 파싱 DTO 작성**

`apps/backend/src/main/kotlin/com/aulms/ai/LlmDraftPayload.kt`:

```kotlin
package com.aulms.ai

data class LlmDraftPayload(
    val domainName: String,
    val usageType: String,
    val englishName: String,
    val englishAbbreviation: String,
    val businessDefinition: String,
    val usageContext: String,
    val physicalType: String,
    val digits: Int,
    val decimalPoint: Int,
    val owner: String,
)
```

- [ ] **Step 2: 실패 테스트 작성 (ChatModel mock)**

`apps/backend/src/test/kotlin/com/aulms/ai/LlmTermDraftGeneratorTest.kt`:

```kotlin
package com.aulms.ai

import com.aulms.model.GraphRecommendationContext
import com.aulms.model.RecommendedTermDraft
import com.aulms.model.TermRecommendationRequest
import com.aulms.model.TermRecommendationMode
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.model.Generation
import org.springframework.ai.chat.messages.AssistantMessage
import kotlin.test.assertEquals

class LlmTermDraftGeneratorTest {
    private val request = TermRecommendationRequest(
        koreanName = "주문취소사유코드",
        mode = TermRecommendationMode.TermCreate,
        currentDomainName = "주문",
    )
    private val graphContext = GraphRecommendationContext(
        inferredDomainName = "주문",
        relatedTerms = listOf("주문상태코드"),
        relationshipHints = listOf("주문 도메인 코드 체계와 함께 검토"),
    )

    private fun modelReturning(text: String): ChatModel = mock {
        on { call(any<org.springframework.ai.chat.prompt.Prompt>()) } doReturn
            ChatResponse(listOf(Generation(AssistantMessage(text))))
    }

    private fun generator(model: ChatModel, key: String = "test-key") =
        LlmTermDraftGenerator(model, ObjectMapper(), key)

    @Test
    fun `parses valid json into RecommendedTermDraft`() {
        val json = """
            {"domainName":"주문","usageType":"표준항목","englishName":"Order Cancel Reason Code",
             "englishAbbreviation":"ORD_CNCL_RSN_CD","businessDefinition":"주문 취소 사유를 코드로 관리",
             "usageContext":"주문 취소 화면/API","physicalType":"VARCHAR","digits":10,"decimalPoint":0,
             "owner":"주문도메인 데이터스튜어드"}
        """.trimIndent()
        val draft: RecommendedTermDraft = generator(modelReturning(json)).generate(request, "주문", graphContext)
        assertEquals("Order Cancel Reason Code", draft.englishName)
        assertEquals("ORD_CNCL_RSN_CD", draft.englishAbbreviation)
        assertEquals(10, draft.digits)
    }

    @Test
    fun `throws when api key blank`() {
        assertThrows<LlmUnavailableException> {
            generator(modelReturning("{}"), key = "").generate(request, "주문", graphContext)
        }
    }

    @Test
    fun `throws when model returns non-json`() {
        assertThrows<LlmUnavailableException> {
            generator(modelReturning("죄송합니다 모르겠어요")).generate(request, "주문", graphContext)
        }
    }

    @Test
    fun `throws when model call fails`() {
        val failing: ChatModel = mock {
            on { call(any<org.springframework.ai.chat.prompt.Prompt>()) } doThrow RuntimeException("upstream 500")
        }
        assertThrows<LlmUnavailableException> {
            generator(failing).generate(request, "주문", graphContext)
        }
    }
}
```

> `mockito-kotlin` 의 `doReturn`/`doThrow` 중위 함수 import: `import org.mockito.kotlin.doReturn`, `import org.mockito.kotlin.doThrow`. 필요 시 추가.

- [ ] **Step 3: 테스트 실패 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.ai.LlmTermDraftGeneratorTest" -q`
Expected: FAIL — `LlmTermDraftGenerator`, `LlmUnavailableException` 미정의.

- [ ] **Step 4: 구현 작성**

`apps/backend/src/main/kotlin/com/aulms/ai/LlmTermDraftGenerator.kt`:

```kotlin
package com.aulms.ai

import com.aulms.model.GraphRecommendationContext
import com.aulms.model.RecommendedTermDraft
import com.aulms.model.TermRecommendationRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

class LlmUnavailableException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

@Component
class LlmTermDraftGenerator(
    private val chatModel: ChatModel,
    private val objectMapper: ObjectMapper,
    @Value("\${spring.ai.anthropic.api-key:}") private val apiKey: String,
) {
    fun generate(
        request: TermRecommendationRequest,
        inferredDomainName: String,
        graphContext: GraphRecommendationContext,
    ): RecommendedTermDraft {
        if (apiKey.isBlank()) {
            throw LlmUnavailableException("Anthropic api key 미설정")
        }
        val content = try {
            val response = chatModel.call(Prompt(buildPrompt(request, inferredDomainName, graphContext)))
            response.result?.output?.text ?: throw LlmUnavailableException("LLM 응답 본문 없음")
        } catch (ex: LlmUnavailableException) {
            throw ex
        } catch (ex: Exception) {
            throw LlmUnavailableException("LLM 호출 실패: ${ex.message}", ex)
        }
        val json = extractJson(content) ?: throw LlmUnavailableException("LLM 응답에서 JSON 추출 실패")
        val payload = try {
            objectMapper.readValue(json, LlmDraftPayload::class.java)
        } catch (ex: Exception) {
            throw LlmUnavailableException("LLM JSON 파싱 실패: ${ex.message}", ex)
        }
        return payload.toDraft()
    }

    private fun extractJson(text: String): String? {
        val start = text.indexOf('{')
        val end = text.lastIndexOf('}')
        return if (start >= 0 && end > start) text.substring(start, end + 1) else null
    }

    private fun buildPrompt(
        request: TermRecommendationRequest,
        inferredDomainName: String,
        graphContext: GraphRecommendationContext,
    ): String = """
        너는 데이터 표준 용어 사전 전문가다. 아래 한글 용어명에 대한 표준 용어 후보를 추론한다.
        한글명: ${request.koreanName}
        추정 도메인: $inferredDomainName
        관련 용어: ${graphContext.relatedTerms.joinToString(", ").ifBlank { "없음" }}
        관계 힌트: ${graphContext.relationshipHints.joinToString("; ").ifBlank { "없음" }}
        기존 업무정의(있으면 우선): ${request.currentBusinessDefinition ?: "없음"}
        기존 사용맥락(있으면 우선): ${request.currentUsageContext ?: "없음"}

        다음 JSON 스키마로만 답하라. 코드블록/설명 없이 순수 JSON 객체만 출력한다.
        {
          "domainName": string, "usageType": string,
          "englishName": string, "englishAbbreviation": string(대문자_언더스코어),
          "businessDefinition": string, "usageContext": string,
          "physicalType": string(VARCHAR|CHAR|NUMERIC|DATE|TIMESTAMP 등),
          "digits": number, "decimalPoint": number, "owner": string
        }
    """.trimIndent()

    private fun LlmDraftPayload.toDraft(): RecommendedTermDraft = RecommendedTermDraft(
        domainName = domainName,
        usageType = usageType,
        englishName = englishName,
        englishAbbreviation = englishAbbreviation,
        businessDefinition = businessDefinition,
        usageContext = usageContext,
        physicalType = physicalType,
        digits = digits,
        decimalPoint = decimalPoint,
        owner = owner,
    )
}
```

> `RecommendedTermDraft` 생성자 인자 순서/타입은 `apps/backend/src/main/kotlin/com/aulms/ai/TermRecommendationService.kt:160-171` 사용처와 일치. 생성 모델의 실제 필드 nullability 가 다르면 컴파일 에러를 보고 맞춰 조정. `response.result.output.text` 접근자가 Spring AI 버전에 따라 `content` 일 수 있음 — 컴파일 에러 시 해당 접근자로 교체.

- [ ] **Step 5: 테스트 통과 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.ai.LlmTermDraftGeneratorTest" -q`
Expected: PASS.

- [ ] **Step 6: Commit**

```bash
git add apps/backend/src/main/kotlin/com/aulms/ai/LlmDraftPayload.kt apps/backend/src/main/kotlin/com/aulms/ai/LlmTermDraftGenerator.kt apps/backend/src/test/kotlin/com/aulms/ai/LlmTermDraftGeneratorTest.kt
git commit -m "feat: add LlmTermDraftGenerator with structured JSON parsing and failure handling"
```

---

## Task 6: TermRecommendationService 통합 (LLM 우선 / 휴리스틱 폴백)

**Files:**
- Modify: `apps/backend/src/main/kotlin/com/aulms/ai/TermRecommendationService.kt`
- Test: `apps/backend/src/test/kotlin/com/aulms/ai/TermRecommendationServiceTest.kt`

- [ ] **Step 1: 생성자에 generator 주입 + 메서드 리네임**

`TermRecommendationService` 생성자(`:20-25`)에 `private val llmTermDraftGenerator: LlmTermDraftGenerator,` 추가.

기존 `private fun llmInferRecommendation(` (`:126`) 의 **이름만** `private fun heuristicInferRecommendation(` 로 변경(본문/시그니처 동일). 이 메서드가 폴백 구현이 된다.

- [ ] **Step 2: recommend() 본문에 LLM-우선 분기 추가**

`recommend()` (`:26`) 내 `val recommendation = llmInferRecommendation(...)` (`:49-56`) 호출부를 다음으로 교체:

```kotlin
        val recommendation: RecommendedTermDraft
        val usedLlm: Boolean
        if (existingExact != null) {
            recommendation = heuristicInferRecommendation(
                request = request,
                normalizedKoreanName = normalizedKoreanName,
                inferredDomainName = inferredDomainName,
                ragMatches = ragMatches,
                graphContext = graphContext,
                existingExact = existingExact,
            )
            usedLlm = false
        } else {
            val llmResult = try {
                llmTermDraftGenerator.generate(request, inferredDomainName, graphContext)
            } catch (ex: LlmUnavailableException) {
                null
            }
            if (llmResult != null) {
                recommendation = llmResult
                usedLlm = true
            } else {
                recommendation = heuristicInferRecommendation(
                    request = request,
                    normalizedKoreanName = normalizedKoreanName,
                    inferredDomainName = inferredDomainName,
                    ragMatches = ragMatches,
                    graphContext = graphContext,
                    existingExact = existingExact,
                )
                usedLlm = false
            }
        }
```

`TermRecommendationResponse(...)` 의 `warnings = warnings(...)` 인자를 다음으로 교체:

```kotlin
            warnings = warnings(normalizedKoreanName, ragMatches, existingExact != null, usedLlm),
```

- [ ] **Step 3: warnings() 에 usedLlm 반영**

`private fun warnings(` (`:246`) 시그니처에 `usedLlm: Boolean` 파라미터 추가하고, 본문 첫 부분에:

```kotlin
        if (!usedLlm && existingExact == null) {
            items += "LLM 미사용(키 미설정 또는 호출 실패) 규칙기반 추천값이므로 검토가 필요함"
        }
```

(기존 if/else 로직은 그대로 유지, 위 한 줄만 추가.)

- [ ] **Step 4: 실패 테스트 작성**

`apps/backend/src/test/kotlin/com/aulms/ai/TermRecommendationServiceTest.kt`:

```kotlin
package com.aulms.ai

import com.aulms.model.RecommendedTermDraft
import com.aulms.model.TermRecommendationMode
import com.aulms.model.TermRecommendationRequest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
class TermRecommendationServiceTest {
    @Autowired lateinit var service: TermRecommendationService
    @MockBean lateinit var generator: LlmTermDraftGenerator

    private fun req(name: String) = TermRecommendationRequest(
        koreanName = name,
        mode = TermRecommendationMode.TermCreate,
        currentDomainName = "주문",
    )

    @Test
    fun `uses llm result when generator succeeds`() {
        val llmDraft = RecommendedTermDraft(
            domainName = "주문", usageType = "표준항목",
            englishName = "Order Magic Field", englishAbbreviation = "ORD_MAGIC",
            businessDefinition = "정의", usageContext = "맥락",
            physicalType = "VARCHAR", digits = 10, decimalPoint = 0, owner = "owner",
        )
        whenever(generator.generate(any(), any(), any())).thenReturn(llmDraft)
        val response = service.recommend(req("주문임의신규항목"))
        assertEquals("Order Magic Field", response.recommendation.englishName)
    }

    @Test
    fun `falls back to heuristic when generator throws`() {
        whenever(generator.generate(any(), any(), any())).thenThrow(LlmUnavailableException("no key"))
        val response = service.recommend(req("주문임의신규항목"))
        // 휴리스틱은 도메인 약어 prefix(ORD_) 를 생성
        assertTrue(response.recommendation.englishAbbreviation.startsWith("ORD"))
        assertTrue(response.warnings.any { it.contains("LLM 미사용") })
    }

    @Test
    fun `skips llm and reuses existing term on exact match`() {
        // 기존 사전에 존재하는 Approved 용어명을 사용 (fixture 의 실제 표준 용어명으로 교체할 것)
        val response = service.recommend(req("주문일자"))
        // exact 재사용 경로는 generator 를 호출하지 않음
        org.mockito.Mockito.verify(generator, org.mockito.Mockito.never()).generate(any(), any(), any())
        assertEquals("주문", response.recommendation.domainName)
    }
}
```

> `verify never` 테스트의 `"주문일자"` 는 시드 데이터에 실제 존재하는 Approved 표준 용어명이어야 한다. `TermRepository` 시드(`apps/backend/src/main/kotlin/com/aulms/term/TermRepository.kt`)를 확인해 정확한 용어명으로 교체.

- [ ] **Step 5: 테스트 실패 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.ai.TermRecommendationServiceTest" -q`
Expected: FAIL (통합 전이면 컴파일/단언 실패).

- [ ] **Step 6: 통과까지 구현 보정 후 재실행**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.ai.TermRecommendationServiceTest" -q`
Expected: PASS. 실패 시 exact-match 용어명/약어 prefix 기대값을 시드 데이터에 맞게 조정.

- [ ] **Step 7: 전체 테스트**

Run: `cd apps/backend && ./gradlew test -q`
Expected: PASS.

- [ ] **Step 8: Commit**

```bash
git add apps/backend/src/main/kotlin/com/aulms/ai/TermRecommendationService.kt apps/backend/src/test/kotlin/com/aulms/ai/TermRecommendationServiceTest.kt
git commit -m "feat: LLM-first term recommendation with heuristic fallback"
```

---

## Task 7: 수동 검증 (프론트 엔드투엔드)

**Files:** 없음 (검증만, 프론트 코드 변경 없음)

- [ ] **Step 1: 키 없이 백엔드 기동 → 폴백 확인**

Run: `cd apps/backend && unset ANTHROPIC_API_KEY && ./gradlew bootRun` (별도 터미널)
프론트(`apps/frontend`)에서 "용어 등록" → 한글명 입력 → "추천" 클릭.
Expected: 추천값 채워짐 + 응답 `warnings` 에 "LLM 미사용" 포함(개발자도구 네트워크 탭으로 `/ai/recommend-term-draft` 응답 확인).

- [ ] **Step 2: 키 설정 후 LLM 경로 확인**

Run: `cd apps/backend && export ANTHROPIC_API_KEY=<유효키> && ./gradlew bootRun`
프론트에서 사전에 없는 신규 한글명(예: "주문취소사유코드")으로 "추천" 클릭.
Expected: LLM 추론값으로 폼 채워짐, `warnings` 에 "LLM 미사용" 없음.

- [ ] **Step 3: exact 일치 재사용 확인**

기존 Approved 용어명으로 "추천" 클릭.
Expected: 기존 승인 용어 그대로 채워짐(LLM 호출 안 함).

- [ ] **Step 4: 검증 결과 기록 후 마무리**

verification-before-completion 스킬로 위 3개 경로 실제 동작을 증거(응답 캡처)와 함께 확인 후 완료 선언.

---

## Self-Review 결과

- **Spec 커버리지**: 한글명 입력+추천(Task7) / RAG·DB 검색(기존 유지, Task4 임베딩 교체) / 적당한 용어 없을 때 Spring AI(Task5-6) / 화면 표시(Task7, 스키마 불변) — 모두 태스크 존재. ✅
- **폴백 결정(휴리스틱 유지)**: Task6 try/catch + warnings. ✅
- **임베딩 교체 결정**: Task3-4. ✅
- **Placeholder 없음**: 모든 코드 스텝에 실제 코드 포함. ✅
- **타입 일관성**: `DenseEmbeddingService.embed: FloatArray`, `SemanticVectorDocument.embedding: FloatArray`, `LlmTermDraftGenerator.generate(request, inferredDomainName, graphContext): RecommendedTermDraft`, `LlmUnavailableException` 일관 사용. ✅
- **미해결(실행 중 확인 필요)**: Spring AI 정확 버전(1.0.x)·`EmbeddingModel.embed`/`ChatResponse.output.text` 접근자 시그니처, exact-match 테스트용 실제 시드 용어명. 각 태스크 노트에 명시.
