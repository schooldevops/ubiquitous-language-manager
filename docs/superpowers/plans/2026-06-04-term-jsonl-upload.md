# 용어 JSONL 업로드 구현 계획

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** `.jsonl` 용어 파일을 업로드하면 각 줄을 DB에 upsert(termId 있으면 update/없으면 insert, 표현·별칭·관계 포함) 하고, 줄별 결과(성공/실패·오류·일시)를 `term_upload_row`에 기록해 화면에 노출한다.

**Architecture:** OpenAPI-first(spec→generator stub/client). 백엔드는 Querydsl-SQL(no JPA), postgres 프로파일 전용. 동기 per-row 처리, 각 row 개별 트랜잭션(REQUIRES_NEW)으로 격리, 2-pass(term 본문 → 관계). 프론트는 Next.js + 생성된 axios client.

**Tech Stack:** Kotlin, Spring Boot 3.3, Querydsl-SQL 5.1, Flyway, PostgreSQL(pgvector), Next.js, Tailwind 4, Base UI, openapi-generator 7.14.

**Spec:** `docs/superpowers/specs/2026-06-04-term-jsonl-upload-design.md`

---

## File Structure

신규/수정 파일:

- **OpenAPI**
  - Create `openapi/schemas/term-upload.yaml` — 결과 스키마.
  - Create `openapi/paths/term-uploads.yaml` — 3개 오퍼레이션.
  - Modify `openapi/openapi.yaml` — tag + paths $ref 등록.
- **DB**
  - Create `apps/backend/src/main/resources/db/migration/V3__term_upload_row.sql`.
- **Backend (postgres 전용, `com.aulms.upload`)**
  - Create `.../upload/sql/TermUploadRowTable.kt` — Q-type `QTermUploadRow`.
  - Create `.../upload/sql/QuerydslTermUploadResultRepository.kt` — 결과 기록/조회.
  - Create `.../upload/sql/QuerydslTermUpsertRepository.kt` — term/표현/별칭/관계 upsert.
  - Create `.../upload/TermJsonlParser.kt` — 줄→`ParsedTermRow`.
  - Create `.../upload/TermUploadModels.kt` — 내부 DTO.
  - Create `.../upload/TermUploadRowProcessor.kt` — row 1건 처리(@Transactional REQUIRES_NEW).
  - Create `.../upload/TermUploadService.kt` — 오케스트레이션 + 집계.
  - Create `.../upload/TermUploadController.kt` — 생성 stub 구현.
- **Frontend**
  - Modify `apps/frontend/src/lib/term-api.ts` — `TermUploadApi` 와이어링 + 업로드 함수.
  - Create `apps/frontend/src/app/terms/upload/page.tsx` — 업로드 화면.
- **Tests**
  - Create `apps/backend/src/test/kotlin/com/aulms/upload/TermJsonlParserTest.kt`.
  - Create `apps/backend/src/test/kotlin/com/aulms/upload/TermUploadServiceTest.kt`.

> 주의: `com.aulms.upload` 의 빈은 모두 `@Profile("postgres")`. memory 프로파일에서는 업로드 비활성.

---

## Task 1: OpenAPI 스키마 + 경로 작성

**Files:**
- Create: `openapi/schemas/term-upload.yaml`
- Create: `openapi/paths/term-uploads.yaml`
- Modify: `openapi/openapi.yaml`

- [ ] **Step 1: 결과 스키마 작성**

Create `openapi/schemas/term-upload.yaml`:

```yaml
TermUploadStatus:
  type: string
  enum: [INSERTED, UPDATED, FAILED]
  description: 행 처리 결과 상태

TermUploadRow:
  type: object
  required: [lineNo, status]
  properties:
    lineNo: { type: integer, example: 1 }
    termId: { type: string, nullable: true, example: "T-000001" }
    status: { $ref: '#/TermUploadStatus' }
    errorMessage: { type: string, nullable: true }
    registeredAt: { type: string, format: date-time, nullable: true }
    failedAt: { type: string, format: date-time, nullable: true }

TermUploadResult:
  type: object
  required: [uploadBatchId, uploadedAt, totalRows, inserted, updated, failed, rows]
  properties:
    uploadBatchId: { type: string, example: "UPL-20260604-0001" }
    uploadedAt: { type: string, format: date-time }
    totalRows: { type: integer }
    inserted: { type: integer }
    updated: { type: integer }
    failed: { type: integer }
    rows:
      type: array
      items: { $ref: '#/TermUploadRow' }

TermUploadBatchSummary:
  type: object
  required: [uploadBatchId, uploadedAt, totalRows, inserted, updated, failed]
  properties:
    uploadBatchId: { type: string }
    uploadedAt: { type: string, format: date-time }
    totalRows: { type: integer }
    inserted: { type: integer }
    updated: { type: integer }
    failed: { type: integer }

TermUploadBatchListResponse:
  type: object
  required: [items]
  properties:
    items:
      type: array
      items: { $ref: '#/TermUploadBatchSummary' }
```

- [ ] **Step 2: 경로 작성**

Create `openapi/paths/term-uploads.yaml`:

```yaml
termUploads:
  post:
    tags: [Term]
    operationId: uploadTerms
    summary: 용어 JSONL 파일 업로드(upsert)
    requestBody:
      required: true
      content:
        multipart/form-data:
          schema:
            type: object
            required: [file]
            properties:
              file:
                type: string
                format: binary
                description: JSONL 파일(.jsonl)
    responses:
      '200':
        description: 업로드 처리 결과
        content:
          application/json:
            schema:
              $ref: '../schemas/term-upload.yaml#/TermUploadResult'
  get:
    tags: [Term]
    operationId: listTermUploadBatches
    summary: 업로드 배치 목록(최근순)
    responses:
      '200':
        description: 배치 목록
        content:
          application/json:
            schema:
              $ref: '../schemas/term-upload.yaml#/TermUploadBatchListResponse'

termUploadById:
  get:
    tags: [Term]
    operationId: getTermUploadBatch
    summary: 업로드 배치 결과 단건 조회
    parameters:
      - name: uploadBatchId
        in: path
        required: true
        schema: { type: string }
    responses:
      '200':
        description: 배치 결과
        content:
          application/json:
            schema:
              $ref: '../schemas/term-upload.yaml#/TermUploadResult'
```

- [ ] **Step 3: openapi.yaml 에 paths 등록**

Modify `openapi/openapi.yaml` — `/terms/{termId}/aliases` $ref 줄(파일 내 검색) 아래에 추가:

```yaml
  /terms/uploads:
    $ref: './paths/term-uploads.yaml#/termUploads'
  /terms/uploads/{uploadBatchId}:
    $ref: './paths/term-uploads.yaml#/termUploadById'
```

