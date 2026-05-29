# Task 04 결과: AI 산출물 생성 지원 API

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, OpenAPI-first 원칙에 따라 AI 산출물 생성 지원 API를 구현했다.

## OpenAPI 변경

- `openapi/openapi.yaml`
  - `AI` 태그 추가
  - `/ai/development-assist` path 추가
  - AI Assist schema component 추가
- `openapi/paths/ai.yaml`
  - `POST /ai/development-assist`
- `openapi/schemas/ai.yaml`
  - `DevelopmentAssistRequest`
  - `DevelopmentAssistResponse`
  - `AssistTargetArtifact`
  - `ExtractedBusinessConcept`
  - `AssistTermMapping`
  - `GeneratedArtifact`
  - `AssistCandidateTerm`
  - `StandardViolationWarning`

## Generator 산출물

- 백엔드 Spring Boot Kotlin server stub 생성
  - `generated/backend/src/main/kotlin/com/aulms/api/AIApi.kt`
  - `generated/backend/src/main/kotlin/com/aulms/model/DevelopmentAssist*.kt`
  - `generated/backend/src/main/kotlin/com/aulms/model/Assist*.kt`
- 프런트 TypeScript axios client 생성
  - `generated/frontend/api/aiapi.ts`
  - `generated/frontend/model/development-assist*.ts`
  - `generated/frontend/model/assist*.ts`

## 백엔드 구현

- `AIApiController`
  - OpenAPI generator server stub 구현
- `DevelopmentAssistService`
  - 업무 개념 추출
  - 정확 검색, 유사어 검색, 의미 검색 순차 적용
  - 표준 용어 매핑 생성
  - 신규 용어 후보 생성
  - Rule Engine 표준 위반 경고 생성
  - DTO, OpenAPI Schema, SQL 예시 생성

## 문서

- `docs/api/ai-development-assist-api-spec.md`
- `docs/api/ai-development-assist-implementation.md`

## 검증 기준

- `고객별 주문 내역을 조회하는 API 만들어줘.` 입력 시 다음 표준 용어 매핑 반환
  - 고객번호
  - 주문번호
  - 주문일자
  - 주문금액
  - 주문상태코드
- 매핑 결과는 DB 컬럼, API 필드, 코드 변수명을 포함한다.
- 데이터 사전에 없는 용어는 신규 후보로 분리한다.
- DTO, OpenAPI Schema, SQL 예시를 반환한다.

## 검증 명령

```bash
ruby -e "require 'yaml'; %w[openapi/openapi.yaml openapi/paths/ai.yaml openapi/schemas/ai.yaml].each { |f| YAML.load_file(f); puts \"OK #{f}\" }"
openapi-generator-cli validate -i openapi/openapi.yaml
./scripts/openapi-generate-backend.sh
./scripts/openapi-generate-frontend.sh
gradle test
npm run typecheck
```

## 검증 결과

- OpenAPI YAML 파싱 성공
- OpenAPI validation 성공
  - 기존 경고: `UserRole`, `RelationshipType` 미사용 모델
- 백엔드 OpenAPI generator 실행 성공
- 프런트 OpenAPI generator 실행 성공
- `gradle test` 성공
- `npm run typecheck` 성공
