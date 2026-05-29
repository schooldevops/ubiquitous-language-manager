# 프롬프트 템플릿 API Spec

## 목적

바이브코딩과 기획서 작성 도구가 사내 데이터 사전 기반 프롬프트를 조회하고, 데이터 사전 검색 결과를 변수로 주입한 최종 프롬프트를 미리보기할 수 있게 한다.

## OpenAPI 경로

- `GET /prompt-templates`: 템플릿 목록 조회
- `GET /prompt-templates/{templateId}`: 템플릿 상세 조회
- `GET /prompt-templates/{templateId}/versions`: 템플릿 버전 목록 조회
- `POST /prompt-templates/{templateId}/preview`: 변수 주입 미리보기

## 기본 템플릿

| 템플릿 ID | 유형 | 이름 | 버전 | 상태 |
|---|---|---|---|---|
| PT-VIBE-001 | VibeCoding | 사내 데이터 사전 기반 개발 규칙 | 1.0.0 | Active |
| PT-PLAN-001 | Planning | 데이터 사전 기반 기획서 작성 규칙 | 1.0.0 | Active |

## 변수

| 변수 | 출처 | 필수 | 설명 |
|---|---|---|---|
| requirementText | UserInput | 예 | 사용자가 입력한 요구사항 또는 기획서 초안 |
| termMappings | DictionarySearch | 아니오 | 데이터 사전 검색으로 확정된 표준 용어 매핑 |
| candidateTerms | CandidateWorkflow | 아니오 | 데이터 사전에 없어 후보로 분리한 신규 용어 |
| additionalContext | System | 아니오 | 기술 스택, 산출물 범위 등 추가 맥락 |

## 버전 정책

- `MAJOR`: 출력 구조 또는 필수 규칙 변경
- `MINOR`: 규칙, 변수, 섹션 추가
- `PATCH`: 문구 보완, 예시 수정

## API-first 기준

프롬프트 템플릿 API는 `openapi/paths/prompt-templates.yaml`과 `openapi/schemas/prompt-template.yaml`에 먼저 정의하고, OpenAPI Generator로 서버 stub과 프런트 axios client를 생성한다.