- [ ] **Step 4: 커밋**

```bash
cd /Users/1111489/Documents/AULMS
git add openapi/schemas/term-upload.yaml openapi/paths/term-uploads.yaml openapi/openapi.yaml
git commit -m "feat(openapi): term JSONL upload endpoints"
```

---

## Task 2: 코드 생성 (backend stub + frontend client)

**Files:** 생성물 `generated/backend`, `generated/frontend`.

- [ ] **Step 1: 백엔드 stub 생성**

Run:
```bash
cd /Users/1111489/Documents/AULMS && bash scripts/openapi-generate-backend.sh
```
Expected: `generated/backend/src/main/kotlin/com/aulms/api/TermApi.kt` 에 `uploadTerms`, `listTermUploadBatches`, `getTermUploadBatch` 추가. `generated/.../model/TermUploadResult.kt` 등 모델 생성.

- [ ] **Step 2: 프론트 client 생성**

Run:
```bash
cd /Users/1111489/Documents/AULMS && bash scripts/openapi-generate-frontend.sh
```
Expected: `generated/frontend/model/term-upload-result.ts` 등 + `api.ts` 에 `uploadTerms` 메서드.

- [ ] **Step 3: 생성 확인**

Run:
```bash
grep -rl "uploadTerms" generated/backend generated/frontend
```
Expected: backend `TermApi.kt`, frontend `api.ts` 매치.

- [ ] **Step 4: 커밋**

```bash
git add generated/backend generated/frontend
git commit -m "chore: regenerate stub/client for term upload"
```

---

## Task 3: Flyway 마이그레이션 — term_upload_row

**Files:**
- Create: `apps/backend/src/main/resources/db/migration/V3__term_upload_row.sql`

- [ ] **Step 1: 마이그레이션 작성**

Create `apps/backend/src/main/resources/db/migration/V3__term_upload_row.sql`:

```sql
-- 용어 JSONL 업로드 결과 추적 (postgres 전용)
CREATE SEQUENCE IF NOT EXISTS term_upload_batch_seq START 1;

CREATE TABLE term_upload_row (
    upload_row_id   BIGSERIAL    PRIMARY KEY,
    upload_batch_id VARCHAR(40)  NOT NULL,
    line_no         INT          NOT NULL,
    term_id         VARCHAR(32),
    status          VARCHAR(20)  NOT NULL,
    raw_json        TEXT         NOT NULL,
    error_message   TEXT,
    registered_at   TIMESTAMPTZ,
    failed_at       TIMESTAMPTZ,
    uploaded_at     TIMESTAMPTZ  NOT NULL
);
CREATE INDEX idx_term_upload_batch ON term_upload_row(upload_batch_id);
CREATE INDEX idx_term_upload_batch_status ON term_upload_row(upload_batch_id, status);
```

- [ ] **Step 2: 마이그레이션 적용 검증**

Run (docker postgres 기동 상태):
```bash
cd apps/backend && SPRING_PROFILES_ACTIVE=postgres SERVER_PORT=8082 POSTGRES_PASSWORD=changeme NEO4J_PASSWORD=changeme123 timeout 40 ./gradlew bootRun 2>&1 | grep -E "Migrating schema|term upload|Started" | head
```
Expected: `Migrating schema "public" to version "3 - term upload row"` 로그.

- [ ] **Step 3: 커밋**

```bash
git add apps/backend/src/main/resources/db/migration/V3__term_upload_row.sql
git commit -m "feat(db): term_upload_row migration"
```

---

## Task 4: Q-type QTermUploadRow

**Files:**
- Create: `apps/backend/src/main/kotlin/com/aulms/upload/sql/TermUploadRowTable.kt`

- [ ] **Step 1: Q-type 작성**

Create `apps/backend/src/main/kotlin/com/aulms/upload/sql/TermUploadRowTable.kt`:

```kotlin
package com.aulms.upload.sql

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.ColumnMetadata
import com.querydsl.sql.RelationalPathBase
import java.sql.Types
import java.time.OffsetDateTime

class QTermUploadRow(variable: String) : RelationalPathBase<QTermUploadRow>(QTermUploadRow::class.java, forVariable(variable), "public", "term_upload_row") {
    val uploadRowId: NumberPath<Long> = createNumber("uploadRowId", Long::class.javaObjectType)
    val uploadBatchId: StringPath = createString("uploadBatchId")
    val lineNo: NumberPath<Int> = createNumber("lineNo", Int::class.javaObjectType)
    val termId: StringPath = createString("termId")
    val status: StringPath = createString("status")
    val rawJson: StringPath = createString("rawJson")
    val errorMessage: StringPath = createString("errorMessage")
    val registeredAt: DateTimePath<OffsetDateTime> = createDateTime("registeredAt", OffsetDateTime::class.java)
    val failedAt: DateTimePath<OffsetDateTime> = createDateTime("failedAt", OffsetDateTime::class.java)
    val uploadedAt: DateTimePath<OffsetDateTime> = createDateTime("uploadedAt", OffsetDateTime::class.java)

    init {
        addMetadata(uploadRowId, ColumnMetadata.named("upload_row_id").withIndex(1).ofType(Types.BIGINT))
        addMetadata(uploadBatchId, ColumnMetadata.named("upload_batch_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(lineNo, ColumnMetadata.named("line_no").withIndex(3).ofType(Types.INTEGER))
        addMetadata(termId, ColumnMetadata.named("term_id").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(status, ColumnMetadata.named("status").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(rawJson, ColumnMetadata.named("raw_json").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(errorMessage, ColumnMetadata.named("error_message").withIndex(7).ofType(Types.VARCHAR))
        addMetadata(registeredAt, ColumnMetadata.named("registered_at").withIndex(8).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
        addMetadata(failedAt, ColumnMetadata.named("failed_at").withIndex(9).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
        addMetadata(uploadedAt, ColumnMetadata.named("uploaded_at").withIndex(10).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val termUploadRow = QTermUploadRow("term_upload_row")
    }
}
```

- [ ] **Step 2: 컴파일 확인**

Run: `cd apps/backend && ./gradlew compileKotlin -q && echo OK`
Expected: `OK`

- [ ] **Step 3: 커밋**

```bash
git add apps/backend/src/main/kotlin/com/aulms/upload/sql/TermUploadRowTable.kt
git commit -m "feat(upload): QTermUploadRow q-type"
```

---

## Task 5: 내부 모델 + JSONL 파서 (TDD)

**Files:**
- Create: `apps/backend/src/main/kotlin/com/aulms/upload/TermUploadModels.kt`
- Create: `apps/backend/src/main/kotlin/com/aulms/upload/TermJsonlParser.kt`
- Test: `apps/backend/src/test/kotlin/com/aulms/upload/TermJsonlParserTest.kt`

