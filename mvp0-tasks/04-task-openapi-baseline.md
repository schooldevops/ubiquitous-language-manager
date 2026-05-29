# Task 04: OpenAPI Baseline 작성

## 목표

서버 API 개발의 기준이 되는 OpenAPI Baseline을 작성한다.

## 범위

- OpenAPI 버전 확정
- 공통 스키마 정의
- 공통 에러 응답 정의
- 인증/권한 스키마 placeholder 정의
- API 태그 구조 정의

## 상세 태스크

1. OpenAPI 버전을 `3.0.x` 또는 `3.1.x` 중 하나로 확정한다.
2. `openapi.yaml` 또는 도메인별 분할 Spec 구조를 정한다.
3. `ErrorResponse` 공통 스키마를 정의한다.
4. `PageResponse` 공통 스키마를 정의한다.
5. `ValidationIssue` 공통 스키마를 정의한다.
6. `TermStatus` enum을 정의한다.
7. `ExpressionType` enum을 정의한다.
8. `AliasType` enum을 정의한다.
9. `RelationshipType` enum을 정의한다.
10. Term, Search, Review, Candidate, Governance API 태그를 정의한다.
11. OpenAPI lint 규칙을 정의한다.
12. Backend 구현은 OpenAPI Spec과 불일치하지 않도록 검증 절차를 정의한다.

## 산출물

- OpenAPI Baseline Spec
- 공통 API 스키마
- API 태그 구조
- OpenAPI 검증 규칙

## 완료 기준

- 이후 서버 API 태스크가 OpenAPI Spec을 기준으로 개발될 수 있다.
- 공통 응답과 에러 포맷이 Spec에 정의되어 있다.

