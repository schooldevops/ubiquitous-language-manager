"use client";

import { ReactNode } from "react";
import {
  BookOpen,
  Database,
  Download,
  FileJson,
  GitBranch,
  LayoutDashboard,
  ListChecks,
  Plus,
  Search,
  Table2,
} from "lucide-react";
import MermaidDiagram from "./MermaidDiagram";

const ERD_CHART = `erDiagram
    TERM_MASTER {
        varchar term_id PK
        varchar term_no UK
        varchar domain_name
        varchar korean_name
        varchar english_name
        varchar english_abbreviation
        varchar physical_type
        varchar status
        varchar owner
        varchar version
    }
    TERM_EXPRESSION {
        bigint expression_id PK
        varchar term_id FK
        varchar expression_type
        varchar expression_value
        boolean is_standard
    }
    TERM_ALIAS {
        varchar alias_id PK
        varchar term_id FK
        varchar alias_name UK
        varchar alias_type
        varchar recommendation_action
    }
    TERM_RELATIONSHIP {
        bigint relationship_id PK
        varchar source_term_id FK
        varchar relationship_type
        varchar target_term_id FK
    }
    TERM_CHANGE_HISTORY {
        bigint change_history_id PK
        varchar term_id FK
        varchar change_type
        varchar previous_status
        varchar new_status
        varchar approved_by
    }
    TERM_CANDIDATE {
        varchar candidate_id PK
        varchar requested_by
        varchar domain_name
        varchar korean_name
        varchar english_abbreviation
        varchar status
        varchar promoted_term_id FK
    }
    TERM_REVIEW_REQUEST {
        varchar review_request_id PK
        varchar candidate_id FK
        varchar term_id FK
        varchar status
        varchar reviewer
    }

    TERM_MASTER ||--o{ TERM_EXPRESSION : has
    TERM_MASTER ||--o{ TERM_ALIAS : has
    TERM_MASTER ||--o{ TERM_RELATIONSHIP : source
    TERM_MASTER ||--o{ TERM_CHANGE_HISTORY : changed
    TERM_MASTER ||--o{ TERM_CANDIDATE : promoted
    TERM_CANDIDATE ||--o{ TERM_REVIEW_REQUEST : reviewed`;

const JSONL_SAMPLE = `{
  "termId": "T-000001",
  "termNumber": "TERM-000001",
  "domainName": "고객",
  "usageType": "표준항목",
  "koreanName": "고객번호",
  "englishName": "Customer Number",
  "englishAbbreviation": "CUST_NO",
  "businessDefinition": "회사에서 고객을 업무적으로 식별하기 위해 사용하는 번호",
  "usageContext": "주문, 계약, 청구, 상담 등에서 고객 식별 기준으로 사용",
  "physicalType": "VARCHAR",
  "digits": 20,
  "decimalPoint": 0,
  "status": "Approved",
  "owner": "고객도메인 데이터스튜어드",
  "version": "1.0",
  "expressions": [
    { "expressionType": "DB_COLUMN",  "expressionValue": "CUST_NO",        "isStandard": true, "language": "en", "style": "UPPER_SNAKE" },
    { "expressionType": "API_FIELD",  "expressionValue": "customerNumber", "isStandard": true, "language": "en", "style": "camelCase" },
    { "expressionType": "CODE_VARIABLE", "expressionValue": "customerNumber", "isStandard": true },
    { "expressionType": "UI_LABEL",   "expressionValue": "고객번호",         "isStandard": true, "language": "ko" }
  ],
  "aliases": {
    "synonyms":     [ { "aliasId": "A-000001", "aliasName": "고객ID", "aliasType": "Synonym",  "recommendationAction": "표준어 CUST_NO 사용 권장", "reason": "동일 의미" } ],
    "forbidden":    [ { "aliasName": "cust_id",  "aliasType": "Forbidden", "recommendationAction": "사용 금지", "reason": "비표준 약어" } ],
    "deprecated":   [],
    "needsContext": []
  }
}`;