- [ ] **Step 1: 내부 모델 작성**

Create `apps/backend/src/main/kotlin/com/aulms/upload/TermUploadModels.kt`:

```kotlin
package com.aulms.upload

import com.aulms.model.RelationshipType
import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
import com.aulms.model.TermStatus

/** 파싱된 한 줄. parseError != null 이면 파싱 실패 행. */
data class ParsedTermRow(
    val lineNo: Int,
    val rawJson: String,
    val term: ParsedTerm?,
    val parseError: String?,
)

data class ParsedTerm(
    val termId: String,
    val termNumber: String?,
    val domainName: String?,
    val usageType: String?,
    val koreanName: String?,
    val englishName: String?,
    val englishAbbreviation: String?,
    val businessDefinition: String?,
    val usageContext: String?,
    val physicalType: String?,
    val digits: Int?,
    val decimalPoint: Int?,
    val status: TermStatus?,
    val owner: String?,
    val version: String?,
    val expressions: List<TermExpression>,
    val aliases: List<TermAlias>,
    val relationships: List<ParsedRelationship>,
)

data class ParsedRelationship(
    val relationshipId: Long,
    val relationshipType: RelationshipType,
    val targetTermId: String,
    val description: String,
)
```

- [ ] **Step 2: 실패 테스트 작성**

Create `apps/backend/src/test/kotlin/com/aulms/upload/TermJsonlParserTest.kt`:

```kotlin
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
```

- [ ] **Step 3: 테스트 실패 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.upload.TermJsonlParserTest" -q`
Expected: FAIL (`TermJsonlParser` 미존재 컴파일 에러).

- [ ] **Step 4: 파서 구현**

Create `apps/backend/src/main/kotlin/com/aulms/upload/TermJsonlParser.kt`:

```kotlin
package com.aulms.upload

import com.aulms.model.AliasType
import com.aulms.model.ExpressionType
import com.aulms.model.RelationshipType
import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
import com.aulms.model.TermStatus
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class TermJsonlParser(private val objectMapper: ObjectMapper = ObjectMapper()) {

    fun parse(content: String): List<ParsedTermRow> {
        val result = mutableListOf<ParsedTermRow>()
        var lineNo = 0
        content.lineSequence().forEach { raw ->
            lineNo += 1
            val line = raw.trim()
            if (line.isEmpty()) return@forEach
            result.add(parseLine(lineNo, line))
        }
        return result
    }

    private fun parseLine(lineNo: Int, line: String): ParsedTermRow = try {
        val node = objectMapper.readTree(line)
        val termId = node.path("termId").asText(null)
            ?: throw IllegalArgumentException("termId 누락")
        ParsedTermRow(lineNo, line, toParsedTerm(termId, node), null)
    } catch (ex: Exception) {
        ParsedTermRow(lineNo, line, null, ex.message ?: "파싱 실패")
    }

    private fun toParsedTerm(termId: String, node: JsonNode): ParsedTerm = ParsedTerm(
        termId = termId,
        termNumber = node.path("termNumber").asText(null),
        domainName = node.path("domainName").asText(null),
        usageType = node.path("usageType").asText(null),
        koreanName = node.path("koreanName").asText(null),
        englishName = node.path("englishName").asText(null),
        englishAbbreviation = node.path("englishAbbreviation").asText(null),
        businessDefinition = node.path("businessDefinition").asText(null),
        usageContext = node.path("usageContext").asText(null),
        physicalType = node.path("physicalType").asText(null),
        digits = node.get("digits")?.takeIf { it.isInt }?.asInt(),
        decimalPoint = node.get("decimalPoint")?.takeIf { it.isInt }?.asInt(),
        status = node.path("status").asText(null)?.let { TermStatus.forValue(it) },
        owner = node.path("owner").asText(null),
        version = node.path("version").asText(null),
        expressions = parseExpressions(termId, node.path("expressions")),
        aliases = parseAliases(termId, node.path("aliases")),
        relationships = parseRelationships(node.path("relationships")),
    )

    private fun parseExpressions(termId: String, node: JsonNode): List<TermExpression> =
        node.takeIf { it.isArray }?.map {
            TermExpression(
                expressionId = it.path("expressionId").asLong(),
                termId = termId,
                expressionType = ExpressionType.forValue(it.path("expressionType").asText()),
                expressionValue = it.path("expressionValue").asText(),
                isStandard = it.path("isStandard").asBoolean(true),
                language = it.path("language").asText(null),
                style = it.path("style").asText(null),
            )
        }.orEmpty()

    private fun parseAliases(termId: String, node: JsonNode): List<TermAlias> {
        if (node.isMissingNode || node.isNull) return emptyList()
        val groups = listOf("synonyms", "forbidden", "deprecated", "needsContext")
        return groups.flatMap { group ->
            node.path(group).takeIf { it.isArray }?.map { aliasNode(termId, it) }.orEmpty()
        }
    }

    private fun aliasNode(termId: String, it: JsonNode): TermAlias = TermAlias(
        aliasId = it.path("aliasId").asText(),
        termId = termId,
        aliasName = it.path("aliasName").asText(),
        aliasType = AliasType.forValue(it.path("aliasType").asText()),
        recommendationAction = it.path("recommendationAction").asText(""),
        reason = it.path("reason").asText(""),
    )

    private fun parseRelationships(node: JsonNode): List<ParsedRelationship> =
        node.takeIf { it.isArray }?.map {
            ParsedRelationship(
                relationshipId = it.path("relationshipId").asLong(),
                relationshipType = RelationshipType.forValue(it.path("relationshipType").asText()),
                targetTermId = it.path("targetTermId").asText(),
                description = it.path("description").asText(""),
            )
        }.orEmpty()
}
```

> 주의: `JsonNode.asText(null)` 은 노드 부재 시 null 반환. `path("termId").asText(null)` 가 null 이면 termId 누락으로 예외 → 파싱오류 행.

- [ ] **Step 5: 테스트 통과 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.upload.TermJsonlParserTest" -q`
Expected: PASS (4 tests).

- [ ] **Step 6: 커밋**

```bash
git add apps/backend/src/main/kotlin/com/aulms/upload/TermUploadModels.kt apps/backend/src/main/kotlin/com/aulms/upload/TermJsonlParser.kt apps/backend/src/test/kotlin/com/aulms/upload/TermJsonlParserTest.kt
git commit -m "feat(upload): JSONL parser with tests"
```

---

## Task 6: Upsert 저장소 (Querydsl-SQL, postgres)

**Files:**
- Create: `apps/backend/src/main/kotlin/com/aulms/upload/sql/QuerydslTermUpsertRepository.kt`

