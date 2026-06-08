# 용어 JSONL 업로드 → DB 등록 설계

작성일: 2026-06-04
대상: AULMS backend(Kotlin, Spring Boot, Querydsl-SQL, postgres) + frontend(Next.js)

## 1. 목적

용어 데이터를 담은 JSONL 파일(`docs/jsons/terms.jsonl` 형식)을 업로드하면, 각 줄(row)을
DB 구조에 맞게 등록한다. termId 가 있으면 update, 없으면 insert 하는 **upsert** 로 동작하고,
참조 테이블(표현/별칭/관계)도 모두 upsert 한다. 줄 단위 처리 결과(성공/실패, 오류, 등록/실패 일시)를
DB 에 기록하고 화면에 노출한다.

## 2. 범위

- **포함**: JSONL 파싱, 계층형 row(term + expressions + aliases + relationships) upsert,
  결과 추적 테이블, 업로드 REST API, 업로드/결과 조회 화면.
- **제외**: 비동기 배치 처리, 대용량 스트리밍, 권한/인증, 롤백/재처리 UI.
- **동작 모드**: **postgres 프로파일 전용**. 결과 추적 테이블이 postgres 에만 존재하므로
  업로드 기능은 `@Profile("postgres")` 빈으로만 활성화한다. memory 모드는 대상 아님.

## 3. 입력 형식

- JSONL(JSON Lines): 한 줄당 용어 1건. 계층형 객체.
- 줄 객체 스키마(요지):
  - `termId, termNumber, domainName, usageType, koreanName, englishName, englishAbbreviation,`
    `businessDefinition, usageContext?, physicalType, digits, decimalPoint, status, owner, version`
  - `expressions[]`: `{expressionId, expressionType, expressionValue, isStandard, language?, style?}`
  - `aliases`: `{synonyms[], forbidden[], deprecated[], needsContext[]}` 각 항목
    `{aliasId, aliasName, aliasType, recommendationAction, reason}`
  - `relationships[]`: `{relationshipId, relationshipType, targetTermId, description}`
    (`targetTerm` 요약은 무시 가능 — targetTermId 만 사용)
- `role`(primary/related) 같은 부가 필드는 등록에 영향 없음(무시).
- 일부 필드 누락 시(related 요약 row 등) 등록에 필수인 필드가 비면 해당 row 는 FAILED 처리.

## 4. 처리 방식 (동기 per-row, 2-pass)

업로드 1요청 내에서 순차 처리. 각 row 는 **개별 트랜잭션**으로 격리(한 줄 실패가 다른 줄을 롤백하지 않음).

1. **파싱**: 파일을 줄 단위로 읽어 각 줄을 JSON 으로 파싱. 파싱 실패 시 그 줄을 FAILED 로 기록(termId=null).
2. **pass1 — term 본문 upsert**: 각 row 의 term + expressions + aliases 를 upsert.
   - term: `termId` 존재 → update, 없으면 insert.
   - expression: `expressionId` 기준 upsert. alias: `aliasId` 기준 upsert.
3. **pass2 — 관계 upsert**: 모든 term 적재 후 relationships 를 upsert.
   - `relationshipId` 기준 upsert. source(=row termId), target(=targetTermId) 둘 다 존재할 때만 등록.
     target 부재 시 그 관계는 FAILED 사유에 포함.
4. **결과 기록**: row 별 `term_upload_row` 1건 기록(INSERTED/UPDATED/FAILED + 오류 + 일시).
5. **응답**: 집계(total/inserted/updated/failed) + row 목록.

upsert 구현은 Querydsl-SQL 로 PK 존재검사 후 insert/update 분기(ON CONFLICT 미사용, 프로파일 postgres 고정).

### 시퀀스 정합성
JSONL 이 명시 ID(expressionId 36.., aliasId A-000001, relationshipId 7..)를 포함하므로
업로드 후 각 시퀀스를 `setval(max)` 로 전진시켜 이후 신규 생성분과 충돌을 막는다
(term_expression_seq, term_alias_seq, term_relationship_id_seq).

