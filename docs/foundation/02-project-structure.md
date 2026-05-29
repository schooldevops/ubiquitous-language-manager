# 프로젝트 구조 정의서

## 1. 목표 구조

```text
AULMS/
  apps/
    backend/
    frontend/
  docs/
    api/
    dictionary/
    foundation/
  generated/
    backend/
    frontend/
  infra/
    local/
  openapi/
    openapi.yaml
    paths/
    schemas/
  scripts/
  mvp0-tasks/
  mvp1-tasks/
  mvp2-tasks/
  mvp3-tasks/
  mvp4-tasks/
  mvp5-tasks/
```

## 2. 디렉토리 역할

| 경로 | 역할 |
|---|---|
| `apps/backend` | Spring Boot Kotlin Backend 애플리케이션 |
| `apps/frontend` | Next.js Frontend 애플리케이션 |
| `docs/api` | API 설계와 운영 문서 |
| `docs/dictionary` | 유비쿼터스 언어와 데이터 딕셔너리 문서 |
| `docs/foundation` | 프로젝트 기반, 스택, 로컬 환경 문서 |
| `generated/backend` | OpenAPI Generator로 생성한 Backend server stub |
| `generated/frontend` | OpenAPI Generator로 생성한 Frontend axios client |
| `infra/local` | 로컬 개발용 PostgreSQL 등 인프라 설정 |
| `openapi` | OpenAPI Spec 원본 |
| `openapi/paths` | API path 분할 파일 |
| `openapi/schemas` | 공통 schema 분할 파일 |
| `scripts` | 코드 생성, 검증, 로컬 실행 보조 스크립트 |

## 3. OpenAPI 관리 위치

- 원본 진입점: `openapi/openapi.yaml`
- Path 분할 파일: `openapi/paths/*.yaml`
- Schema 분할 파일: `openapi/schemas/*.yaml`
- 서버 stub 생성 위치: `generated/backend`
- 프론트 client 생성 위치: `generated/frontend`

## 4. 구현 순서 원칙

1. `docs/dictionary`에 개발 대상 용어를 정의한다.
2. `openapi/openapi.yaml`과 분할 Spec을 작성한다.
3. OpenAPI lint를 통과시킨다.
4. OpenAPI Generator로 Backend server stub을 생성한다.
5. OpenAPI Generator로 Frontend axios TypeScript client를 생성한다.
6. 생성된 타입과 인터페이스를 기준으로 Backend를 구현한다.
7. 생성된 client를 기준으로 Frontend를 구현한다.
8. contract test로 Spec과 구현의 일치 여부를 검증한다.