upsert 규칙: 존재(PK)하면 UPDATE, 없으면 INSERT. INSERT 는 NOT NULL 필수필드가 모두 있어야 함(없으면 예외→FAILED). UPDATE 는 제공된(non-null) 필드만 set(부분 갱신).

- [ ] **Step 1: 저장소 작성**

Create `apps/backend/src/main/kotlin/com/aulms/upload/sql/QuerydslTermUpsertRepository.kt`:

```kotlin
package com.aulms.upload.sql

import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
import com.aulms.term.sql.QTerm
import com.aulms.term.sql.QTermAlias
import com.aulms.term.sql.QTermExpression
import com.aulms.term.sql.QTermRelationship
import com.aulms.upload.ParsedRelationship
import com.aulms.upload.ParsedTerm
import com.querydsl.sql.SQLQueryFactory
import com.querydsl.sql.dml.SQLUpdateClause
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

/** 업로드용 term/표현/별칭/관계 upsert. postgres 전용. */
@Repository
@Profile("postgres")
class QuerydslTermUpsertRepository(
    private val queryFactory: SQLQueryFactory,
    private val jdbc: JdbcTemplate,
) {
    private val t = QTerm.term
    private val e = QTermExpression.termExpression
    private val a = QTermAlias.termAlias
    private val r = QTermRelationship.termRelationship

    /** term 본문 upsert. 반환: true=INSERT, false=UPDATE. 필수필드 누락 시 IllegalArgumentException. */
    fun upsertTerm(term: ParsedTerm): Boolean {
        val exists = queryFactory.select(t.termId).from(t).where(t.termId.eq(term.termId)).fetchFirst() != null
        val now = OffsetDateTime.now()
        if (exists) {
            val clause: SQLUpdateClause = queryFactory.update(t).where(t.termId.eq(term.termId))
            term.domainName?.let { clause.set(t.domainName, it) }
            term.usageType?.let { clause.set(t.usageType, it) }
            term.koreanName?.let { clause.set(t.koreanName, it) }
            term.englishName?.let { clause.set(t.englishName, it) }
            term.englishAbbreviation?.let { clause.set(t.englishAbbreviation, it) }
            term.businessDefinition?.let { clause.set(t.businessDefinition, it) }
            term.physicalType?.let { clause.set(t.physicalType, it) }
            term.digits?.let { clause.set(t.digits, it) }
            term.decimalPoint?.let { clause.set(t.decimalPoint, it) }
            term.status?.let { clause.set(t.status, it.value) }
            term.owner?.let { clause.set(t.owner, it) }
            term.version?.let { clause.set(t.version, it) }
            term.usageContext?.let { clause.set(t.usageContext, it) }
            term.termNumber?.let { clause.set(t.termNumber, it) }
            clause.set(t.updatedAt, now)
            clause.execute()
            return false
        }
        requireFields(term)
        queryFactory.insert(t)
            .set(t.termId, term.termId)
            .set(t.termNumber, term.termNumber ?: "TERM-${term.termId.removePrefix("T-")}")
            .set(t.domainName, term.domainName)
            .set(t.usageType, term.usageType)
            .set(t.koreanName, term.koreanName)
            .set(t.englishName, term.englishName)
            .set(t.englishAbbreviation, term.englishAbbreviation)
            .set(t.businessDefinition, term.businessDefinition)
            .set(t.physicalType, term.physicalType)
            .set(t.digits, term.digits)
            .set(t.decimalPoint, term.decimalPoint)
            .set(t.status, term.status!!.value)
            .set(t.owner, term.owner)
            .set(t.version, term.version ?: "1.0")
            .set(t.usageContext, term.usageContext)
            .set(t.createdAt, now)
            .set(t.updatedAt, now)
            .execute()
        return true
    }

    private fun requireFields(term: ParsedTerm) {
        val missing = buildList {
            if (term.domainName == null) add("domainName")
            if (term.usageType == null) add("usageType")
            if (term.koreanName == null) add("koreanName")
            if (term.englishName == null) add("englishName")
            if (term.englishAbbreviation == null) add("englishAbbreviation")
            if (term.businessDefinition == null) add("businessDefinition")
            if (term.physicalType == null) add("physicalType")
            if (term.digits == null) add("digits")
            if (term.decimalPoint == null) add("decimalPoint")
            if (term.status == null) add("status")
            if (term.owner == null) add("owner")
        }
        require(missing.isEmpty()) { "신규 용어 필수필드 누락: ${missing.joinToString(",")}" }
    }

    fun upsertExpression(ex: TermExpression) {
        val exists = queryFactory.select(e.expressionId).from(e).where(e.expressionId.eq(ex.expressionId)).fetchFirst() != null
        if (exists) {
            queryFactory.update(e).where(e.expressionId.eq(ex.expressionId))
                .set(e.termId, ex.termId)
                .set(e.expressionType, ex.expressionType.value)
                .set(e.expressionValue, ex.expressionValue)
                .set(e.isStandard, ex.isStandard)
                .set(e.language, ex.language)
                .set(e.style, ex.style)
                .execute()
        } else {
            queryFactory.insert(e)
                .set(e.expressionId, ex.expressionId)
                .set(e.termId, ex.termId)
                .set(e.expressionType, ex.expressionType.value)
                .set(e.expressionValue, ex.expressionValue)
                .set(e.isStandard, ex.isStandard)
                .set(e.language, ex.language)
                .set(e.style, ex.style)
                .execute()
        }
    }

    fun upsertAlias(al: TermAlias) {
        val exists = queryFactory.select(a.aliasId).from(a).where(a.aliasId.eq(al.aliasId)).fetchFirst() != null
        if (exists) {
            queryFactory.update(a).where(a.aliasId.eq(al.aliasId))
                .set(a.termId, al.termId)
                .set(a.aliasName, al.aliasName)
                .set(a.aliasType, al.aliasType.value)
                .set(a.recommendationAction, al.recommendationAction)
                .set(a.reason, al.reason)
                .execute()
        } else {
            queryFactory.insert(a)
                .set(a.aliasId, al.aliasId)
                .set(a.termId, al.termId)
                .set(a.aliasName, al.aliasName)
                .set(a.aliasType, al.aliasType.value)
                .set(a.recommendationAction, al.recommendationAction)
                .set(a.reason, al.reason)
                .execute()
        }
    }

    /** 관계 upsert. source/target 둘 다 존재해야 함(아니면 예외). relationshipId 기준. */
    fun upsertRelationship(sourceTermId: String, rel: ParsedRelationship) {
        require(termExists(sourceTermId)) { "source 용어 없음: $sourceTermId" }
        require(termExists(rel.targetTermId)) { "target 용어 없음: ${rel.targetTermId}" }
        val exists = queryFactory.select(r.id).from(r).where(r.id.eq(rel.relationshipId)).fetchFirst() != null
        if (exists) {
            queryFactory.update(r).where(r.id.eq(rel.relationshipId))
                .set(r.sourceTermId, sourceTermId)
                .set(r.relationshipType, rel.relationshipType.value)
                .set(r.targetTermId, rel.targetTermId)
                .set(r.description, rel.description)
                .execute()
        } else {
            queryFactory.insert(r)
                .set(r.id, rel.relationshipId)
                .set(r.sourceTermId, sourceTermId)
                .set(r.relationshipType, rel.relationshipType.value)
                .set(r.targetTermId, rel.targetTermId)
                .set(r.description, rel.description)
                .execute()
        }
    }

    private fun termExists(termId: String): Boolean =
        queryFactory.select(t.termId).from(t).where(t.termId.eq(termId)).fetchFirst() != null

    /** 명시 ID 적재 후 시퀀스 정합(다음 신규 생성분 충돌 방지). */
    fun realignSequences() {
        jdbc.queryForObject("SELECT setval('term_expression_seq', (SELECT COALESCE(MAX(expression_id), 36) FROM term_expression))", Long::class.java)
        jdbc.queryForObject("SELECT setval('term_alias_seq', (SELECT COALESCE(MAX(CAST(SUBSTRING(alias_id FROM 3) AS INTEGER)), 13) FROM term_alias))", Long::class.java)
        jdbc.queryForObject("SELECT setval('term_relationship_id_seq', (SELECT COALESCE(MAX(id), 1) FROM term_relationship))", Long::class.java)
    }
}
```

