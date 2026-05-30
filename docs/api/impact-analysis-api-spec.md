# Impact Analysis API Spec

## 목적

Graphify 관계 그래프를 기반으로 표준 용어 변경 시 영향받는 시스템, DB, API, DTO, 기획서, 테스트를 조회한다.

## OpenAPI 경로

| Method | Path | 설명 |
|---|---|---|
| GET | `/impact/terms/{termId}` | 기준 용어의 변경 영향도 분석 |

## 요청 파라미터

| 이름 | 위치 | 필수 | 설명 |
|---|---|---|---|
| `termId` | path | Y | 영향도 분석 기준 용어 ID |
| `changeType` | query | N | 변경 유형 |
| `includeTwoHop` | query | N | 2-hop 관계까지 확장할지 여부 |

## 변경 유형

| 값 | 설명 |
|---|---|
| `DESCRIPTION_UPDATE` | 업무 정의 또는 설명 보완 |
| `ALIAS_ADD` | 별칭 추가 |
| `API_FIELD_RENAME` | API 필드명 변경 |
| `DB_COLUMN_RENAME` | DB 컬럼명 변경 |
| `PHYSICAL_TYPE_CHANGE` | 물리 타입 변경 |
| `DIGITS_CHANGE` | 자릿수 변경 |
| `DEPRECATE_TERM` | 용어 폐기 |

## 대표 응답

`GET /impact/terms/T-000001?changeType=API_FIELD_RENAME&includeTwoHop=true`는 고객번호 변경 영향으로 다음 대상을 반환한다.

| 대상 유형 | 예시 |
|---|---|
| `SYSTEM` | `DICTIONARY` |
| `DB_TABLE` | `고객_MASTER` |
| `DB_COLUMN` | `CUST_NO` |
| `API_FIELD` | `customerNumber` |
| `DTO` | `customerNumber` |
| `DOCUMENT` | `고객번호 기획서 표현` |
| `TEST_CASE` | `고객번호 필수 검증` |

응답에는 `riskLevel`, `riskScore`, `recommendations`가 포함된다.
