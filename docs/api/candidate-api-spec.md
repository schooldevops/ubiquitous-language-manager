# Candidate API Spec

## 1. 목적

데이터 사전에 없는 용어를 신규 후보로 등록하고, 검토 후 표준 용어로 승격하는 Candidate API 계약을 정의한다.

## 2. 작성 파일

| 파일 | 설명 |
|---|---|
| `openapi/openapi.yaml` | Candidate API path와 schema 참조 추가 |
| `openapi/paths/candidates.yaml` | 후보 등록, 조회, 검토, 승격 API 정의 |
| `openapi/schemas/candidate.yaml` | 후보 workflow 요청/응답 schema 정의 |

## 3. 정의 API

| Method | Path | Operation ID | 설명 |
|---|---|---|---|
| POST | `/candidates` | `createCandidate` | 신규 용어 후보 등록 |
| GET | `/candidates` | `listCandidates` | 신규 용어 후보 목록 조회 |
| GET | `/candidates/{candidateId}` | `getCandidate` | 신규 용어 후보 상세 조회 |
| POST | `/candidates/{candidateId}/review` | `reviewCandidate` | 신규 용어 후보 검토 |
| POST | `/candidates/{candidateId}/promote` | `promoteCandidate` | 후보를 표준 용어로 승격 |

## 4. 상태

| 상태 | 의미 |
|---|---|
| `Draft` | 후보 등록됨 |
| `Reviewing` | 수정 요청 또는 검토 중 |
| `Approved` | 승격 가능 상태 |
| `Rejected` | 반려됨 |
| `Promoted` | 표준 용어로 승격됨 |