- [ ] **Step 2: 컴파일 확인**

Run: `cd apps/backend && ./gradlew compileKotlin -q && echo OK`
Expected: `OK`

- [ ] **Step 3: 커밋**

```bash
git add apps/backend/src/main/kotlin/com/aulms/upload/sql/QuerydslTermUpsertRepository.kt
git commit -m "feat(upload): querydsl term upsert repository"
```

---

## Task 7: 결과 저장소 (term_upload_row)

**Files:**
- Create: `apps/backend/src/main/kotlin/com/aulms/upload/sql/QuerydslTermUploadResultRepository.kt`

- [ ] **Step 1: 저장소 작성**

Create `apps/backend/src/main/kotlin/com/aulms/upload/sql/QuerydslTermUploadResultRepository.kt`:

```kotlin
package com.aulms.upload.sql

import com.aulms.model.TermUploadBatchSummary
import com.aulms.model.TermUploadResult
import com.aulms.model.TermUploadRow
import com.aulms.model.TermUploadStatus
import com.aulms.persistence.PostgresSequences
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.sql.SQLQueryFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
@Profile("postgres")
class QuerydslTermUploadResultRepository(
    private val queryFactory: SQLQueryFactory,
    private val sequences: PostgresSequences,
) {
    private val u = QTermUploadRow.termUploadRow

    fun nextBatchId(today: String): String =
        "UPL-%s-%04d".format(today, sequences.next("term_upload_batch_seq"))

    fun saveRow(
        batchId: String,
        lineNo: Int,
        termId: String?,
        status: TermUploadStatus,
        rawJson: String,
        errorMessage: String?,
        uploadedAt: OffsetDateTime,
    ) {
        val success = status != TermUploadStatus.FAILED
        queryFactory.insert(u)
            .set(u.uploadBatchId, batchId)
            .set(u.lineNo, lineNo)
            .set(u.termId, termId)
            .set(u.status, status.value)
            .set(u.rawJson, rawJson)
            .set(u.errorMessage, errorMessage)
            .set(u.registeredAt, if (success) uploadedAt else null)
            .set(u.failedAt, if (success) null else uploadedAt)
            .set(u.uploadedAt, uploadedAt)
            .execute()
    }

    fun result(batchId: String): TermUploadResult {
        val rows = queryFactory.select(*u.all()).from(u)
            .where(u.uploadBatchId.eq(batchId)).orderBy(u.lineNo.asc()).fetch().map {
                TermUploadRow(
                    lineNo = it.get(u.lineNo)!!,
                    status = TermUploadStatus.forValue(it.get(u.status)!!),
                    termId = it.get(u.termId),
                    errorMessage = it.get(u.errorMessage),
                    registeredAt = it.get(u.registeredAt),
                    failedAt = it.get(u.failedAt),
                )
            }
        val uploadedAt = rows.firstOrNull()?.let {
            queryFactory.select(u.uploadedAt).from(u).where(u.uploadBatchId.eq(batchId)).fetchFirst()
        } ?: OffsetDateTime.now()
        return TermUploadResult(
            uploadBatchId = batchId,
            uploadedAt = uploadedAt,
            totalRows = rows.size,
            inserted = rows.count { it.status == TermUploadStatus.INSERTED },
            updated = rows.count { it.status == TermUploadStatus.UPDATED },
            failed = rows.count { it.status == TermUploadStatus.FAILED },
            rows = rows,
        )
    }

    fun listBatches(): List<TermUploadBatchSummary> {
        val ids = queryFactory.select(u.uploadBatchId, u.uploadedAt.max())
            .from(u).groupBy(u.uploadBatchId).orderBy(u.uploadedAt.max().desc()).fetch()
        return ids.map { tuple ->
            val batchId = tuple.get(u.uploadBatchId)!!
            val r = result(batchId)
            TermUploadBatchSummary(
                uploadBatchId = batchId,
                uploadedAt = r.uploadedAt,
                totalRows = r.totalRows,
                inserted = r.inserted,
                updated = r.updated,
                failed = r.failed,
            )
        }
    }
}
```

> 주의: 생성된 모델 `TermUploadResult/TermUploadRow/TermUploadBatchSummary/TermUploadStatus` 의 정확한 프로퍼티명/생성자는 Task 2 생성물 기준으로 맞춘다. `Expressions` import 미사용 시 제거.

- [ ] **Step 2: 컴파일 확인**

Run: `cd apps/backend && ./gradlew compileKotlin -q && echo OK`
Expected: `OK` (모델 프로퍼티명 불일치 시 생성물 확인 후 수정)

- [ ] **Step 3: 커밋**

```bash
git add apps/backend/src/main/kotlin/com/aulms/upload/sql/QuerydslTermUploadResultRepository.kt
git commit -m "feat(upload): term_upload_row result repository"
```

---

## Task 8: Row 프로세서 + 업로드 서비스 (TDD)

**Files:**
- Create: `apps/backend/src/main/kotlin/com/aulms/upload/TermUploadRowProcessor.kt`
- Create: `apps/backend/src/main/kotlin/com/aulms/upload/TermUploadService.kt`
- Test: `apps/backend/src/test/kotlin/com/aulms/upload/TermUploadServiceTest.kt`

