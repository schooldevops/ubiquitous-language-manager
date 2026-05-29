# Task 01 완료 결과: 프로젝트 기반 정리

## 수행 일자

2026-05-27

## 확인한 규칙

`rules.md`를 확인했고 다음 기준을 반영했다.

- Backend는 Kotlin, Spring Boot, OpenAPI, QueryDSL SQL, PostgreSQL을 사용한다.
- Frontend는 React, Next.js, Tailwind CSS 4, Base UI를 사용한다.
- 서버 API는 항상 OpenAPI Spec을 먼저 작성한다.
- OpenAPI Generator로 Backend Spring Boot Kotlin server stub을 생성한 뒤 구현한다.
- OpenAPI Generator로 Frontend axios TypeScript client를 생성한 뒤 구현한다.
- 이 시스템 자체도 유비쿼터스 언어와 데이터 딕셔너리를 먼저 작성한 뒤 개발한다.
- 매 단계마다 변경 내용을 확인받고 승인 후 다음 단계로 진행한다.

## 생성한 산출물

| 산출물 | 파일 |
|---|---|
| 기술 스택 결정 기록 | `docs/foundation/01-technology-stack-decisions.md` |
| 프로젝트 구조 정의서 | `docs/foundation/02-project-structure.md` |
| API-first 개발 원칙 | `docs/foundation/03-api-first-development-rules.md` |
| API 요청/응답 공통 포맷 | `docs/foundation/04-api-common-format.md` |
| 로컬 개발 환경 가이드 | `docs/foundation/05-local-development-guide.md` |
| 초기 유비쿼터스 언어 사전 | `docs/dictionary/initial-ubiquitous-language.md` |

## 생성한 디렉터리

```text
apps/backend
apps/frontend
docs/api
docs/dictionary
docs/foundation
generated/backend
generated/frontend
infra/local
openapi/paths
openapi/schemas
scripts
```

## 결정 사항

- OpenAPI 원본 진입점은 `openapi/openapi.yaml`로 둔다.
- path 분할 파일은 `openapi/paths/*.yaml`로 둔다.
- schema 분할 파일은 `openapi/schemas/*.yaml`로 둔다.
- Backend server stub 생성 결과는 `generated/backend`에 둔다.
- Frontend axios client 생성 결과는 `generated/frontend`에 둔다.
- 실제 Backend 구현은 `apps/backend`에 둔다.
- 실제 Frontend 구현은 `apps/frontend`에 둔다.

## 완료 기준 충족 여부

| 완료 기준 | 상태 |
|---|---|
| 개발자가 같은 방식으로 로컬 환경을 구성할 수 있다 | 충족 |
| OpenAPI Spec 파일 위치와 작성 규칙이 확정되어 있다 | 충족 |
| 서버 API 개발 전에 OpenAPI Spec을 작성해야 한다는 기준이 명확하다 | 충족 |

## 다음 단계

사용자 승인 후 `mvp0-tasks/02-task-domain-and-role-definition.md`를 진행한다.

