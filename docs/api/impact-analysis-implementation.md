# Impact Analysis 구현

## 구현 범위

- `ImpactApiController`
- `ImpactAnalysisService`
- OpenAPI generator 기반 서버 인터페이스와 프런트 axios 클라이언트
- 영향도 분석 화면
- Contract test

## 조회 방식

MVP4 구현은 `GraphSyncWorker.buildSnapshot()` 결과를 사용한다.

- 1-hop 직접 영향
  - `Column` 노드: `DB_COLUMN`
  - `APIField` 노드: `API_FIELD`
  - `DTOField` 노드: `DTO`
  - 기준 시스템, 대표 테이블, 대표 API
- 2-hop 확장 영향
  - 기획서 표현: `DOCUMENT`
  - 테스트 케이스: `TEST_CASE`

현재 문서와 테스트 케이스는 별도 저장소가 없으므로 2-hop 확장 대상으로 합성한다. 추후 기획서 저장소와 테스트 관리 저장소가 생기면 Graphify sync 원천으로 편입한다.

## 위험도 산정

변경 유형별 기본 점수에 영향 대상 수를 반영한다.

| 변경 유형 | 기본 점수 |
|---|---:|
| `DESCRIPTION_UPDATE` | 15 |
| `ALIAS_ADD` | 25 |
| `API_FIELD_RENAME` | 55 |
| `DB_COLUMN_RENAME` | 70 |
| `PHYSICAL_TYPE_CHANGE` | 85 |
| `DIGITS_CHANGE` | 75 |
| `DEPRECATE_TERM` | 80 |

점수 구간은 `LOW`, `MEDIUM`, `HIGH`로 분류한다.

## 프런트 화면

메인 용어 관리 화면에 영향도 분석 패널을 추가했다.

- 변경 유형 선택
- 선택 용어 기준 분석 실행
- 위험도와 영향 대상 수 표시
- 시스템, DB, API, DTO, 문서, 테스트 영향 목록 표시
- 권장 조치 표시

## 검증

`ImpactAnalysisApiContractTest`에서 다음을 검증한다.

- 고객번호 `API_FIELD_RENAME` 변경 분석
- `HIGH` 위험도 반환
- `DB_COLUMN`, `API_FIELD`, `DTO`, `DOCUMENT`, `TEST_CASE` 대상 반환
- `CUST_NO`, `customerNumber`, 기획서 표현, 테스트 케이스 대상 반환
- API v2 하위 호환 권장 조치 반환