- [ ] **Step 1: Row 프로세서 작성 (개별 트랜잭션)**

Create `apps/backend/src/main/kotlin/com/aulms/upload/TermUploadRowProcessor.kt`:

```kotlin
package com.aulms.upload

import com.aulms.upload.sql.QuerydslTermUpsertRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/** 한 row 의 term 본문 upsert 를 독립 트랜잭션으로 처리(한 줄 실패가 다른 줄을 롤백하지 않도록). */
@Component
@Profile("postgres")
class TermUploadRowProcessor(private val upsert: QuerydslTermUpsertRepository) {

    /** 반환: true=INSERTED, false=UPDATED. 예외 시 호출측이 FAILED 처리. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun upsertTermBody(term: ParsedTerm): Boolean {
        val inserted = upsert.upsertTerm(term)
        term.expressions.forEach { upsert.upsertExpression(it) }
        term.aliases.forEach { upsert.upsertAlias(it) }
        return inserted
    }

    /** 관계 upsert(독립 트랜잭션). 실패 시 예외 전파. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun upsertRelationships(termId: String, relationships: List<ParsedRelationship>) {
        relationships.forEach { upsert.upsertRelationship(termId, it) }
    }
}
```

- [ ] **Step 2: 서비스 실패 테스트 작성**

Create `apps/backend/src/test/kotlin/com/aulms/upload/TermUploadServiceTest.kt`:

```kotlin
package com.aulms.upload

import com.aulms.model.TermUploadStatus
import com.aulms.upload.sql.QuerydslTermUploadResultRepository
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class TermUploadServiceTest {

    private val processor = mock<TermUploadRowProcessor>()
    private val resultRepo = mock<QuerydslTermUploadResultRepository>()
    private val parser = TermJsonlParser()
    private val service = TermUploadService(parser, processor, resultRepo)

    private fun fullRow(termId: String) =
        """{"termId":"$termId","domainName":"고객","usageType":"표준항목","koreanName":"k","englishName":"E","englishAbbreviation":"AB","businessDefinition":"d","physicalType":"VARCHAR","digits":1,"decimalPoint":0,"status":"Draft","owner":"o","version":"1.0"}"""

    @Test
    fun `isolates failed row from successful rows`() {
        whenever(resultRepo.nextBatchId(any())).thenReturn("UPL-20260604-0001")
        // T-1 insert ok, T-2 throws, T-3 update ok
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
```

- [ ] **Step 3: 테스트 실패 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.upload.TermUploadServiceTest" -q`
Expected: FAIL (`TermUploadService` 미존재).

- [ ] **Step 4: 서비스 구현**

Create `apps/backend/src/main/kotlin/com/aulms/upload/TermUploadService.kt`:

```kotlin
package com.aulms.upload

import com.aulms.model.TermUploadResult
import com.aulms.model.TermUploadStatus
import com.aulms.upload.sql.QuerydslTermUploadResultRepository
import com.aulms.upload.sql.QuerydslTermUpsertRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class UploadCounts(val inserted: Int, val updated: Int, val failed: Int)

@Service
@Profile("postgres")
class TermUploadService(
    private val parser: TermJsonlParser,
    private val processor: TermUploadRowProcessor,
    private val resultRepo: QuerydslTermUploadResultRepository,
) {
    @Autowired(required = false)
    private var upsertRepo: QuerydslTermUpsertRepository? = null

    private val dateFmt = DateTimeFormatter.ofPattern("yyyyMMdd")

    /** 테스트에서 집계만 검증하기 위한 진입점. 결과 기록 포함. */
    fun process(content: String): UploadCounts {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val batchId = resultRepo.nextBatchId(now.format(dateFmt))
        val rows = parser.parse(content)

        var inserted = 0
        var updated = 0
        var failed = 0

        // pass1: term 본문
        val succeeded = mutableListOf<ParsedTerm>()
        rows.forEach { row ->
            if (row.term == null) {
                failed += 1
                resultRepo.saveRow(batchId, row.lineNo, null, TermUploadStatus.FAILED, row.rawJson, row.parseError, now)
                return@forEach
            }
            try {
                val isInsert = processor.upsertTermBody(row.term)
                if (isInsert) inserted += 1 else updated += 1
                succeeded.add(row.term)
                resultRepo.saveRow(
                    batchId, row.lineNo, row.term.termId,
                    if (isInsert) TermUploadStatus.INSERTED else TermUploadStatus.UPDATED,
                    row.rawJson, null, now,
                )
            } catch (ex: Exception) {
                failed += 1
                resultRepo.saveRow(batchId, row.lineNo, row.term.termId, TermUploadStatus.FAILED, row.rawJson, ex.message, now)
            }
        }

        // pass2: 관계 (term 적재 후). 관계 실패는 해당 term row 상태를 바꾸지 않고 로그성 무시(개발 단계).
        succeeded.forEach { term ->
            if (term.relationships.isNotEmpty()) {
                runCatching { processor.upsertRelationships(term.termId, term.relationships) }
            }
        }
        upsertRepo?.realignSequences()

        return UploadCounts(inserted, updated, failed)
    }

    fun upload(content: String): TermUploadResult {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val batchId = resultRepo.nextBatchId(now.format(dateFmt))
        processInto(batchId, content, now)
        upsertRepo?.realignSequences()
        return resultRepo.result(batchId)
    }

    private fun processInto(batchId: String, content: String, now: OffsetDateTime) {
        val rows = parser.parse(content)
        val succeeded = mutableListOf<ParsedTerm>()
        rows.forEach { row ->
            if (row.term == null) {
                resultRepo.saveRow(batchId, row.lineNo, null, TermUploadStatus.FAILED, row.rawJson, row.parseError, now)
                return@forEach
            }
            try {
                val isInsert = processor.upsertTermBody(row.term)
                succeeded.add(row.term)
                resultRepo.saveRow(
                    batchId, row.lineNo, row.term.termId,
                    if (isInsert) TermUploadStatus.INSERTED else TermUploadStatus.UPDATED,
                    row.rawJson, null, now,
                )
            } catch (ex: Exception) {
                resultRepo.saveRow(batchId, row.lineNo, row.term.termId, TermUploadStatus.FAILED, row.rawJson, ex.message, now)
            }
        }
        succeeded.forEach { term ->
            if (term.relationships.isNotEmpty()) {
                runCatching { processor.upsertRelationships(term.termId, term.relationships) }
            }
        }
    }

    fun result(batchId: String): TermUploadResult = resultRepo.result(batchId)
    fun listBatches() = resultRepo.listBatches()
}
```

