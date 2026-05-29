# 기존 데이터 사전 컬럼 매핑표

## 1. 목적

기존 데이터 사전 샘플을 신규 유비쿼터스 랭기지 데이터 모델로 이관하기 위한 컬럼 매핑 기준을 정의한다.

## 2. 기존 컬럼과 신규 모델 매핑

| 기존 데이터 사전 컬럼 | 신규 테이블 | 신규 컬럼 | 매핑 방식 | 비고 |
|---|---|---|---|---|
| 번호 | `TERM_MASTER` | `TERM_NO` | `TERM-` prefix를 붙여 변환 | 기존 번호는 원천 번호로 보존 가능 |
| 사용 구분 | `TERM_MASTER` | `USAGE_TYPE` | 그대로 매핑 | 표준항목, 단위항목 등 |
| 한글 용어명 | `TERM_MASTER` | `KOREAN_NAME` | 그대로 매핑 | 필수 |
| 영문 용어명 | `TERM_MASTER` | `ENGLISH_NAME` | 그대로 매핑 | 필수 |
| 영문 약어 | `TERM_MASTER` | `ENGLISH_ABBREVIATION` | 그대로 매핑 | DB 컬럼 후보 |
| 관련 시스템 | 별도 시스템 매핑 또는 관계 | `SYSTEM_NAME` 후보 | 쉼표 분리 후 시스템 관계 생성 | MVP에서는 텍스트 보존 |
| 물리 타입 | `TERM_MASTER` | `PHYSICAL_TYPE` | 그대로 매핑 | 필수 |
| 자릿수 | `TERM_MASTER` | `DIGITS` | 숫자로 변환 | 필수 |
| 소수점 | `TERM_MASTER` | `DECIMAL_POINT` | 숫자로 변환 | 필수 |
| 비고 | `TERM_ALIAS`, `TERM_CHANGE_HISTORY` | 상황별 매핑 | Deprecated, NeedsContext 등 분류 | 이관 검토 필요 |

## 3. 추가 생성 규칙

| 생성 대상 | 생성 규칙 |
|---|---|
| `TERM_ID` | `T-` + 6자리 일련번호 |
| `DOMAIN_NAME` | 한글 용어명과 관련 시스템 기준으로 도메인 추론 |
| `STATUS` | 검증 완료 샘플은 Approved, 폐기 후보는 Deprecated 후보로 분리 |
| `OWNER` | `{도메인명}도메인 데이터스튜어드` |
| `VERSION` | 최초 이관 시 `1.0` |
| `API_FIELD` | 영문명 기반 camelCase |
| `CODE_VARIABLE` | API_FIELD와 동일한 camelCase |
| `DB_COLUMN` | 영문 약어 기반 UPPER_SNAKE |

## 4. 이관 제외 또는 후보 분리 기준

- 같은 한글 용어명이 2개 이상이면 중복 후보로 분리한다.
- 같은 영문 약어가 2개 이상이면 중복 약어 후보로 분리한다.
- 물리 타입이 없으면 이관 보류한다.
- 자릿수 또는 소수점이 없으면 이관 보류한다.
- 관련 시스템이 없으면 보완 필요 항목으로 표시한다.
- 비고에 Deprecated, 폐기, 금지, 혼동 표현이 있으면 `TERM_ALIAS` 또는 Deprecated 후보로 분리한다.

