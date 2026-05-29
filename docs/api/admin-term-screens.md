# 용어 관리자 MVP 화면

## 1. 목적

관리자가 표준 용어를 검색, 조회, 등록, 수정할 수 있는 MVP 화면을 구현한다.

## 2. 구현 위치

| 파일 | 설명 |
|---|---|
| `apps/frontend/src/app/page.tsx` | 용어 검색, 상세, 등록/수정 화면 |
| `apps/frontend/src/lib/term-api.ts` | OpenAPI Generator 기반 API client 래퍼 |
| `apps/frontend/src/app/globals.css` | Tailwind CSS 4 기반 전역 스타일 |
| `generated/frontend` | OpenAPI Generator `typescript-axios` client |
| `scripts/openapi-generate-frontend.sh` | Frontend client 생성 스크립트 |

## 3. 구현한 화면 기능

- 한글명, 영문명, 약어, API 필드명 검색 입력
- 도메인 필터
- 상태 필터
- 검색 결과 테이블
- 용어 상세 표시
- 업무 정의, 사용 맥락, 물리 타입, 자릿수, 소수점 표시
- DB 컬럼명, API 필드명, 코드 변수명, UI 라벨 표시
- 별칭, 유사어, 금지어 표시
- 신규 용어 등록 폼
- 선택 용어 기반 수정 폼
- 상태값 Draft, Reviewing, Approved, Deprecated, Rejected 표시

## 4. API 연동 기준

화면은 직접 axios request shape을 만들지 않고 `generated/frontend`의 `TermApi`, `ExpressionApi`, `AliasApi`를 사용한다.

백엔드가 실행 중이면 실제 API를 호출한다. 백엔드가 실행 중이 아니면 MVP 화면 검토를 위해 고객번호 샘플 데이터로 폴백한다.

## 5. 완료 기준

- 고객번호를 상세 조회할 수 있다.
- 고객ID 별칭을 UI에서 확인할 수 있다.
- OpenAPI Generator로 생성된 axios TypeScript client를 통해 API 호출 경계를 구성했다.