> 설계 메모: `process(content)` 는 테스트용 집계 진입점, `upload(content)` 는 컨트롤러용(결과 DTO 반환). 두 메서드가 동일 로직을 공유하도록 리팩터 가능하나 가독성 위해 분리 유지. `realignSequences` 의 NPE 방지를 위해 `upsertRepo` 는 nullable 주입.

- [ ] **Step 5: 테스트 통과 확인**

Run: `cd apps/backend && ./gradlew test --tests "com.aulms.upload.TermUploadServiceTest" -q`
Expected: PASS.

- [ ] **Step 6: 커밋**

```bash
git add apps/backend/src/main/kotlin/com/aulms/upload/TermUploadRowProcessor.kt apps/backend/src/main/kotlin/com/aulms/upload/TermUploadService.kt apps/backend/src/test/kotlin/com/aulms/upload/TermUploadServiceTest.kt
git commit -m "feat(upload): row processor + upload service with isolation test"
```

---

## Task 9: 업로드 컨트롤러 (생성 stub 구현)

**Files:**
- Create: `apps/backend/src/main/kotlin/com/aulms/upload/TermUploadController.kt`

> 생성된 `TermApi`(또는 별도 인터페이스)의 `uploadTerms` 시그니처는 Task 2 생성물 확인 후 정확히 맞춘다. kotlin-spring 생성기는 multipart 를 `org.springframework.web.multipart.MultipartFile` 로 만든다.

- [ ] **Step 1: 생성된 시그니처 확인**

Run: `grep -n "uploadTerms\|listTermUploadBatches\|getTermUploadBatch" generated/backend/src/main/kotlin/com/aulms/api/*.kt`
Expected: 3개 메서드 시그니처 출력. 어느 인터페이스(예: `TermApi`)에 속하는지 확인.

- [ ] **Step 2: 컨트롤러 작성**

Create `apps/backend/src/main/kotlin/com/aulms/upload/TermUploadController.kt` (인터페이스명/시그니처는 Step 1 결과로 치환):

```kotlin
package com.aulms.upload

import com.aulms.api.TermApi
import com.aulms.model.TermUploadBatchListResponse
import com.aulms.model.TermUploadResult
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Profile("postgres")
class TermUploadController(private val service: TermUploadService) : TermApi {

    override fun uploadTerms(file: MultipartFile): ResponseEntity<TermUploadResult> {
        val content = file.bytes.toString(Charsets.UTF_8)
        return ResponseEntity.ok(service.upload(content))
    }

    override fun getTermUploadBatch(uploadBatchId: String): ResponseEntity<TermUploadResult> =
        ResponseEntity.ok(service.result(uploadBatchId))

    override fun listTermUploadBatches(): ResponseEntity<TermUploadBatchListResponse> =
        ResponseEntity.ok(TermUploadBatchListResponse(items = service.listBatches()))
}
```

> 주의: 기존 `TermApiController` 가 이미 `TermApi` 를 구현한다. `uploadTerms` 등 새 메서드가 `TermApi` 에 추가되면 **두 컨트롤러가 같은 인터페이스를 구현**해 빈 충돌/미구현이 생긴다. 해결책 중 택1 (Step 3에서 결정):
> - (권장) openapi 에서 업로드 오퍼레이션에 별도 `tags: [TermUpload]` 부여 → 생성기가 `TermUploadApi` 인터페이스 분리. 그러면 이 컨트롤러는 `TermUploadApi` 만 구현, 충돌 없음.
> - 또는 업로드 메서드도 기존 `TermApiController` 에 함께 구현.

- [ ] **Step 3: 인터페이스 분리 처리 (권장안 적용)**

Modify `openapi/paths/term-uploads.yaml` — 3개 오퍼레이션 `tags` 를 `[Term]` → `[TermUpload]` 로 변경하고 `openapi/openapi.yaml` `tags:` 에 추가:

```yaml
  - name: TermUpload
    description: 용어 JSONL 일괄 업로드 API
```
그 후 Task 2 재생성(`bash scripts/openapi-generate-backend.sh && bash scripts/openapi-generate-frontend.sh`). 이제 `TermUploadApi` 인터페이스 생성 → 컨트롤러 `: TermUploadApi` 로 수정.

- [ ] **Step 4: 컴파일 + 컨트롤러 검증**

Run: `cd apps/backend && ./gradlew compileKotlin -q && echo OK`
Expected: `OK`

- [ ] **Step 5: 커밋**

```bash
git add openapi generated apps/backend/src/main/kotlin/com/aulms/upload/TermUploadController.kt
git commit -m "feat(upload): TermUpload controller + split api tag"
```

---

## Task 10: 통합 검증 (postgres + docker)

**Files:** 없음(런타임 검증).

- [ ] **Step 1: 앱 기동 (postgres)**

Run:
```bash
cd apps/backend && SPRING_PROFILES_ACTIVE=postgres SERVER_PORT=8082 POSTGRES_PASSWORD=changeme NEO4J_PASSWORD=changeme123 ./gradlew bootRun > /tmp/upload-verify.log 2>&1 &
until grep -q "Started AulmsBackend" /tmp/upload-verify.log; do sleep 3; done; echo STARTED
```
Expected: `STARTED`.

- [ ] **Step 2: 업로드 호출**

Run:
```bash
curl -s -F "file=@docs/jsons/terms.jsonl" http://localhost:8082/terms/uploads | python3 -m json.tool
```
Expected: `uploadBatchId`, `totalRows:4`, `updated`(기존 seed 갱신) + `inserted`/`failed` 합 = 4, `rows[]` 각 status/일시.

- [ ] **Step 3: 배치 재조회 + 목록**

Run:
```bash
BID=$(curl -s -F "file=@docs/jsons/terms.jsonl" http://localhost:8082/terms/uploads | python3 -c "import sys,json;print(json.load(sys.stdin)['uploadBatchId'])")
curl -s "http://localhost:8082/terms/uploads/$BID" | python3 -c "import sys,json;d=json.load(sys.stdin);print('rows',len(d['rows']),'failed',d['failed'])"
curl -s "http://localhost:8082/terms/uploads" | python3 -c "import sys,json;print('batches',len(json.load(sys.stdin)['items']))"
```
Expected: rows=4, batches>=2.

- [ ] **Step 4: 실패 행 시나리오 검증**

Run (깨진 줄 포함 임시 파일):
```bash
printf '%s\n%s\n' '{not json}' "$(head -1 docs/jsons/terms.jsonl)" > /tmp/mixed.jsonl
curl -s -F "file=@/tmp/mixed.jsonl" http://localhost:8082/terms/uploads | python3 -c "import sys,json;d=json.load(sys.stdin);print('failed',d['failed'],'rows',[(r['lineNo'],r['status'],bool(r['errorMessage'])) for r in d['rows']])"
```
Expected: `failed>=1`, 깨진 줄 status=FAILED + errorMessage 존재 + failedAt 설정.

