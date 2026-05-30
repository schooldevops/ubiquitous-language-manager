# 개발자 연동 가이드

## 1. 목적

개발자는 데이터 사전 기반 검증 도구를 로컬 개발, PR 리뷰, CI 품질 게이트에 연결해 표준 용어 위반을 빠르게 찾고 수정한다.

## 2. OpenAPI Spec

| 항목 | 위치 |
|---|---|
| Root spec | `openapi/openapi.yaml` |
| Review path | `openapi/paths/reviews.yaml` |
| Artifact schema | `openapi/schemas/artifact-validation.yaml` |

검증:

```bash
openapi-generator-cli validate -i openapi/openapi.yaml
```

서버/클라이언트 생성:

```bash
./scripts/openapi-generate-backend.sh
./scripts/openapi-generate-frontend.sh
```

## 3. Review API

| API | 용도 |
|---|---|
| `POST /reviews/openapi` | OpenAPI YAML API field 검증 |
| `POST /reviews/ddl` | DB DDL 컬럼명/물리 스펙 검증 |
| `POST /reviews/code` | DTO, Entity, SQL, 테스트 용어 검증 |
| `POST /reviews/pr` | PR 변경 파일 묶음 검증 |

요청 기본형:

```json
{
  "sourceType": "KOTLIN",
  "filePath": "CustomerOrderResponse.kt",
  "content": "data class CustomerOrderResponse(val customerId: String)",
  "includeSuggestions": true
}
```

응답 기본형:

```json
{
  "filePath": "CustomerOrderResponse.kt",
  "sourceType": "KOTLIN",
  "checkedCount": 1,
  "summary": {
    "errorCount": 0,
    "warningCount": 1,
    "infoCount": 0
  },
  "issues": [
    {
      "severity": "WARNING",
      "source": "KOTLIN",
      "location": "CustomerOrderResponse.kt:1",
      "inputExpression": "customerId",
      "standardTerm": "고객번호",
      "recommendedExpression": "customerNumber",
      "reason": "권장 표현은 customerNumber입니다."
    }
  ],
  "exitCode": 0
}
```

## 4. Generated Client

프런트 TypeScript axios client 위치:

```text
generated/frontend
```

사용 예:

```ts
import { Configuration, ReviewApi } from "@aulms/api-client";

const reviewApi = new ReviewApi(
  new Configuration({
    basePath: process.env.NEXT_PUBLIC_API_BASE_URL,
    accessToken: process.env.NEXT_PUBLIC_API_TOKEN,
  }),
);

const response = await reviewApi.reviewCodeArtifact({
  sourceType: "KOTLIN",
  filePath: "CustomerOrderResponse.kt",
  content: "data class CustomerOrderResponse(val customerId: String)",
  includeSuggestions: true,
});

console.log(response.data.issues);
```

백엔드 Kotlin server stub 위치:

```text
generated/backend/src/main/kotlin/com/aulms/api/ReviewApi.kt
```

구현체:

```text
apps/backend/src/main/kotlin/com/aulms/review/ReviewApiController.kt
```

## 5. 로컬 CLI

PR 검증 CLI:

```bash
git diff --name-only origin/main...HEAD > changed-files.txt
ruby scripts/aulms-pr-review.rb \
  --changed-files changed-files.txt \
  --output-json build/reports/aulms-pr-review.json \
  --output-md build/reports/aulms-pr-review.md \
  --fail-on error
```

필요 환경 변수:

| 변수 | 설명 |
|---|---|
| `AULMS_API_URL` | AULMS API base URL |
| `AULMS_API_TOKEN` | Bearer token |
| `PR_NUMBER` | PR 번호 |
| `GITHUB_REPOSITORY` | 저장소 이름 |

샘플 E2E:

```bash
ruby scripts/test-aulms-pr-review.rb
```

## 6. OpenAPI YAML 검증 예시

입력:

```yaml
components:
  schemas:
    CustomerOrderResponse:
      type: object
      properties:
        customerId:
          type: string
```

호출:

```bash
curl -X POST "$AULMS_API_URL/reviews/openapi" \
  -H "Authorization: Bearer $AULMS_API_TOKEN" \
  -H "Content-Type: application/json" \
  -d @request.json
```

