# 도메인, 역할, 권한 모델

## 1. 목적

본 문서는 데이터 사전 기반 유비쿼터스 랭기지 관리 시스템에서 사용할 초기 업무 도메인, 사용자 역할, 권한 모델, 승인 프로세스 참여자를 정의한다.

## 2. 초기 업무 도메인

MVP에서는 도메인을 과도하게 세분화하지 않고, 용어 관리와 검증 흐름을 검증하기에 충분한 범위로 시작한다.

| 도메인명 | 영문명 | 설명 | 기본 소유 역할 |
|---|---|---|---|
| 고객 | Customer | 고객 식별, 고객 속성, 고객 상태 관련 용어 | 데이터 스튜어드 |
| 주문 | Order | 주문, 주문 내역, 주문 상태, 주문 금액 관련 용어 | 데이터 스튜어드 |
| 결제 | Payment | 결제 수단, 결제 금액, 결제 상태 관련 용어 | 데이터 스튜어드 |
| 상품 | Product | 상품, 상품 옵션, 상품 이미지, 상품 상태 관련 용어 | 데이터 스튜어드 |
| 계약 | Contract | 계약, 약정, 계약 상태 관련 용어 | 데이터 스튜어드 |
| 청구 | Billing | 청구, 청구서, 청구 금액 관련 용어 | 데이터 스튜어드 |
| 상담 | Consultation | 상담, 문의, 상담 이력 관련 용어 | 데이터 스튜어드 |
| 공통 | Common | 여러 도메인에서 공통으로 사용하는 코드, 상태, 일자, 금액 용어 | 관리자 |

## 3. 사용자 역할

| 역할 | 역할 코드 | 설명 |
|---|---|---|
| 관리자 | ADMIN | 시스템 설정, 권한 관리, 전체 용어 정책을 관리한다. |
| 데이터 스튜어드 | DATA_STEWARD | 담당 도메인의 표준 용어 의미, 승인, 폐기를 책임진다. |
| 개발 리더 | DEVELOPMENT_LEAD | 개발 산출물 영향도와 API, DB, 코드 적용 가능성을 검토한다. |
| 아키텍트 | ARCHITECT | 도메인 간 충돌, 공통 모델, 기술 표준 정합성을 검토한다. |
| 기획자 | PLANNER | 요구사항과 기획서에서 사용할 용어를 검색하고 신규 후보를 신청한다. |
| 개발자 | DEVELOPER | API, DB, 코드, 테스트에서 표준 용어를 사용하고 검증 결과를 수정한다. |
| 운영자 | OPERATOR | 배포 후 용어 사용 현황, 폐기 용어 사용, 검증 리포트를 모니터링한다. |
| 조회자 | VIEWER | 표준 용어와 검증 결과를 조회한다. |

## 4. 권한 매트릭스

권한 표기는 다음 기준을 사용한다.

- `R`: 조회
- `C`: 생성
- `U`: 수정
- `D`: 삭제 또는 폐기
- `A`: 승인
- `E`: 실행
- `-`: 권한 없음

| 기능 | ADMIN | DATA_STEWARD | DEVELOPMENT_LEAD | ARCHITECT | PLANNER | DEVELOPER | OPERATOR | VIEWER |
|---|---|---|---|---|---|---|---|---|
| 용어 검색 | R | R | R | R | R | R | R | R |
| 용어 상세 조회 | R | R | R | R | R | R | R | R |
| 용어 등록 | C | C | C | C | C | C | - | - |
| 용어 수정 | U | U | U | U | - | - | - | - |
| 용어 승인 | A | A | - | A | - | - | - | - |
| 용어 폐기 | D | D | - | A | - | - | - | - |
| 표현 매핑 등록 | C | C | C | C | - | C | - | - |
| 표현 매핑 수정 | U | U | U | U | - | - | - | - |
| 별칭/금지어 등록 | C | C | C | C | C | C | - | - |
| 별칭/금지어 수정 | U | U | U | U | - | - | - | - |
| 신규 용어 후보 등록 | C | C | C | C | C | C | - | - |
| 신규 용어 후보 검토 | A | A | A | A | - | - | - | - |
| 후보의 표준 용어 승격 | A | A | - | A | - | - | - | - |
| 기획서 용어 검토 | E | E | E | E | E | E | R | R |
| OpenAPI 검증 | E | E | E | E | R | E | R | R |
| DDL 검증 | E | E | E | E | - | E | R | R |
| 코드 검증 | E | E | E | E | - | E | R | R |
| PR 검증 결과 조회 | R | R | R | R | R | R | R | R |
| PR 검증 정책 변경 | U | - | U | A | - | - | - | - |
| 사용자 권한 관리 | U | - | - | - | - | - | - | - |

## 5. 승인 프로세스 참여자

### 5.1 신규 용어 승인