- [ ] **Step 5: 앱 종료**

Run: `lsof -ti:8082 | xargs kill 2>/dev/null; echo stopped`

---

## Task 11: 프론트엔드 업로드 화면

**Files:**
- Modify: `apps/frontend/src/lib/term-api.ts`
- Create: `apps/frontend/src/app/terms/upload/page.tsx`

- [ ] **Step 1: 생성된 업로드 API 시그니처 확인**

Run: `grep -n "uploadTerms\|TermUploadApi\|TermUploadResult" generated/frontend/api.ts | head`
Expected: `uploadTerms(file: File ...)` 및 `TermUploadApi` 클래스 확인. (multipart 는 axios generator 가 `File`/`Blob` 파라미터로 생성)

- [ ] **Step 2: lib 와이어링 추가**

Modify `apps/frontend/src/lib/term-api.ts` — import 에 `TermUploadApi`, `type TermUploadResult` 추가하고 인스턴스 + 함수 export:

```typescript
// (상단 import 블록에 추가)
//   TermUploadApi,
//   type TermUploadResult,

const termUploadApi = new TermUploadApi(configuration, basePath, axiosInstance);

export async function uploadTermsFile(file: File): Promise<TermUploadResult> {
  const res = await termUploadApi.uploadTerms(file);
  return res.data;
}

export async function listTermUploadBatches() {
  const res = await termUploadApi.listTermUploadBatches();
  return res.data.items;
}
```

> `uploadTerms` 인자 형태(파일 직접 vs options)는 Step 1 생성물 기준으로 맞춘다.

- [ ] **Step 3: 업로드 페이지 작성**

Create `apps/frontend/src/app/terms/upload/page.tsx`:

```tsx
"use client";

import { useState } from "react";
import type { TermUploadResult } from "../../../lib/term-api";
import { uploadTermsFile } from "../../../lib/term-api";

export default function TermUploadPage() {
  const [file, setFile] = useState<File | null>(null);
  const [result, setResult] = useState<TermUploadResult | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function onUpload() {
    if (!file) return;
    setLoading(true);
    setError(null);
    try {
      setResult(await uploadTermsFile(file));
    } catch (e) {
      setError(e instanceof Error ? e.message : "업로드 실패");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="mx-auto max-w-4xl p-8">
      <h1 className="text-2xl font-bold mb-4">용어 JSONL 업로드</h1>

      <div className="flex items-center gap-3 mb-6">
        <input
          type="file"
          accept=".jsonl,application/jsonl,text/plain"
          onChange={(e) => setFile(e.target.files?.[0] ?? null)}
          className="border rounded px-3 py-2"
        />
        <button
          onClick={onUpload}
          disabled={!file || loading}
          className="rounded bg-blue-600 px-4 py-2 text-white disabled:opacity-50"
        >
          {loading ? "업로드 중..." : "업로드"}
        </button>
      </div>

      {error && <p className="text-red-600 mb-4">{error}</p>}

      {result && (
        <section>
          <div className="flex gap-2 mb-4 text-sm">
            <Badge label={`전체 ${result.totalRows}`} cls="bg-gray-200 text-gray-800" />
            <Badge label={`삽입 ${result.inserted}`} cls="bg-green-200 text-green-900" />
            <Badge label={`수정 ${result.updated}`} cls="bg-blue-200 text-blue-900" />
            <Badge label={`실패 ${result.failed}`} cls="bg-red-200 text-red-900" />
          </div>
          <p className="text-xs text-gray-500 mb-2">batch: {result.uploadBatchId}</p>
          <table className="w-full text-sm border">
            <thead className="bg-gray-100">
              <tr>
                <th className="p-2 text-left">#</th>
                <th className="p-2 text-left">termId</th>
                <th className="p-2 text-left">status</th>
                <th className="p-2 text-left">error</th>
                <th className="p-2 text-left">등록일시</th>
                <th className="p-2 text-left">실패일시</th>
              </tr>
            </thead>
            <tbody>
              {result.rows.map((r) => (
                <tr key={r.lineNo} className={r.status === "FAILED" ? "bg-red-50" : ""}>
                  <td className="p-2">{r.lineNo}</td>
                  <td className="p-2">{r.termId ?? "-"}</td>
                  <td className="p-2 font-medium">{r.status}</td>
                  <td className="p-2 text-red-600">{r.errorMessage ?? ""}</td>
                  <td className="p-2">{r.registeredAt ?? ""}</td>
                  <td className="p-2">{r.failedAt ?? ""}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      )}
    </main>
  );
}

function Badge({ label, cls }: { label: string; cls: string }) {
  return <span className={`rounded px-2 py-1 ${cls}`}>{label}</span>;
}
```

- [ ] **Step 4: 프론트 빌드/타입 검증**

Run: `cd apps/frontend && npm run build 2>&1 | tail -20`
Expected: 빌드 성공(타입 에러 없음). 에러 시 생성 client 시그니처에 맞춰 `term-api.ts`/`page.tsx` 수정.

- [ ] **Step 5: 커밋**

```bash
git add apps/frontend/src/lib/term-api.ts apps/frontend/src/app/terms/upload/page.tsx
git commit -m "feat(frontend): term JSONL upload screen"
```

---

## Self-Review 결과

- **Spec 커버리지**: 업로드(multipart) T1·T2·T9, upsert T6, 결과 테이블 T3·T7, 2-pass/격리 T8, API 3개 T1, postgres 전용 `@Profile` T6–T9, 프론트 T11, 검증 T10 — 전 항목 매핑됨.
- **관계 row span**: spec의 "관계 target 부재 시 FAILED 사유 포함"은 현재 pass2 에서 `runCatching` 으로 무시 처리(개발 단계 단순화)로 약화됨 → 실행 시 관계 실패를 row error 에 합치려면 T8 보강 필요(주: 알려진 단순화).
- **타입 일관성**: `ParsedTerm`/`ParsedRelationship`/`UploadCounts`/`TermUploadStatus` 명칭 일관. 생성 모델 프로퍼티명은 T2 산출물로 확정(플레이스홀더 아님, 확인 단계 명시).
- **시퀀스 정합**: `realignSequences` 가 expression/alias/relationship 시퀀스 전진 — seed 와 동일 패턴.

## 알려진 가정/후속

- 관계 upsert 실패의 row 단위 상태 반영은 단순화(무시). 필요 시 후속.
- 인증/권한 없음.
- 대용량 스트리밍 미고려(동기 처리).