조치:

```yaml
properties:
  customerNumber:
    type: string
```

## 7. DDL 검증 예시

입력:

```sql
CREATE TABLE CUSTOMER_ORDER (
  CUST_ID VARCHAR(20)
);
```

결과:

| Input | Recommendation | Severity |
|---|---|---|
| `CUST_ID` | `CUST_NO` | `ERROR` |

조치:

```sql
CREATE TABLE CUSTOMER_ORDER (
  CUST_NO VARCHAR(20)
);
```

## 8. DTO 검증 예시

입력:

```kotlin
data class CustomerOrderResponse(
    val customerId: String,
)
```

결과:

| Input | Recommendation | Severity |
|---|---|---|
| `customerId` | `customerNumber` | `WARNING` |

조치:

```kotlin
data class CustomerOrderResponse(
    val customerNumber: String,
)
```

## 9. PR 코멘트 예시

```markdown
<!-- aulms-artifact-review -->
## 데이터 사전 표준 검증 결과

| Severity | Count |
|---|---:|
| ERROR | 1 |
| WARNING | 1 |
| INFO | 0 |

### 위반 목록

| File | Line | Severity | Input | Recommendation | Reason |
|---|---:|---|---|---|---|
| CustomerOrderResponse.kt | 4 | WARNING | customerId | customerNumber | 고객번호 표준 표현 불일치 |
| customer.sql | 2 | ERROR | CUST_ID | CUST_NO | Forbidden 표현 |
```

## 10. CI 실패 대응

1. PR 코멘트 또는 `build/reports/aulms-pr-review.md`를 확인한다.
2. `ERROR` 항목을 먼저 수정한다.
3. `recommendedExpression` 값으로 필드명, 컬럼명, 테스트 용어를 치환한다.
4. 물리 타입/자릿수/소수점 불일치는 데이터 사전 기준값을 확인한다.
5. 로컬에서 CLI를 재실행한다.
6. 여전히 `INFO`만 남으면 신규 용어 후보 등록 여부를 판단한다.

## 11. 신규 용어 후보 요청

데이터 사전에 없는 표현이 `INFO`로 나오면 후보 등록을 요청한다.

API:

```text
POST /candidates
```

필수 작성:

| 항목 | 설명 |
|---|---|
| 한글 용어명 | 업무 표준 후보 |
| 영문 용어명 | 영어 표현 |
| 영문 약어 | DB 컬럼 후보 |
| 도메인 | 고객, 주문 등 |
| 업무 정의 | 의미 |
| 사용 맥락 | 화면/API/테이블 |
| 물리 타입 | VARCHAR, DATE, NUMERIC 등 |
| 자릿수/소수점 | 물리 스펙 |

후보 상태에서는 개발 산출물에 바로 사용하지 않는다. `Approved` 후 사용한다.

## 12. Deprecated/Forbidden 대응

| 상태 | 처리 |
|---|---|
| `Deprecated` | 신규 사용 금지. 대체 표준 용어로 교체 |
| `Forbidden` alias | 즉시 교체. CI `ERROR` |
| `Draft` 용어 | 기획서 후보로만 사용. 개발 산출물 사용 금지 |
| `Reviewing` 용어 | 승인 전 개발 산출물 사용 금지 |

예:

| 입력 | 권장 |
|---|---|
| `CUST_ID` | `CUST_NO` |
| `customerId` | `customerNumber` |
| `고객 ID` | `고객번호` |

## 13. 개발자 체크리스트

- OpenAPI spec 먼저 수정한다.
- API field는 데이터 사전 `API_FIELD` 값을 쓴다.
- DB column은 데이터 사전 `DB_COLUMN` 값을 쓴다.
- DTO/Entity field는 데이터 사전 `CODE_VARIABLE` 값을 쓴다.
- 테스트 문장은 데이터 사전 `TEST_WORD` 값을 쓴다.
- PR 전 로컬 CLI를 실행한다.
- `ERROR`는 반드시 수정한다.
- 신규 용어는 후보 등록 후 승인받는다.
