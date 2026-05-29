# 프롬프트 템플릿 구현

## 구현 범위

- `PT-VIBE-001`: 바이브코딩용 표준 개발 규칙 프롬프트
- `PT-PLAN-001`: 기획서 작성용 표준화 프롬프트
- 템플릿 목록/상세/버전 조회 API
- 템플릿 미리보기 API
- 템플릿 변수 목록과 버전 정책
- 템플릿 변경 이력

## 저장 구조

MVP3에서는 `PromptTemplateRepository`가 인메모리 seed 데이터로 템플릿을 제공한다. 이후 PostgreSQL 전환 시 `PROMPT_TEMPLATE`, `PROMPT_TEMPLATE_VERSION`, `PROMPT_TEMPLATE_HISTORY` 테이블로 분리한다.

## 미리보기 방식

`PromptTemplateService`는 `{{requirementText}}`, `{{termMappings}}`, `{{candidateTerms}}`, `{{additionalContext}}` 플레이스홀더를 요청 값으로 치환한다.

`termMappings`는 데이터 사전 검색 결과를 Markdown 표로 변환한다. `candidateTerms`는 신규 용어 후보 목록을 Markdown 표로 변환한다.

## 검증

`PromptTemplateApiContractTest`에서 다음을 검증한다.

- `GET /prompt-templates?type=VibeCoding&status=Active`
- `GET /prompt-templates/PT-VIBE-001`
- `GET /prompt-templates/PT-VIBE-001/versions`
- `POST /prompt-templates/PT-VIBE-001/preview`
- 존재하지 않는 템플릿의 `404 PROMPT_TEMPLATE_NOT_FOUND`
