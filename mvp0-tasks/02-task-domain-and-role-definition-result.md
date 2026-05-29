# Task 02 완료 결과: 도메인, 사용자 역할, 권한 정의

## 수행 일자

2026-05-27

## 확인한 규칙

`rules.md`를 확인했고 다음 기준을 반영했다.

- Backend API는 OpenAPI Spec을 먼저 정의해야 한다.
- OpenAPI Spec에는 보호 API의 권한 요구사항을 반영해야 한다.
- 유비쿼터스 언어 관리 시스템 자체도 데이터 딕셔너리를 작성한 뒤 개발해야 한다.
- 각 단계 완료 후 사용자 확인과 승인을 받아야 한다.

## 생성 및 변경한 산출물

| 산출물 | 파일 |
|---|---|
| 도메인, 역할, 권한 모델 | `docs/foundation/06-domain-role-permission-model.md` |
| 용어 승인 프로세스 | `docs/foundation/07-approval-process.md` |
| 초기 유비쿼터스 언어 사전 보강 | `docs/dictionary/initial-ubiquitous-language.md` |

## 정의한 초기 도메인

- 고객
- 주문
- 결제
- 상품
- 계약
- 청구
- 상담
- 공통

## 정의한 사용자 역할

- ADMIN: 관리자
- DATA_STEWARD: 데이터 스튜어드
- DEVELOPMENT_LEAD: 개발 리더
- ARCHITECT: 아키텍트
- PLANNER: 기획자
- DEVELOPER: 개발자
- OPERATOR: 운영자
- VIEWER: 조회자

## 주요 결정 사항

- 용어 승인 최종 책임은 데이터 스튜어드 또는 관리자에게 둔다.
- 공통 도메인 용어와 도메인 충돌 가능 용어는 아키텍트 검토를 거친다.
- 용어 폐기는 영향도 분석과 대체 용어 지정 후 진행한다.
- 보호 API는 OpenAPI Spec에 security requirement와 `x-required-roles` 확장 필드를 사용해 권한 요구사항을 명시한다.
- MVP에서는 인증 시스템을 완전히 구현하지 않더라도 역할 enum, 권한 요구사항, 검증 hook을 먼저 설계한다.

## 완료 기준 충족 여부

| 완료 기준 | 상태 |
|---|---|
| 각 기능을 어떤 역할이 사용할 수 있는지 설명할 수 있다 | 충족 |
| 승인, 폐기, 후보 승격의 책임자가 명확하다 | 충족 |

## 다음 단계

사용자 승인 후 `mvp0-tasks/03-task-sample-data-preparation.md`를 진행한다.

