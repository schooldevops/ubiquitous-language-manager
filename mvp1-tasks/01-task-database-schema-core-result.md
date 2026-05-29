# Task 01 완료 결과: 데이터 사전 코어 DB 스키마 구현

## 수행 일자

2026-05-27

## 확인한 규칙

`rules.md`를 확인했고 다음 기준을 반영했다.

- Backend 기술 스택은 Kotlin, Spring Boot, QueryDSL SQL, PostgreSQL이다.
- 서버 API는 이후 반드시 OpenAPI Spec을 먼저 작성한 뒤 구현한다.
- 개발 대상 스키마와 필드명은 유비쿼터스 언어와 데이터 딕셔너리 기준을 따른다.
- 단계 완료 후 사용자 확인과 승인을 받는다.

## 생성한 산출물

| 산출물 | 파일 |
|---|---|
| 코어 스키마 migration | `apps/backend/src/main/resources/db/migration/V1__create_dictionary_core_schema.sql` |
| 샘플 seed migration | `apps/backend/src/main/resources/db/migration/V2__seed_dictionary_core_sample_data.sql` |
| DB 스키마 설명 | `docs/database/core-schema.md` |
| ERD 문서 | `docs/database/core-schema-erd.md` |

## 생성한 테이블

- `term_master`
- `term_expression`
- `term_alias`
- `term_relationship`
- `term_change_history`
- `term_candidate`
- `term_review_request`

## 적용한 제약조건

- `term_master.status` check constraint
- `term_expression.expression_type` check constraint
- `term_alias.alias_type` check constraint
- `term_relationship.relationship_type` check constraint
- `term_candidate.status` check constraint
- `term_review_request.status` check constraint
- 중복 방지를 위한 unique constraint
- 관계 무결성을 위한 foreign key constraint

## 설계한 검색 인덱스

- 한글 용어명 검색 인덱스
- 영문명 검색 인덱스
- 영문 약어 검색 인덱스
- 도메인/상태 필터 인덱스
- 표현 유형/표현값 검색 인덱스
- 별칭명 검색 인덱스
- 용어 관계 source/target 인덱스

## MVP seed 데이터

Seed migration에는 다음 기준 데이터를 포함했다.

- 고객번호
- 고객명
- 고객상태코드
- 주문번호
- 주문일자
- 주문일시
- 주문금액
- 주문상태코드
- 결제금액
- 결제일시
- 상품번호
- 상품명
- 상품대표이미지URL
- 청구번호
- 청구금액
- 상담번호
- 상담일시

## 완료 기준 충족 여부

| 완료 기준 | 상태 |
|---|---|
| 고객번호 샘플 용어를 DB에 저장할 수 있다 | 충족 |
| 고객번호의 DB 컬럼명, API 필드명, 코드 변수명을 표현 매핑으로 저장할 수 있다 | 충족 |
| 고객ID를 고객번호의 유사어로 저장할 수 있다 | 충족 |

## 다음 단계

사용자 승인 후 `mvp1-tasks/02-task-term-openapi-spec.md`를 진행한다.

