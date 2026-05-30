# Task 04 결과: 용어 변경 영향도 분석

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, OpenAPI-first 원칙에 따라 Impact Analysis API를 정의하고 구현했다.

## OpenAPI 변경

- `openapi/openapi.yaml`
  - `/impact/terms/{termId}`
  - `ImpactAnalysisResponse`
  - `ImpactChangeType`
  - `ImpactRiskLevel`
  - `ImpactTarget`
  - `ImpactRecommendation`
- `openapi/paths/impact.yaml`
- `openapi/schemas/impact.yaml`

## Generator 산출물

- 백엔드
  - `generated/backend/src/main/kotlin/com/aulms/api/ImpactApi.kt`
  - `generated/backend/src/main/kotlin/com/aulms/model/Impact*.kt`
- 프런트
  - `generated/frontend/api/impact-api.ts`
  - `generated/frontend/model/impact*.ts`

## 백엔드 구현

- `ImpactApiController`
- `ImpactAnalysisService`
- `ImpactAnalysisApiContractTest`

## 프런트 구현

- `apps/frontend/src/lib/term-api.ts`
  - 생성된 `ImpactApi` 클라이언트 연결
  - 백엔드 미실행 시 고객번호 샘플 응답 제공
- `apps/frontend/src/app/page.tsx`
  - 용어 상세 화면에 영향도 분석 패널 추가
  - 변경 유형 선택, 위험도, 영향 대상, 권장 조치 표시

## 문서

- `docs/api/impact-analysis-api-spec.md`
- `docs/api/impact-analysis-implementation.md`

## 완료 기준 확인

- `customerNumber` 변경 시 관련 DB 컬럼 `CUST_NO` 반환
- API 필드 `customerNumber` 반환
- DTO 필드 `customerNumber` 반환
- 기획서 표현 `고객번호 기획서 표현` 반환
- 테스트 케이스 `고객번호 필수 검증` 반환
- 위험도와 권장 조치 반환

## 검증 명령

```bash
ruby -e "require 'yaml'; %w[openapi/openapi.yaml openapi/paths/impact.yaml openapi/schemas/impact.yaml].each { |f| YAML.load_file(f); puts \"OK #{f}\" }"
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
- `apps/backend` 기준 `gradle test` 성공
- `apps/frontend` 기준 `npm run typecheck` 성공
