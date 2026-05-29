# Task 03 결과: AI 프롬프트 템플릿 관리

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, OpenAPI-first 원칙에 따라 프롬프트 템플릿 관리 기능을 구현했다.

## OpenAPI 변경

- `openapi/openapi.yaml`
  - `PromptTemplate` 태그 추가
  - `/prompt-templates`
  - `/prompt-templates/{templateId}`
  - `/prompt-templates/{templateId}/versions`
  - `/prompt-templates/{templateId}/preview`
- `openapi/paths/prompt-templates.yaml`
  - 템플릿 목록 조회
  - 템플릿 상세 조회
  - 템플릿 버전 목록 조회
  - 템플릿 미리보기
- `openapi/schemas/prompt-template.yaml`
  - `PromptTemplateType`
  - `PromptTemplateStatus`
  - `PromptTemplate`
  - `PromptTemplateSummary`
  - `PromptTemplateVariable`
  - `PromptTemplateVersion`
  - `PromptTemplateHistory`
  - `PromptTemplatePreviewRequest`
  - `PromptTemplatePreviewResponse`

## Generator 산출물

- 백엔드 Spring Boot Kotlin server stub 생성
  - `generated/backend/src/main/kotlin/com/aulms/api/PromptTemplateApi.kt`
  - `generated/backend/src/main/kotlin/com/aulms/model/PromptTemplate*.kt`
- 프런트 TypeScript axios client 생성
  - `generated/frontend/api/prompt-template-api.ts`
  - `generated/frontend/model/prompt-template*.ts`

## 백엔드 구현

- `PromptTemplateRepository`
  - `PT-VIBE-001` 등록
  - `PT-PLAN-001` 등록
  - 템플릿 변수, 버전, 변경 이력 seed 구성
- `PromptTemplateService`
  - 목록 조회
  - 상세 조회
  - 버전 조회
  - 미리보기 변수 주입
- `PromptTemplateApiController`
  - OpenAPI generator server stub 구현
- `PromptTemplateNotFoundException`
  - 존재하지 않는 템플릿 404 처리
- `ApiExceptionHandler`
  - `PROMPT_TEMPLATE_NOT_FOUND` 응답 추가

## 문서

- `docs/api/prompt-template-api-spec.md`
- `docs/api/prompt-template-implementation.md`

## 검증 기준

- AI 개발 도구가 `PT-VIBE-001` 프롬프트를 조회할 수 있다.
- 기획서 작성 도구가 `PT-PLAN-001` 프롬프트를 조회할 수 있다.
- 템플릿 버전 `1.0.0`을 추적할 수 있다.
- 데이터 사전 검색 결과를 `termMappings` 변수로 주입해 최종 프롬프트를 생성할 수 있다.

## 검증 명령

```bash
ruby -e "require 'yaml'; %w[openapi/openapi.yaml openapi/paths/prompt-templates.yaml openapi/schemas/prompt-template.yaml].each { |f| YAML.load_file(f); puts \"OK #{f}\" }"
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