```text
기획자 또는 개발자
  -> 신규 용어 후보 등록
  -> 데이터 스튜어드 의미 검토
  -> 개발 리더 기술 산출물 검토
  -> 아키텍트 도메인 충돌 검토
  -> 데이터 스튜어드 또는 관리자 승인
  -> Approved 상태 전환
```

| 단계 | 책임 역할 | 주요 검토 항목 |
|---|---|---|
| 후보 등록 | PLANNER, DEVELOPER | 용어명, 사용 맥락, 사용 예정 산출물 |
| 의미 검토 | DATA_STEWARD | 업무 정의, 유사 용어, 중복 여부 |
| 기술 검토 | DEVELOPMENT_LEAD | DB 컬럼명, API 필드명, 코드 변수명, 타입 |
| 구조 검토 | ARCHITECT | 도메인 충돌, 공통 용어 여부, 확장성 |
| 최종 승인 | DATA_STEWARD, ADMIN | Approved 전환 가능 여부 |

### 5.2 용어 수정 승인

| 수정 유형 | 필수 승인 역할 | 비고 |
|---|---|---|
| 설명 수정 | DATA_STEWARD | 낮은 위험도 |
| 별칭 추가 | DATA_STEWARD | 낮은 위험도 |
| API 필드명 수정 | DATA_STEWARD, DEVELOPMENT_LEAD | 중간 위험도 |
| 영문 약어 수정 | DATA_STEWARD, DEVELOPMENT_LEAD, ARCHITECT | 높은 위험도 |
| 물리 타입 수정 | DATA_STEWARD, DEVELOPMENT_LEAD, ARCHITECT | 높은 위험도 |
| 자릿수 수정 | DATA_STEWARD, DEVELOPMENT_LEAD | 높은 위험도 |
| 용어 폐기 | DATA_STEWARD, ARCHITECT 또는 ADMIN | 높은 위험도 |

### 5.3 용어 폐기 승인

용어 폐기는 기존 DB, API, 코드, 기획서, 테스트에 영향을 줄 수 있으므로 영향도 분석이 선행되어야 한다.

```text
폐기 요청
  -> 영향도 분석
  -> 대체 용어 지정
  -> 하위 호환성 검토
  -> 적용 일정 확정
  -> 폐기 승인
  -> Deprecated 상태 전환
```

## 6. 권한 검증이 필요한 API

서버 API는 OpenAPI Spec에 security requirement와 권한 설명을 포함해야 한다. 실제 구현은 Spring Security 또는 동등한 권한 검증 계층에서 수행한다.

| API | 필요 권한 |
|---|---|
| `POST /terms` | 용어 등록 권한 |
| `PUT /terms/{termId}` | 용어 수정 권한 |
| `POST /terms/{termId}/approve` | 용어 승인 권한 |
| `POST /terms/{termId}/deprecate` | 용어 폐기 권한 |
| `POST /terms/{termId}/expressions` | 표현 매핑 등록 권한 |
| `PUT /terms/{termId}/expressions/{expressionId}` | 표현 매핑 수정 권한 |
| `POST /terms/{termId}/aliases` | 별칭/금지어 등록 권한 |
| `PUT /terms/{termId}/aliases/{aliasId}` | 별칭/금지어 수정 권한 |
| `POST /candidates` | 신규 용어 후보 등록 권한 |
| `POST /candidates/{candidateId}/review` | 신규 용어 후보 검토 권한 |
| `POST /candidates/{candidateId}/promote` | 후보 승격 권한 |
| `POST /reviews/document` | 기획서 용어 검토 실행 권한 |
| `POST /reviews/openapi` | OpenAPI 검증 실행 권한 |
| `POST /reviews/ddl` | DDL 검증 실행 권한 |
| `POST /reviews/code` | 코드 검증 실행 권한 |
| `POST /reviews/pr` | PR 검증 실행 권한 |
| `PUT /policies/review-rules` | 검증 정책 변경 권한 |
| `PUT /users/{userId}/roles` | 사용자 권한 관리 권한 |

## 7. OpenAPI 반영 기준

OpenAPI Spec 작성 시 다음 내용을 반영한다.

- 모든 보호 API에는 security scheme을 연결한다.
- 관리자 전용 API에는 `x-required-roles` 확장 필드를 사용한다.
- 승인 API에는 요청자의 역할과 대상 도메인 권한을 검증한다.
- 도메인 소유 권한이 필요한 API는 `domainName` 또는 `termId`를 기준으로 소유 도메인을 조회해 검증한다.

예시:

```yaml
x-required-roles:
  - ADMIN
  - DATA_STEWARD
```

## 8. MVP 적용 범위

MVP에서는 인증 시스템을 완전히 구현하지 않더라도 다음 기준을 API Spec과 서비스 계층에 반영한다.

- 역할 enum 정의
- 보호 API의 권한 요구사항 문서화
- 승인, 폐기, 후보 승격 API의 권한 검증 hook 설계
- 도메인 소유 권한 검증을 위한 service interface 설계

