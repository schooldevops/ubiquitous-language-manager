# 초기 유비쿼터스 언어 사전

## 1. 목적

본 문서는 유비쿼터스 랭기지 관리 시스템 자체를 개발하기 전에 사용할 초기 표준 용어를 정의한다.

## 2. 핵심 개발 용어

| 표준 용어 | 영문명 | DB 컬럼 후보 | API 필드명 | 코드 변수명 | 설명 |
|---|---|---|---|---|---|
| 표준용어 | Standard Term | STD_TERM | standardTerm | standardTerm | 승인된 업무 용어 |
| 용어식별자 | Term Identifier | TERM_ID | termId | termId | 용어를 식별하는 내부 ID |
| 용어번호 | Term Number | TERM_NO | termNumber | termNumber | 업무적으로 노출 가능한 용어 번호 |
| 도메인명 | Domain Name | DOMAIN_NAME | domainName | domainName | 용어가 속한 업무 도메인명 |
| 사용구분 | Usage Type | USAGE_TYPE | usageType | usageType | 단위 항목, 표준 항목 등 사용 분류 |
| 한글명 | Korean Name | KOREAN_NAME | koreanName | koreanName | 용어의 한글 표준명 |
| 영문명 | English Name | ENGLISH_NAME | englishName | englishName | 용어의 영문 표준명 |
| 영문약어 | English Abbreviation | ENGLISH_ABBR | englishAbbreviation | englishAbbreviation | DB 컬럼명 등에 사용하는 영문 약어 |
| 업무정의 | Business Definition | BUSINESS_DEFINITION | businessDefinition | businessDefinition | 용어가 의미하는 업무 정의 |
| 사용맥락 | Usage Context | USAGE_CONTEXT | usageContext | usageContext | 용어를 사용하는 업무 상황 |
| 물리타입 | Physical Type | PHYSICAL_TYPE | physicalType | physicalType | DB 물리 타입 |
| 자릿수 | Digits | DIGITS | digits | digits | 데이터 길이 |
| 소수점 | Decimal Point | DECIMAL_POINT | decimalPoint | decimalPoint | 소수점 자리수 |
| 용어상태 | Term Status | STATUS | status | status | Draft, Reviewing, Approved, Deprecated, Rejected |
| 소유자 | Owner | OWNER | owner | owner | 용어 관리 책임자 |
| 버전 | Version | VERSION | version | version | 용어 변경 버전 |
| 표현유형 | Expression Type | EXPRESSION_TYPE | expressionType | expressionType | DB_COLUMN, API_FIELD, CODE_VARIABLE 등 표현 종류 |
| 표현값 | Expression Value | EXPRESSION_VALUE | expressionValue | expressionValue | 산출물별 실제 표현값 |
| 별칭 | Alias | ALIAS_NAME | aliasName | aliasName | 표준 용어와 연결되는 별도 표현 |
| 별칭유형 | Alias Type | ALIAS_TYPE | aliasType | aliasType | 유사어, 금지어, 폐기어 등 별칭 분류 |
| 관계유형 | Relationship Type | RELATIONSHIP_TYPE | relationshipType | relationshipType | 용어 간 관계 유형 |
| 검증이슈 | Validation Issue | VALIDATION_ISSUE | validationIssue | validationIssue | 표준 위반 또는 권고 결과 |

## 3. 상태값

| 값 | 설명 |
|---|---|
| Draft | 초안 상태, 개발 산출물 사용 불가 |
| Reviewing | 검토 중 상태, 개발 산출물 사용 불가 |
| Approved | 승인 상태, 개발 산출물 사용 가능 |
| Deprecated | 폐기 상태, 신규 사용 불가 |
| Rejected | 반려 상태, 추천 금지 |

## 4. 표현 유형

| 값 | 설명 |
|---|---|
| Korean | 한글 표준명 |
| English | 영문 표준명 |
| DB_COLUMN | DB 컬럼명 |
| API_FIELD | API 필드명 |
| CODE_VARIABLE | 코드 변수명 |
| UI_LABEL | 화면 표시명 |
| TEST_WORD | 테스트 용어 |

## 5. 별칭 유형

| 값 | 설명 |
|---|---|
| Synonym | 표준 용어와 같은 의미의 유사어 |
| Forbidden | 사용 금지 표현 |
| Deprecated | 폐기된 표현 |
| NeedsContext | 문맥 확인이 필요한 표현 |

## 6. 역할 용어

| 표준 용어 | 영문명 | DB 컬럼 후보 | API 필드명 | 코드 변수명 | 설명 |
|---|---|---|---|---|---|
| 관리자 | Administrator | ADMIN | admin | admin | 시스템 설정과 권한을 관리하는 역할 |
| 데이터스튜어드 | Data Steward | DATA_STEWARD | dataSteward | dataSteward | 담당 도메인의 표준 용어를 책임지는 역할 |
| 개발리더 | Development Lead | DEVELOPMENT_LEAD | developmentLead | developmentLead | 개발 산출물 정합성을 검토하는 역할 |
| 아키텍트 | Architect | ARCHITECT | architect | architect | 도메인 간 충돌과 기술 구조를 검토하는 역할 |
| 기획자 | Planner | PLANNER | planner | planner | 요구사항과 기획서를 작성하는 역할 |
| 개발자 | Developer | DEVELOPER | developer | developer | API, DB, 코드, 테스트를 개발하는 역할 |
| 운영자 | Operator | OPERATOR | operator | operator | 운영 상태와 검증 결과를 모니터링하는 역할 |
| 조회자 | Viewer | VIEWER | viewer | viewer | 표준 용어와 검증 결과를 조회하는 역할 |

## 7. 도메인 용어

| 표준 용어 | 영문명 | DB 컬럼 후보 | API 필드명 | 코드 변수명 | 설명 |
|---|---|---|---|---|---|
| 고객도메인 | Customer Domain | CUSTOMER_DOMAIN | customerDomain | customerDomain | 고객 관련 용어를 관리하는 업무 도메인 |
| 주문도메인 | Order Domain | ORDER_DOMAIN | orderDomain | orderDomain | 주문 관련 용어를 관리하는 업무 도메인 |
| 결제도메인 | Payment Domain | PAYMENT_DOMAIN | paymentDomain | paymentDomain | 결제 관련 용어를 관리하는 업무 도메인 |
| 상품도메인 | Product Domain | PRODUCT_DOMAIN | productDomain | productDomain | 상품 관련 용어를 관리하는 업무 도메인 |
| 계약도메인 | Contract Domain | CONTRACT_DOMAIN | contractDomain | contractDomain | 계약 관련 용어를 관리하는 업무 도메인 |
| 청구도메인 | Billing Domain | BILLING_DOMAIN | billingDomain | billingDomain | 청구 관련 용어를 관리하는 업무 도메인 |
| 상담도메인 | Consultation Domain | CONSULTATION_DOMAIN | consultationDomain | consultationDomain | 상담 관련 용어를 관리하는 업무 도메인 |
| 공통도메인 | Common Domain | COMMON_DOMAIN | commonDomain | commonDomain | 여러 업무에서 공통으로 사용하는 용어 도메인 |