export default function ManualTab({
  onDownloadExcel,
  onDownloadJsonl,
}: {
  onDownloadExcel: () => void;
  onDownloadJsonl: () => void;
}) {
  return (
    <div className="mx-auto max-w-5xl space-y-6 px-6 py-6">
      <header className="rounded-md border border-[var(--line)] bg-white px-5 py-4">
        <div className="flex items-center gap-2 text-lg font-semibold">
          <BookOpen size={20} />
          AULMS 용어 관리 사용 설명서
        </div>
        <p className="mt-2 text-sm text-[var(--muted)]">
          표준 용어의 조회·등록·수정·승인과 일괄 업로드/다운로드 사용법을 안내합니다.
          각 탭의 기능, 데이터 구조(ERD), 데이터 흐름, JSONL 파일 규격을 순서대로 설명합니다.
        </p>
      </header>

      {/* 1. 웹 페이지 설명 및 목적 */}
      <Section no="1" title="웹 페이지 설명 및 목적" icon={<BookOpen size={17} />}>
        <p>
          AULMS 용어 관리는 전사 <b>표준 용어 사전</b>을 한 곳에서 관리하기 위한 화면입니다.
          하나의 업무 개념(예: 고객번호)에 대해 한글명·영문명·약어와 함께
          <b>산출물 표현</b>(DB 컬럼명, API 필드명, 코드 변수명, 화면 표시명)을 일관되게 매핑합니다.
        </p>
        <ul className="ml-4 list-disc space-y-1">
          <li>같은 개념을 시스템마다 다르게 부르는 문제(고객번호 / CUST_NO / customerNumber)를 표준 1건으로 통일합니다.</li>
          <li>용어 변경 시 <b>영향도 분석</b>으로 어떤 시스템·산출물이 영향을 받는지 미리 확인합니다.</li>
          <li>현업이 제안한 <b>신규 용어 후보</b>를 검토·승인해 표준 용어로 승격합니다.</li>
          <li>JSONL 일괄 업로드와 엑셀/JSONL 다운로드로 대량 데이터를 주고받습니다.</li>
        </ul>
      </Section>

      {/* 2. 메뉴별 기능 */}
      <Section no="2" title="메뉴별 기능" icon={<ListChecks size={17} />}>
        <p>상단 탭으로 화면을 전환합니다. 각 탭의 역할은 아래와 같습니다.</p>

        <SubSection no="2.1" title="대시보드" icon={<LayoutDashboard size={15} />}>
          <ul className="ml-4 list-disc space-y-1">
            <li>상단 카드: <b>전체 용어 수 · 도메인 수 · 확정 용어 수</b> 요약.</li>
            <li><b>최근 등록/수정된 용어</b> 표: 행을 클릭하면 해당 용어로 ‘용어 조회’ 탭이 열립니다.</li>
            <li><b>도메인별 등록 용어 수</b> 표: 전체/확정/작성/검토/폐기·반려 건수 확인. 행 클릭 시 해당 도메인으로 검색됩니다.</li>
          </ul>
          <Tip>처음 화면 진입 시 자동으로 통계를 불러옵니다. 숫자가 0이면 아직 등록된 용어가 없는 상태입니다.</Tip>
        </SubSection>

        <SubSection no="2.2" title="용어 조회" icon={<Search size={15} />}>
          <ul className="ml-4 list-disc space-y-1">
            <li>검색어(한글명/영문명/약어/API 필드), 도메인, 상태로 필터링 후 <b>검색</b>.</li>
            <li>좌측 목록에서 용어를 선택하면 우측에 <b>상세 정보</b>(업무 정의, 물리 타입, 자릿수 등)가 표시됩니다.</li>
            <li><b>영향도 분석</b>: 변경 유형(API 필드명 변경 등)을 선택하면 영향받는 대상·위험도·권장 조치를 보여줍니다.</li>
            <li><b>산출물 표현 / 별칭·금지어 / 변경 히스토리</b>를 함께 확인합니다.</li>
            <li>하단 <b>용어 수정</b> 버튼으로 선택 용어를 ‘용어 수정’ 탭에서 편집합니다.</li>
          </ul>
        </SubSection>

        <SubSection no="2.3" title="용어 등록" icon={<Plus size={15} />}>
          <ul className="ml-4 list-disc space-y-1">
            <li>신규 표준 용어를 직접 입력해 등록합니다(한글명·영문명·약어·도메인·업무 정의 등).</li>
            <li>한글명 옆 <b>추천</b> 버튼: 입력한 한글명으로 영문명·약어·산출물 표현·별칭을 AI가 채워 줍니다.</li>
            <li><b>산출물 표현</b>(DB/API/코드/UI)과 <b>별칭·금지어</b>를 행 단위로 추가할 수 있습니다.</li>
            <li>등록 시 동일·유사 용어가 있으면 <b>중복 확인 모달</b>이 뜨며, 확인 후 그대로 등록할 수 있습니다.</li>
            <li>상단의 <b>파일 업로드</b>로 JSONL을 올려 여러 용어를 한 번에 등록합니다(4장 참고).</li>
          </ul>
        </SubSection>

        <SubSection no="2.4" title="용어 수정" icon={<Database size={15} />}>
          <ul className="ml-4 list-disc space-y-1">
            <li>‘용어 조회’에서 선택한 용어를 편집합니다. 선택된 용어가 없으면 안내 문구가 표시됩니다.</li>
            <li>기본 정보·산출물 표현·별칭/금지어를 함께 수정합니다.</li>
            <li><b>변경 사유는 필수</b>입니다. 입력하지 않으면 저장되지 않습니다.</li>
            <li>저장 시 버전이 자동 증가하고(예: 1.0 → 1.1), 변경 내역이 히스토리에 기록됩니다.</li>
          </ul>
        </SubSection>

        <SubSection no="2.5" title="신규 용어 후보" icon={<ListChecks size={15} />}>
          <ul className="ml-4 list-disc space-y-1">
            <li>아직 표준이 아닌 용어를 <b>후보</b>로 등록하고 검토 절차를 거칩니다.</li>
            <li>후보 한글명 <b>추천</b> 버튼으로 영문명·약어·물리 타입 등을 자동 제안받습니다.</li>
            <li>상태 흐름: <b>Draft → Reviewing → Approved → Promoted</b>(또는 Rejected). 승인(Approved) 후에만 <b>표준 승격</b> 가능.</li>
            <li><b>표준 승격</b> 시 후보가 정식 표준 용어로 등록되고 ‘용어 조회’ 탭으로 이동합니다.</li>
            <li>하단 <b>미승인 항목 목록</b>: 승격 전 후보 + Draft/Reviewing 용어를 모아 콤보박스로 상태를 즉시 변경합니다.</li>
          </ul>
        </SubSection>
      </Section>

      {/* 3. 테이블 구조 및 ERD */}
      <Section no="3" title="테이블 구조 및 ERD" icon={<Table2 size={17} />}>
        <p>핵심 테이블은 <b>TERM_MASTER</b>(용어 마스터)를 중심으로 표현·별칭·관계·이력·후보가 1:N으로 연결됩니다.</p>
        <div className="rounded-md border border-[var(--line)] bg-[#fbfcfd] p-3">
          <MermaidDiagram id="core-erd" chart={ERD_CHART} />
        </div>
        <table className="w-full table-fixed text-left text-sm">
          <thead className="bg-[#eef2f5] text-xs text-[var(--muted)]">
            <tr>
              <th className="w-[28%] px-3 py-2">테이블</th>
              <th className="px-3 py-2">역할</th>
            </tr>
          </thead>
          <tbody>
            {[
              ["TERM_MASTER", "표준 용어 마스터(한글/영문/약어/물리타입/상태/소유자/버전)"],
              ["TERM_EXPRESSION", "산출물 표현 매핑(DB 컬럼, API 필드, 코드 변수, UI 라벨 등)"],
              ["TERM_ALIAS", "유사어/금지어/폐기어/문맥확인 별칭"],
              ["TERM_RELATIONSHIP", "용어 간 관계(상위·하위·연관 등)"],
              ["TERM_CHANGE_HISTORY", "상태/내용 변경 이력 및 승인자 기록"],
              ["TERM_CANDIDATE", "신규 용어 후보 및 승격 결과(promoted_term_id)"],
              ["TERM_REVIEW_REQUEST", "후보/용어 검토 요청과 검토자 의견"],
            ].map(([name, desc]) => (
              <tr key={name} className="border-t border-[var(--line)]">
                <td className="px-3 py-2 font-medium">{name}</td>
                <td className="px-3 py-2 text-[var(--muted)]">{desc}</td>
              </tr>
            ))}
          </tbody>
        </table>

        <SubSection no="3.1" title="데이터 입력 시 입력되는 순서 흐름" icon={<GitBranch size={15} />}>
          <ol className="ml-4 list-decimal space-y-1">
            <li><b>TERM_MASTER</b> 1건 생성 → 새 <code>term_id</code> 발급(상태 기본값 Draft).</li>
            <li>발급된 <code>term_id</code>를 FK로 <b>TERM_EXPRESSION</b>(입력된 표현 행만) 등록.</li>
            <li>같은 <code>term_id</code>로 <b>TERM_ALIAS</b>(별칭명이 입력된 행만) 등록.</li>
            <li>등록·변경 시 <b>TERM_CHANGE_HISTORY</b>에 이력 1건 적재.</li>
          </ol>
          <Tip>마스터가 먼저 만들어져 term_id가 나와야 자식 테이블(표현·별칭)을 연결할 수 있습니다. 후보 경로는 TERM_CANDIDATE 등록 → 승격 시 TERM_MASTER로 복제됩니다.</Tip>
        </SubSection>

        <SubSection no="3.2" title="CRUD 시 데이터가 흘러가는 흐름" icon={<GitBranch size={15} />}>
          <table className="w-full table-fixed text-left text-sm">
            <thead className="bg-[#eef2f5] text-xs text-[var(--muted)]">
              <tr>
                <th className="w-[16%] px-3 py-2">동작</th>
                <th className="px-3 py-2">흐름</th>
              </tr>
            </thead>
            <tbody>
              {[
                ["Create", "화면 입력 → createTerm(MASTER) → createExpression/createAlias(자식) → 히스토리 적재"],
                ["Read", "listTerms(검색/필터) → getTerm(상세: 표현·별칭 포함) → 영향도 분석 조회"],
                ["Update", "updateTerm(버전 증가, 변경 사유 필수) → replaceExpressions/replaceAliases(전체 교체) → 히스토리 적재"],
                ["Delete(폐기)", "deprecateTerm → 상태 Deprecated 전환(물리 삭제 대신 상태 보존) → 히스토리 적재"],
              ].map(([op, flow]) => (
                <tr key={op} className="border-t border-[var(--line)]">
                  <td className="px-3 py-2 font-medium">{op}</td>
                  <td className="px-3 py-2 text-[var(--muted)]">{flow}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <Tip>수정은 표현/별칭을 ‘전체 교체’합니다. 화면에서 다루지 않는 표현 타입(한글/영문/테스트 단어 등)은 보존되고, 관리 대상 4개(DB·API·코드·UI)만 입력값으로 대체됩니다.</Tip>
        </SubSection>
      </Section>

      {/* 4. JSONL 파일 구조 */}
      <Section no="4" title="파일 업로드 시 JSONL 파일 내용 구조" icon={<FileJson size={17} />}>
        <p>
          업로드 파일은 <b>JSONL</b>(JSON Lines) 형식입니다. <b>한 줄 = 용어 1건</b>이며, 각 줄은 완전한 JSON 객체여야 합니다.
          빈 줄은 무시되고, <code>termId</code>가 없는 줄은 실패 처리됩니다. 인코딩은 UTF-8입니다.
        </p>
        <table className="w-full table-fixed text-left text-sm">
          <thead className="bg-[#eef2f5] text-xs text-[var(--muted)]">
            <tr>
              <th className="w-[30%] px-3 py-2">필드</th>
              <th className="px-3 py-2">설명</th>
            </tr>
          </thead>
          <tbody>
            {[
              ["termId (필수)", "용어 식별 ID. 누락 시 해당 행 실패."],
              ["termNumber", "업무 노출용 용어 번호."],
              ["domainName / usageType", "도메인명 / 사용 구분."],
              ["koreanName / englishName / englishAbbreviation", "한글명 / 영문명 / 영문 약어."],
              ["businessDefinition / usageContext", "업무 정의 / 사용 맥락."],
              ["physicalType / digits / decimalPoint", "물리 타입 / 자릿수(정수) / 소수점(정수)."],
              ["status", "Draft·Reviewing·Approved·Deprecated·Rejected 중 하나."],
              ["owner / version", "소유자 / 버전 문자열."],
              ["expressions[]", "표현 배열. expressionType·expressionValue·isStandard(+language·style)."],
              ["aliases{}", "별칭 객체. synonyms·forbidden·deprecated·needsContext 4개 그룹 배열."],
              ["relationships[]", "(선택) 용어 관계. relationshipType·targetTermId·description."],
            ].map(([f, d]) => (
              <tr key={f} className="border-t border-[var(--line)]">
                <td className="px-3 py-2 font-medium">{f}</td>
                <td className="px-3 py-2 text-[var(--muted)]">{d}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <div>
          <div className="mb-1 text-sm font-medium">예시 (가독성을 위해 들여썼지만, 실제 파일에서는 한 용어를 한 줄로 작성)</div>
          <pre className="overflow-auto rounded-md bg-[#1e272c] px-4 py-3 text-xs leading-relaxed text-[#e6edf0]">{JSONL_SAMPLE}</pre>
        </div>
        <Tip>업로드 후 결과 표에 성공/실패 건수가 표시됩니다. 실패 행은 오류 메시지를 확인해 수정한 뒤 다시 업로드하세요. ‘용어 등록’ 탭 상단의 파일 업로드 영역을 사용합니다.</Tip>
      </Section>

      {/* 5. 다운로드 */}
      <Section no="5" title="엑셀 다운로드 및 JSONL 전체 다운로드" icon={<Download size={17} />}>
        <p>현재 등록된 <b>전체 표준 용어</b>를 두 가지 형식으로 내려받을 수 있습니다.</p>
        <ul className="ml-4 list-disc space-y-1">
          <li>
            <b>엑셀(.xlsx)</b>: 한 행에 한 용어. 기본 정보 + 산출물 표현(DB/API/코드/UI) + 유사어·금지어 열을 포함합니다.
            검토·공유·보고용으로 적합합니다.
          </li>
          <li>
            <b>JSONL 전체</b>: 업로드와 동일한 구조로 내려받습니다. 받은 파일을 ‘용어 등록’ 탭에서 <b>그대로 재업로드</b>할 수 있어 백업·이관에 사용합니다.
          </li>
        </ul>
        <div className="flex flex-wrap gap-3">
          <button
            type="button"
            onClick={onDownloadExcel}
            className="inline-flex h-10 items-center gap-2 rounded-md bg-[var(--accent)] px-4 text-sm font-medium text-white"
          >
            <Download size={16} />
            엑셀(.xlsx) 다운로드
          </button>
          <button
            type="button"
            onClick={onDownloadJsonl}
            className="inline-flex h-10 items-center gap-2 rounded-md border border-[var(--line)] px-4 text-sm font-medium"
          >
            <FileJson size={16} />
            JSONL 전체 다운로드
          </button>
        </div>
        <Tip>다운로드 버튼을 누르면 브라우저가 terms.xlsx / terms.jsonl 파일을 저장합니다. 용어 수가 많으면 생성에 수 초가 걸릴 수 있습니다.</Tip>
      </Section>
    </div>
  );
}

function Section({ no, title, icon, children }: { no: string; title: string; icon: ReactNode; children: ReactNode }) {
  return (
    <section className="rounded-md border border-[var(--line)] bg-white">
      <div className="flex items-center gap-2 border-b border-[var(--line)] px-5 py-3 font-semibold">
        {icon}
        <span className="text-[var(--muted)]">{no}.</span> {title}
      </div>
      <div className="space-y-3 px-5 py-4 text-sm leading-relaxed">{children}</div>
    </section>
  );
}

function SubSection({ no, title, icon, children }: { no: string; title: string; icon: ReactNode; children: ReactNode }) {
  return (
    <div className="rounded-md border border-[var(--line)] bg-[#fbfcfd] p-4">
      <div className="mb-2 flex items-center gap-2 font-medium">
        {icon}
        <span className="text-[var(--muted)]">{no}</span> {title}
      </div>
      <div className="space-y-2 text-sm leading-relaxed">{children}</div>
    </div>
  );
}

function Tip({ children }: { children: ReactNode }) {
  return (
    <p className="rounded-md border-l-2 border-[var(--accent)] bg-[#f1f7f6] px-3 py-2 text-xs text-[var(--muted)]">
      💡 {children}
    </p>
  );
}