## 5. 데이터 모델 — `term_upload_row` (Flyway V3, postgres)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| upload_row_id | BIGSERIAL PK | 결과 행 ID |
| upload_batch_id | VARCHAR(40) | 파일 업로드 1건 묶음. `UPL-yyyyMMdd-NNNN` |
| line_no | INT | JSONL 줄 번호(1-base) |
| term_id | VARCHAR(32) NULL | 파싱된 termId(파싱 실패 시 NULL) |
| status | VARCHAR(20) | `INSERTED` / `UPDATED` / `FAILED` |
| raw_json | TEXT | 원본 줄 |
| error_message | TEXT NULL | 실패 사유(성공 시 NULL) |
| registered_at | TIMESTAMPTZ NULL | 성공 시각 |
| failed_at | TIMESTAMPTZ NULL | 실패 시각 |
| uploaded_at | TIMESTAMPTZ | 처리 시각 |

인덱스: `upload_batch_id`, `(upload_batch_id, status)`.
배치 ID 시퀀스: `term_upload_batch_seq`.

## 6. API (OpenAPI spec-first → generator stub/client)

`openapi/openapi.yaml` 에 추가:

- `POST /terms/uploads` — `multipart/form-data`, part `file`(.jsonl)
  → 200 `TermUploadResult`
- `GET /terms/uploads/{uploadBatchId}` — 단건 배치 결과 재조회 → 200 `TermUploadResult`
- `GET /terms/uploads` — 배치 목록(최근순) → 200 `TermUploadBatchListResponse`

스키마:
- `TermUploadResult { uploadBatchId, uploadedAt, totalRows, inserted, updated, failed, rows: TermUploadRow[] }`
- `TermUploadRow { lineNo, termId?, status(INSERTED|UPDATED|FAILED), errorMessage?, registeredAt?, failedAt? }`
- `TermUploadBatchSummary { uploadBatchId, uploadedAt, totalRows, inserted, updated, failed }`
- `TermUploadBatchListResponse { items: TermUploadBatchSummary[] }`

## 7. 백엔드 컴포넌트 (postgres 전용)

- `TermUploadController`(생성된 stub 구현) — multipart 수신, 서비스 위임.
- `TermUploadService` — 파싱 + 2-pass 오케스트레이션 + 결과 집계. row 단위 tx 경계.
- `QuerydslTermUploadRepository` — `term_upload_row` 기록/조회(@Profile postgres).
- `QuerydslTermRepository` 확장 — upsert 메서드 추가:
  `upsertTerm`, `upsertExpression`, `upsertAlias`, `upsertRelationship`(존재검사 후 insert/update).
- 파서 유틸 — Jackson 으로 줄 객체 → 내부 명령 DTO(`TermUploadCommand`) 매핑. 필수필드 검증.

memory 모드에서는 위 빈들이 비활성 → 업로드 컨트롤러도 postgres 에서만 동작.
(컨트롤러를 `@Profile("postgres")` 로 한정, memory 에서는 404/비활성.)

## 8. 프론트엔드 (Next.js, Tailwind 4, Base UI, 생성된 axios client)

- 페이지: `apps/frontend` 내 `/terms/upload`.
- 구성:
  - 파일 선택(.jsonl) + 업로드 버튼.
  - 업로드 후 집계 배지(total/inserted/updated/failed).
  - 결과 표: `line_no, termId, status, error_message, registered_at, failed_at`.
    FAILED 행 강조(빨강), INSERTED/UPDATED 구분 배지.
  - 최근 배치 목록(선택 시 해당 배치 결과 재조회).
- 생성된 TypeScript axios client 사용(rules: openapi-generator).

## 9. 에러 처리

- 줄 파싱 오류, 필수필드 누락, 제약 위반(예: 잘못된 enum/길이), 관계 target 부재 → 모두 해당 row FAILED + `error_message` 기록, `failed_at` 설정.
- row 격리: 개별 tx → 한 줄 실패가 배치 전체를 무효화하지 않음.
- 파일 자체 오류(빈 파일/비 JSONL) → 400 또는 0-row 결과.

## 10. 테스트

- 파서: 정상/계층형/누락필드/깨진 JSON.
- upsert: 신규 insert, 기존 termId update, 표현/별칭/관계 upsert.
- 격리: 한 줄 FAILED 시 나머지 정상 처리 확인.
- 집계: total = inserted+updated+failed.
- 결과 조회 API: 배치 단건/목록.

## 11. 미해결/가정

- 인증/권한 없음(개발 단계).
- 동일 파일 재업로드는 새 batch 로 처리(이전 결과 보존), term 은 upsert 라 멱등.
- 대용량(수만 줄) 성능 최적화는 범위 외(현재 동기 처리).
