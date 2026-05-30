# Task 01 결과: 개발 산출물 검증기 설계

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, DDL, OpenAPI, DTO, Entity, SQL, 테스트, 화면 라벨을 검증하는 공통 구조를 설계했다.

## 산출물

- `docs/api/artifact-validator-design.md`
- `docs/api/artifact-parser-strategies.md`
- `docs/api/artifact-validator-cli-and-pr-format.md`

## 설계 범위

- 파일 유형별 parser 전략
- 검증 결과 공통 모델
- Rule Engine 연동 방식
- CLI 입력/출력 형식
- PR inline comment와 summary comment 변환 규칙
- CI exit code 정책
- 기본 제외 경로와 ignore 규칙

## OpenAPI 관련 결정

이번 태스크는 설계 문서 작성 범위라 서버 API를 추가하지 않았다.

다음 구현 태스크부터 서버 API가 필요한 경우 반드시 다음 순서로 진행한다.

1. OpenAPI spec 작성
2. backend Spring Kotlin server stub 생성
3. frontend TypeScript axios client 생성
4. 생성된 인터페이스 기반 구현

## 완료 기준 확인

- DDL parser 전략 정의 완료
- OpenAPI YAML parser 전략 정의 완료
- Kotlin/Java/TypeScript DTO parser 전략 정의 완료
- SQL Mapper parser 전략 정의 완료
- 테스트 코드 용어 추출 전략 정의 완료
- 화면 라벨 추출 전략 정의 완료
- 검증 결과 공통 모델 정의 완료
- Rule Engine 연동 인터페이스 정의 완료
- CLI 입력/출력 형식 정의 완료
- PR 리뷰 코멘트 변환 규칙 정의 완료

## 검증

문서 산출물 태스크이므로 빌드/테스트는 실행하지 않았다.
