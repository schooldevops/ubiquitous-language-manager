# Artifact Validation API Spec

## 목적

OpenAPI YAML과 DB DDL 산출물에서 필드명, 컬럼명, 물리 스펙을 추출해 데이터 사전 기준으로 검증한다.

## OpenAPI 경로

| Method | Path | 설명 |
|---|---|---|
| POST | `/artifact-validations` | 개발 산출물 표준 용어 검증 |
| POST | `/reviews/openapi` | OpenAPI YAML 산출물 검증 |
| POST | `/reviews/ddl` | DB DDL 산출물 검증 |
| POST | `/reviews/code` | Kotlin/Java/TypeScript/SQL/Test 산출물 검증 |
| POST | `/reviews/pr` | PR 변경 파일 묶음 검증 |

## 요청

| 필드 | 설명 |
|---|---|
| `sourceType` | `AUTO`, `OPENAPI`, `DDL`, `KOTLIN`, `JAVA`, `TYPESCRIPT`, `SQL`, `TEST` |
| `filePath` | 검증 대상 파일 경로 |
| `content` | 검증 대상 파일 본문 |
| `domainNames` | 도메인 필터 |
| `failOnWarning` | WARNING도 실패 exitCode로 볼지 여부 |
| `includeSuggestions` | 권장 표현 포함 여부 |

## 응답

| 필드 | 설명 |
|---|---|
| `filePath` | 검증 대상 파일 경로 |
| `sourceType` | 판별된 산출물 유형 |
| `checkedCount` | 검증 표현 수 |
| `summary` | `ERROR`, `WARNING`, `INFO` 집계 |
| `issues` | `ValidationIssue` 목록 |
| `exitCode` | CLI 호환 종료 코드 |

## PR 요청

| 필드 | 설명 |
|---|---|
| `pullRequestId` | PR 번호 또는 외부 시스템 식별자 |
| `repository` | 저장소 이름 |
| `files` | 변경 파일 목록 |
| `failOnWarning` | WARNING도 실패 exitCode로 볼지 여부 |
| `includeSuggestions` | 권장 표현 포함 여부 |

`files` 항목은 `filePath`, `content`, 선택 `sourceType`을 가진다. `sourceType`이 없으면 파일 확장자로 판별한다.

## PR 응답

| 필드 | 설명 |
|---|---|
| `pullRequestId` | PR 번호 또는 외부 시스템 식별자 |
| `repository` | 저장소 이름 |
| `results` | 파일별 `ArtifactValidationResult` |
| `summary` | 전체 집계 |
| `issues` | 전체 `ValidationIssue` 목록 |
| `exitCode` | CI 호환 종료 코드 |

## 대표 예시

OpenAPI YAML에서 `customerId` property를 찾으면 `customerNumber`를 권장한다.

DDL에서 `CUST_ID` 컬럼을 찾으면 `CUST_NO`를 권장하고 `ERROR`로 반환한다.

Kotlin DTO에서 `val customerId: String`을 찾으면 `customerNumber`를 권장한다.

테스트 문장에서 `고객 ID`를 찾으면 `고객번호`를 권장한다.
