# Candidate API Implementation

## 1. 목적

미등록 용어를 신규 후보로 등록하고, 데이터 스튜어드 검토 후 표준 용어로 승격하는 MVP workflow 구현 결과를 정리한다.

## 2. 구현 파일

| 파일 | 설명 |
|---|---|
| `apps/backend/src/main/kotlin/com/aulms/candidate/CandidateApiController.kt` | 생성된 `CandidateApi` 인터페이스 구현 |
| `apps/backend/src/main/kotlin/com/aulms/candidate/CandidateService.kt` | 후보 등록, 조회, 검토, 승격 workflow 구현 |
| `apps/backend/src/main/kotlin/com/aulms/candidate/CandidateRepository.kt` | 후보와 상태 변경 이력 in-memory 저장소 |
| `apps/backend/src/test/kotlin/com/aulms/candidate/CandidateApiContractTest.kt` | Candidate API contract test |
| `apps/frontend/src/lib/term-api.ts` | 생성된 `CandidateApi` axios client wrapper |
| `apps/frontend/src/app/page.tsx` | 신규 용어 후보 등록, 승인, 승격 화면 |

## 3. 처리 흐름

1. `POST /candidates`로 후보를 등록한다.
2. 후보 등록 시 같은 도메인의 기존 표준 용어를 유사 용어로 함께 반환한다.
3. `POST /candidates/{candidateId}/review`로 후보를 승인, 반려, 수정 요청 상태로 변경한다.
4. 승인된 후보는 `POST /candidates/{candidateId}/promote`로 표준 용어로 승격한다.
5. 승격 시 `TERM_MASTER`에 해당하는 `Term`과 `TERM_EXPRESSION`에 해당하는 표현 매핑을 생성한다.
6. 후보 상태 변경은 `CandidateHistory`에 누적한다.

## 4. 대표 검증 케이스

| 단계 | 기대 결과 |
|---|---|
| 후보 등록 | `Draft` 후보 생성, 유사 용어 반환 |
| 후보 목록 조회 | 등록 후보 조회 |
| 후보 승인 | `Approved` 상태와 이력 기록 |
| 후보 승격 | `Promoted` 상태, `Approved` 표준 용어 생성 |
| 표준 용어 조회 | 승격된 용어와 API/DB/코드 표현 조회 |
