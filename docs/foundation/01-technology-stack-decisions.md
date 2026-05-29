# 기술 스택 결정 기록

## 1. 목적

본 문서는 데이터 사전 기반 유비쿼터스 랭기지 관리 시스템의 MVP 개발에 사용할 기술 스택을 확정한다.

## 2. Backend

| 항목 | 결정 |
|---|---|
| 언어 | Kotlin |
| 프레임워크 | Spring Boot |
| API 계약 | OpenAPI Spec |
| 서버 코드 생성 | OpenAPI Generator Spring Boot Kotlin server stub |
| DB 접근 | QueryDSL SQL |
| DBMS | PostgreSQL |
| API 문서 | OpenAPI YAML |
| 테스트 | Spring Boot Test, JUnit, Testcontainers |

## 3. Frontend

| 항목 | 결정 |
|---|---|
| 프레임워크 | Next.js |
| UI 라이브러리 | React |
| 스타일 | Tailwind CSS 4 |
| Headless UI | Base UI |
| API Client | OpenAPI Generator axios TypeScript client |
| 테스트 | Vitest, React Testing Library, Playwright |

## 4. Search, RAG, Graphify

| 영역 | MVP 결정 |
|---|---|
| 정확 검색 | PostgreSQL 기반 검색으로 시작 |
| 유사어 검색 | `TERM_ALIAS` 기반 검색으로 시작 |
| 의미 기반 검색 | 2차 단계에서 RAG 인덱스 도입 |
| RAG 저장소 | 추후 확정, OpenAPI 연동 경계 우선 정의 |
| Graphify | 4차 단계에서 관계 및 영향도 분석용으로 연동 |

## 5. 개발 원칙

- 서버 API는 반드시 OpenAPI Spec을 먼저 작성한 뒤 구현한다.
- Backend는 OpenAPI Generator로 생성한 Spring Boot Kotlin server stub을 기준으로 구현한다.
- Frontend는 OpenAPI Generator로 생성한 axios TypeScript client를 기준으로 API를 호출한다.
- 구현 코드의 요청/응답 모델은 OpenAPI Spec과 불일치하면 안 된다.
- 유비쿼터스 언어 관리 시스템 자체도 데이터 딕셔너리와 표준 용어를 먼저 정의한 뒤 개발한다.

