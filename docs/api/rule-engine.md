# Rule Engine

## 1. 목적

Rule Engine은 기획서, API, DB, 코드 산출물에서 사내 표준 용어 사용 여부를 판단한다. 검증 결과는 OpenAPI 공통 schema의 `ValidationIssue` 형식을 사용한다.

## 2. 구현 파일

| 파일 | 설명 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/rule/RuleEngine.kt` | 상태, 표현 유형, 금지어, 폐기어, 물리 스펙 검증 규칙 구현 |
| `apps/backend/src/test/kotlin/com/aulms/rule/RuleEngineTest.kt` | Rule Engine 단위 테스트 |

## 3. Severity

| Severity | 의미 |
|---|---|
| `ERROR` | 개발 산출물 반영 불가 |
| `WARNING` | 표준 표현으로 교체 권장 또는 문맥 확인 필요 |
| `INFO` | 신규 후보 검토 등 안내 |

## 4. 상태 기반 규칙

| 상태 | 개발 산출물 사용 |
|---|---|
| `Approved` | 가능 |
| `Draft` | 불가, `ERROR` |
| `Reviewing` | 불가, `ERROR` |
| `Deprecated` | 불가, `ERROR` |
| `Rejected` | 불가, 추천 금지, `ERROR` |

## 5. 표현 유형 규칙

| 산출물 | 검증 표현 유형 |
|---|---|
| OpenAPI field | `API_FIELD` |
| DB DDL column | `DB_COLUMN` |
| DTO, Entity, Service code | `CODE_VARIABLE` |
| 화면 라벨 | `UI_LABEL` |
| 테스트 시나리오 | `TEST_WORD` |

## 6. 대표 결과

| 입력 | 표현 유형 | 결과 |
|---|---|---|
| `customerId` | `CODE_VARIABLE` | `customerNumber` 권고, `WARNING` |
| `CUST_ID` | `DB_COLUMN` | `CUST_NO` 권고, `ERROR` |
| `customerNumber` | `API_FIELD` | issue 없음 |
| Draft 용어 | 개발 산출물 | 사용 불가, `ERROR` |

## 7. 물리 스펙 검증 설계

물리 타입, 자릿수, 소수점은 용어의 `physicalType`, `digits`, `decimalPoint` 값과 산출물 입력값을 비교한다. 불일치 시 `ValidationIssue`를 `ERROR`로 반환한다.
