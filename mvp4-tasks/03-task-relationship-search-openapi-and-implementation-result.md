# Task 03 결과: 관계 검색 API 및 구현

## 수행 내용

`rules.md`와 `caveman.md`를 확인한 뒤, OpenAPI-first 원칙에 따라 Relationship Search API를 정의하고 구현했다.

## OpenAPI 변경

- `openapi/openapi.yaml`
  - `/relationships/terms/{termId}`
  - `/relationships/domains/{domainName}/terms`
  - `/relationships/columns/{columnName}/systems`
  - `/relationships/deprecated`
- `openapi/paths/relationships.yaml`
- `openapi/schemas/relationship.yaml`

## Generator 산출물

- 백엔드
  - `generated/backend/src/main/kotlin/com/aulms/api/RelationshipApi.kt`
  - `generated/backend/src/main/kotlin/com/aulms/model/Relationship*.kt`
  - `generated/backend/src/main/kotlin/com/aulms/model/ColumnSystemUsage*.kt`
  - `generated/backend/src/main/kotlin/com/aulms/model/DeprecatedUsage*.kt`
- 프런트
  - `generated/frontend/api/relationship-api.ts`
  - `generated/frontend/model/relationship*.ts`

## 백엔드 구현

- `RelationshipApiController`
- `RelationshipSearchService`
- 고객 샘플 용어 추가
  - 고객명
  - 고객상태코드
- 고객번호 관계 추가
  - 고객번호 `usedWith` 고객명
  - 고객번호 `usedWith` 고객상태코드
  - 고객번호 `usedWith` 주문번호

## 문서

- `docs/api/relationship-search-api-spec.md`
- `docs/api/relationship-search-implementation.md`

## 완료 기준 확인

- 고객번호와 함께 쓰이는 고객명, 고객상태코드, 주문번호 조회 가능
- `CUST_NO`를 사용하는 시스템 조회 가능
- `CUST_ID` 금지어 사용 위치와 권장 표현 `CUST_NO` 조회 가능

## 검증 명령

```bash
ruby -e "require 'yaml'; %w[openapi/openapi.yaml openapi/paths/relationships.yaml openapi/schemas/relationship.yaml].each { |f| YAML.load_file(f); puts \"OK #{f}\" }"
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
