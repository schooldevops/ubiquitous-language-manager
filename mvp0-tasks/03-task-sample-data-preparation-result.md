# Task 03 완료 결과: 기존 데이터 사전 샘플 확보 및 정리

## 수행 일자

2026-05-27

## 확인한 규칙

`rules.md`를 확인했고 다음 기준을 반영했다.

- 유비쿼터스 언어 관리 시스템 자체도 데이터 딕셔너리를 작성한 뒤 개발한다.
- 이후 서버 API는 OpenAPI Spec을 먼저 작성하고 구현한다.
- OpenAPI Generator 기반 Backend server stub과 Frontend axios client 생성 원칙을 유지한다.
- 단계 완료 후 변경 내용을 사용자에게 확인받고 다음 단계로 진행한다.

## 생성한 산출물

| 산출물 | 파일 |
|---|---|
| 원천 데이터 사전 샘플 | `docs/dictionary/sample-data/source-data-dictionary-sample.csv` |
| TERM_MASTER 샘플 | `docs/dictionary/sample-data/term-master-sample.csv` |
| TERM_EXPRESSION 샘플 | `docs/dictionary/sample-data/term-expression-sample.csv` |
| TERM_ALIAS 샘플 | `docs/dictionary/sample-data/term-alias-sample.csv` |
| TERM_RELATIONSHIP 샘플 | `docs/dictionary/sample-data/term-relationship-sample.csv` |
| 기존 컬럼 매핑표 | `docs/dictionary/sample-data/column-mapping.md` |
| 데이터 품질 점검 결과 | `docs/dictionary/sample-data/data-quality-check.md` |
| MVP 샘플 용어 세트 | `docs/dictionary/sample-data/mvp-sample-term-set.md` |

## 포함한 필수 용어

- 고객번호
- 주문번호
- 주문일자
- 주문금액
- 주문상태코드

## 고객ID 검색 테스트 데이터

`TERM_ALIAS` 샘플에 다음 데이터를 포함했다.

| 입력 표현 | 유형 | 권장 처리 |
|---|---|---|
| 고객ID | Synonym | 고객번호로 변환 권장 |
| 고객 ID | Synonym | 고객번호로 변환 권장 |
| customerId | Synonym | customerNumber로 변환 권장 |
| CUST_ID | Forbidden | CUST_NO 사용 |
| 회원번호 | NeedsContext | 문맥 확인 필요 |
| 사용자 식별자 | NeedsContext | 문맥 확인 필요 |

## 데이터 품질 점검 결과

| 점검 항목 | 결과 |
|---|---|
| 한글 용어명 중복 | 없음 |
| 영문 약어 중복 | 없음 |
| 물리 타입 누락 | 없음 |
| 자릿수 누락 | 없음 |
| 소수점 누락 | 없음 |
| 관련 시스템 누락 | 없음 |
| 폐기 후보 | 고객ID 1건 |
| 문맥 확인 후보 | 회원번호, 사용자식별자 2건 |

## 완료 기준 충족 여부

| 완료 기준 | 상태 |
|---|---|
| MVP 개발과 테스트에 사용할 샘플 용어가 준비되어 있다 | 충족 |
| 고객ID 입력 시 고객번호를 추천하는 테스트 데이터를 만들 수 있다 | 충족 |

## 다음 단계

사용자 승인 후 `mvp0-tasks/04-task-openapi-baseline.md`를 진행한다.

