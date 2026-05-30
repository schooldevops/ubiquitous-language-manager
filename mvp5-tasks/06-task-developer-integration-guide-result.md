# Task 06 결과: 개발자 연동 가이드 작성

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, 개발자가 데이터 사전 기반 검증 도구를 로컬 개발, PR, CI 흐름에 적용할 수 있도록 가이드를 작성했다.

## 산출물

- `docs/api/developer-integration-guide.md`
- `docs/api/cli-usage-examples.md`
- `docs/api/pr-ci-troubleshooting.md`

## 포함 내용

- OpenAPI Spec 위치와 사용법
- OpenAPI generator 실행 방법
- 생성된 TypeScript axios client 사용법
- 생성된 Kotlin Spring server stub 위치
- Review API 사용법
- 로컬 CLI 검증 명령
- OpenAPI YAML 검증 예시
- DDL 검증 예시
- DTO 검증 예시
- PR 코멘트 예시
- CI 실패 대응 절차
- 신규 용어 후보 등록 절차
- Deprecated/Forbidden 용어 대응 절차

## 완료 기준 확인

- 개발자가 표준 위반을 로컬에서 재현하고 수정할 수 있다.
- OpenAPI Spec 기준으로 서버 API와 클라이언트를 사용할 수 있다.
- CI 실패 시 우선순위와 수정 절차를 확인할 수 있다.
- 신규 용어 후보와 Deprecated/Forbidden 표현 대응 절차를 확인할 수 있다.

## 검증

문서 산출물 태스크라 빌드/테스트는 실행하지 않았다.
