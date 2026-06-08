# 데이터 통합 매핑 (Phase 7)

온톨로지 노드/정책값 ↔ BSS 컬럼 ↔ 채널 필드. BSS/채널 열은 실제 스키마 확보 후 채움.

## 1. 공통 어휘 (Term 기반 정렬 기준)

| Term ID | 용어 | 통합 식별 역할 |
|---|---|---|
| TM-MBR-014 | CI (Connecting Information) | 동일인을 식별하기 위해 본인확인기관이 제공하는 연계 식별 정보… |
| TM-MBR-015 | DI (Duplicated Information) | 동일 서비스 내 중복 식별을 위해 사용하는 중복 가입 확인 정보… |
| TM-MBR-008 | 회원 상태 | 정상, 휴면, 탈퇴, 가입제한, 미가입 등 고객의 회원 처리 상태… |
| TM-MBR-001 | 고객 | A커머스 통합채널에서 회원 업무를 조회·신청·변경·종료하는 자연인 이용자… |

## 2. 코드·열거 정책값 매핑 시드 (자동 추출 241건)

| 정책항목 ID | 소속정책 | 항목명 | 온톨로지 값(원문) | BSS 컬럼 | 채널 필드 |
|---|---|---|---|---|---|
| POL-MBR-ACCT-001-01 | PG-MBR-ACCT-001 | 계정 생성 조건 | 약관 동의 완료, 본인인증 완료, 신규가입 가능 |  |  |
| POL-MBR-ACCT-001-06 | PG-MBR-ACCT-001 | 가입 채널 저장 값 | 앱, 모바일웹, PC웹 |  |  |
| POL-MBR-ACCT-001-09 | PG-MBR-ACCT-001 | 계정 생성 완료 상태 코드 | 정상 |  |  |
| POL-MBR-ACCT-001-11 | PG-MBR-ACCT-001 | 생성 이력 저장 항목 | 고객ID, CI 해시, 가입 채널, 생성 일시, 처리 결과 코드 |  |  |
| POL-MBR-AUTH-001-05 | PG-MBR-AUTH-001 | 본인인증 적용 시점 | 회원 검증 전, 휴면 해제 처리 전, 탈퇴 최종 동의 전, 재가입 가능 여부 확인 전 |  |  |
| POL-MBR-AUTH-001-06 | PG-MBR-AUTH-001 | 동일 세션 인증 재사용 여부 | 회원 가입: 허용, 휴면 해제: 허용, 재가입: 허용, 회원 탈퇴: 불가 |  |  |
| POL-MBR-AUTH-001-07 | PG-MBR-AUTH-001 | 인증 재사용 유효시간 | 동일 세션 기준 10분 |  |  |
| POL-MBR-AUTH-002-01 | PG-MBR-AUTH-002 | 회원 가입 허용 인증수단 | 휴대폰 본인인증, PASS 인증, 공동인증서 |  |  |
| POL-MBR-AUTH-002-02 | PG-MBR-AUTH-002 | 휴면 해제 허용 인증수단 | 휴대폰 본인인증, PASS 인증, 공동인증서 |  |  |
| POL-MBR-AUTH-002-03 | PG-MBR-AUTH-002 | 회원 탈퇴 허용 인증수단 | 휴대폰 본인인증, PASS 인증 |  |  |
| POL-MBR-AUTH-002-04 | PG-MBR-AUTH-002 | 재가입 허용 인증수단 | 휴대폰 본인인증, PASS 인증, 공동인증서 |  |  |
| POL-MBR-AUTH-002-05 | PG-MBR-AUTH-002 | 기본 노출 인증수단 | 휴대폰 본인인증 |  |  |
| POL-MBR-AUTH-002-06 | PG-MBR-AUTH-002 | 대체 인증수단 | 공동인증서 |  |  |
| POL-MBR-AUTH-002-07 | PG-MBR-AUTH-002 | 미성년자 인증수단 | 법정대리인 휴대폰 본인인증 |  |  |
| POL-MBR-AUTH-002-08 | PG-MBR-AUTH-002 | 외국인 인증수단 | 휴대폰 본인인증, 외국인등록번호 기반 인증 |  |  |
| POL-MBR-AUTH-002-09 | PG-MBR-AUTH-002 | 인증수단 노출 순서 | 휴대폰 본인인증 → PASS 인증 → 공동인증서 |  |  |
| POL-MBR-AUTH-003-03 | PG-MBR-AUTH-003 | 인증번호 유효시간 | 3분 |  |  |
| POL-MBR-AUTH-003-04 | PG-MBR-AUTH-003 | 인증번호 발급 채널 | SMS |  |  |
| POL-MBR-AUTH-005-05 | PG-MBR-AUTH-005 | 실패 횟수 포함 대상 | 인증번호 불일치, 인증수단 검증 실패 |  |  |
| POL-MBR-AUTH-005-06 | PG-MBR-AUTH-005 | 실패 횟수 제외 대상 | 외부 인증기관 장애, 네트워크 오류, 시스템 오류 |  |  |
| POL-MBR-AUTH-006-01 | PG-MBR-AUTH-006 | 인증 성공 판정 기준 | 외부 인증기관 성공 응답, CI 수신, 이름·생년월일 일치 |  |  |
| POL-MBR-AUTH-006-02 | PG-MBR-AUTH-006 | 인증 실패 판정 기준 | 외부 인증기관 실패 응답, 인증번호 불일치, 필수 식별정보 불일치 |  |  |
| POL-MBR-AUTH-006-03 | PG-MBR-AUTH-006 | 인증 취소 판정 기준 | 고객 취소, 브라우저 종료, 인증 세션 종료 |  |  |
| POL-MBR-AUTH-006-04 | PG-MBR-AUTH-006 | 인증 오류 판정 기준 | 외부 인증기관 장애, 통신 오류, 시스템 오류 |  |  |
| POL-MBR-AUTH-006-05 | PG-MBR-AUTH-006 | 인증 시간초과 판정 기준 | 인증 세션 유지시간 초과, 인증번호 유효시간 초과 |  |  |
| POL-MBR-AUTH-006-06 | PG-MBR-AUTH-006 | 내부 인증 결과 코드 | SUCCESS, FAIL, CANCEL, ERROR, TIMEOUT |  |  |
| POL-MBR-AUTH-006-07 | PG-MBR-AUTH-006 | 외부 인증 결과 코드 매핑 | 외부 성공=SUCCESS, 외부 실패=FAIL, 고객 취소=CANCEL, 기관 오류=ERROR, 시간초과=T… |  |  |
| POL-MBR-AUTH-007-01 | PG-MBR-AUTH-007 | 인증 이력 저장 항목 | 업무구분, 인증수단, 인증결과, 결과코드, 요청일시, 완료일시, 채널, 세션ID, CI 해시, IP |  |  |
| POL-MBR-AUTH-007-02 | PG-MBR-AUTH-007 | 인증 이력 보관 기간 | 5년 |  |  |
| POL-MBR-AUTH-007-03 | PG-MBR-AUTH-007 | 인증 이력 조회 권한 | 회원 정책 담당자, 인증 시스템 운영자, 감사 권한자 |  |  |
| POL-MBR-AUTH-007-04 | PG-MBR-AUTH-007 | 인증 이력 마스킹 대상 | CI, DI, 휴대폰번호, 이름, 생년월일, IP |  |  |
| POL-MBR-AUTH-007-06 | PG-MBR-AUTH-007 | 감사 추적 항목 | 조회자 ID, 조회일시, 조회사유, 변경 전 값, 변경 후 값 |  |  |
| POL-MBR-AUTH-008-03 | PG-MBR-AUTH-008 | 추가 인증수단 | 휴대폰 본인인증, PASS 인증 |  |  |
| POL-MBR-AUTH-008-06 | PG-MBR-AUTH-008 | 인증 유효시간 | 5분 |  |  |
| POL-MBR-AUTH-008-10 | PG-MBR-AUTH-008 | 추가 인증 이력 저장 항목 | 고객ID, 업무ID, 인증수단, 인증 결과, 인증 일시, 처리 채널 |  |  |
| POL-MBR-DORM-001-01 | PG-MBR-DORM-001 | 휴면 해제 가능 상태 | 휴면 |  |  |
| POL-MBR-DORM-001-02 | PG-MBR-DORM-001 | 휴면 해제 불가 상태 | 탈퇴, 가입제한 |  |  |
| POL-MBR-DORM-001-05 | PG-MBR-DORM-001 | 휴면 해제 가능 채널 | 앱, 모바일웹, PC웹 |  |  |
| POL-MBR-DORM-001-06 | PG-MBR-DORM-001 | 휴면 해제 제한 조건 | 본인인증 실패, 가입제한 상태, BSS 상태 조회 실패 |  |  |
| POL-MBR-DORM-001-07 | PG-MBR-DORM-001 | 해제 불가 안내 항목 | 해제 불가 사유, 후속 문의 경로 |  |  |
| POL-MBR-DORM-001-12 | PG-MBR-DORM-001 | 해제 가능 여부 이력 저장 항목 | 고객ID, 상태 코드, 판정 결과, 판정 일시 |  |  |
| POL-MBR-DORM-002-01 | PG-MBR-DORM-002 | 상태 전환 대상 상태 | 휴면 |  |  |
| POL-MBR-DORM-002-02 | PG-MBR-DORM-002 | 전환 후 상태 | 정상 |  |  |
| POL-MBR-DORM-002-03 | PG-MBR-DORM-002 | 상태 전환 조건 | 본인인증 성공, 필수 약관 재동의 완료, 휴면 해제 가능 판정 통과 |  |  |
| POL-MBR-DORM-002-04 | PG-MBR-DORM-002 | 상태 전환 처리 시스템 | BSS |  |  |
| POL-MBR-DORM-002-05 | PG-MBR-DORM-002 | 상태 전환 기준 시각 | BSS 처리 완료 시각 |  |  |
| POL-MBR-DORM-002-07 | PG-MBR-DORM-002 | 상태 전환 실패 처리 | 업무 중단 및 오류 안내 |  |  |
| POL-MBR-DORM-002-08 | PG-MBR-DORM-002 | 상태 전환 이력 저장 여부 | 저장 |  |  |
| POL-MBR-DORM-002-09 | PG-MBR-DORM-002 | 상태 전환 이력 저장 항목 | 고객ID, 이전 상태, 변경 상태, 처리 일시, 처리 채널, 처리 결과 코드 |  |  |
| POL-MBR-DORM-002-10 | PG-MBR-DORM-002 | 상태 전환 알림 여부 | 발송 |  |  |
| POL-MBR-DORM-002-11 | PG-MBR-DORM-002 | 상태 전환 알림 채널 | 앱 푸시, SMS |  |  |
| POL-MBR-DORM-002-12 | PG-MBR-DORM-002 | 상태 전환 결과 코드 | 성공, 실패, 오류 |  |  |
| POL-MBR-DORM-003-01 | PG-MBR-DORM-003 | 복원 대상 데이터 | 회원 기본정보, 로그인 정보, 마케팅 수신 동의, 서비스 이용 설정 |  |  |
| POL-MBR-DORM-003-02 | PG-MBR-DORM-003 | 복원 제외 데이터 | 삭제 대상 개인정보, 법정 보관 만료 정보 |  |  |
| POL-MBR-DORM-003-05 | PG-MBR-DORM-003 | 복원 처리 순서 | 식별정보, 회원 기본정보, 수신 동의, 서비스 이용 설정 |  |  |
| POL-MBR-DORM-003-09 | PG-MBR-DORM-003 | 복원 후 검증 항목 | 회원 상태, 로그인 가능 여부, 필수 정보 존재 여부 |  |  |
| POL-MBR-DORM-003-11 | PG-MBR-DORM-003 | 복원 이력 저장 항목 | 고객ID, 복원 대상, 복원 결과, 복원 일시, 처리 채널 |  |  |
| POL-MBR-DORM-003-12 | PG-MBR-DORM-003 | 복원 결과 코드 | 성공, 실패, 부분 실패, 오류 |  |  |
| POL-MBR-DORM-004-03 | PG-MBR-DORM-004 | 안내 채널 | 화면, 앱 푸시, SMS |  |  |
| POL-MBR-DORM-004-04 | PG-MBR-DORM-004 | 화면 안내 항목 | 정상 회원 전환 완료, 서비스 이용 가능 상태, 후속 이동 경로 |  |  |
| POL-MBR-DORM-004-05 | PG-MBR-DORM-004 | 알림 발송 항목 | 휴면 해제 완료 일시, 처리 채널 |  |  |
| POL-MBR-DORM-004-06 | PG-MBR-DORM-004 | 안내 제외 조건 | 고객 연락처 부재, 알림 수신 불가 상태 |  |  |
| POL-MBR-DORM-004-08 | PG-MBR-DORM-004 | 후속 권장 경로 | 회원정보 확인, 마케팅 동의 관리 |  |  |
| POL-MBR-DORM-004-10 | PG-MBR-DORM-004 | 결과 안내 이력 저장 항목 | 고객ID, 안내 채널, 발송 일시, 발송 결과 코드 |  |  |
| POL-MBR-INFO-001-01 | PG-MBR-INFO-001 | 회원 가입 필수 입력 항목 | 아이디, 비밀번호, 이메일, 연락처 |  |  |
| POL-MBR-INFO-001-02 | PG-MBR-INFO-001 | 회원 가입 선택 입력 항목 | 닉네임, 관심 혜택, 추천 수신 선호 항목 |  |  |
| POL-MBR-INFO-001-03 | PG-MBR-INFO-001 | 본인인증 결과 연계 항목 | 이름, 생년월일, 성별, 내외국인 구분, 휴대폰번호, CI, DI |  |  |
| POL-MBR-INFO-001-04 | PG-MBR-INFO-001 | 고객 유형 구분 항목 | 일반 개인, 미성년자, 외국인 |  |  |
| POL-MBR-INFO-001-05 | PG-MBR-INFO-001 | 미성년자 추가 입력 항목 | 법정대리인 이름, 법정대리인 휴대폰번호, 고객과의 관계 |  |  |
| POL-MBR-INFO-001-06 | PG-MBR-INFO-001 | 외국인 추가 입력 항목 | 국적, 외국인등록번호 대체 식별값, 체류자격 확인 결과 |  |  |
| POL-MBR-INFO-001-07 | PG-MBR-INFO-001 | 고객 직접 입력 제외 항목 | CI, DI |  |  |
| POL-MBR-INFO-001-08 | PG-MBR-INFO-001 | 항목 노출 기준 | 고객 유형, 본인인증 결과, 가입 경로 |  |  |
| POL-MBR-INFO-001-09 | PG-MBR-INFO-001 | 입력 생략 가능 항목 | 본인인증 결과 연계 항목, 임시저장 복원 항목 |  |  |
| POL-MBR-INFO-001-10 | PG-MBR-INFO-001 | 입력 임시저장 대상 항목 | 아이디, 이메일, 연락처 |  |  |
| POL-MBR-INFO-001-11 | PG-MBR-INFO-001 | 입력 임시저장 제외 항목 | 비밀번호, CI, DI |  |  |
| POL-MBR-INFO-001-12 | PG-MBR-INFO-001 | 가입 완료 저장 항목 | 아이디, 이메일, 연락처, 이름, 생년월일, 성별, 내외국인 구분, 휴대폰번호, CI, DI |  |  |
| POL-MBR-INFO-002-01 | PG-MBR-INFO-002 | 필수값 검증 대상 | 아이디, 비밀번호, 이메일, 연락처 |  |  |
| POL-MBR-INFO-002-03 | PG-MBR-INFO-002 | 아이디 허용 문자 | 영문 대문자, 영문 소문자, 숫자 |  |  |
| POL-MBR-INFO-002-06 | PG-MBR-INFO-002 | 비밀번호 문자 조합 | 영문 대문자, 영문 소문자, 숫자, 특수문자 중 3종 이상 조합 |  |  |
| POL-MBR-INFO-002-12 | PG-MBR-INFO-002 | 검증 시점 | 입력 중, 다음 단계 이동 시, 가입 완료 요청 시 |  |  |
| POL-MBR-INFO-003-01 | PG-MBR-INFO-003 | 중복 확인 대상 식별정보 | CI, DI, 아이디, 휴대폰번호, 이메일 |  |  |
| POL-MBR-INFO-003-07 | PG-MBR-INFO-003 | 기존 정상 회원 식별 시 처리 | 로그인, 아이디 찾기 |  |  |
| POL-MBR-INFO-003-11 | PG-MBR-INFO-003 | 중복 확인 시점 | 회원 정보 입력 완료 후, 가입 처리 전 |  |  |
| POL-MBR-INFO-004-01 | PG-MBR-INFO-004 | 기존 정보 재사용 가능 항목 | 본인인증 결과 항목, CI, DI |  |  |
| POL-MBR-INFO-004-02 | PG-MBR-INFO-004 | 기존 정보 재사용 제한 항목 | 비밀번호, 마케팅 동의, 선택 약관 동의 |  |  |
| POL-MBR-INFO-004-03 | PG-MBR-INFO-004 | 신규 입력 필수 항목 | 아이디, 비밀번호, 휴대폰번호, 이메일 |  |  |
| POL-MBR-INFO-004-04 | PG-MBR-INFO-004 | 고객 확인 대상 항목 | 이름, 휴대폰번호, 이메일 |  |  |
| POL-MBR-INFO-004-11 | PG-MBR-INFO-004 | 입력 이력 저장 항목 | 고객ID, 입력 항목, 처리 일시, 처리 채널 |  |  |
| POL-MBR-JOIN-001-01 | PG-MBR-JOIN-001 | 신규가입 가능 고객 상태 | 미가입 |  |  |
| POL-MBR-JOIN-001-09 | PG-MBR-JOIN-001 | 가입 불가 안내 항목 | 가입 불가 사유, 후속 처리 경로 |  |  |
| POL-MBR-JOIN-001-11 | PG-MBR-JOIN-001 | 가입 제한 사유 코드 | DUPLICATE_CI, DORMANT_MEMBER, LEAVE_PENDING, REJOIN_LIMITED,… |  |  |
| POL-MBR-LEAVE-001-02 | PG-MBR-LEAVE-001 | 사유 코드 | 서비스 이용 빈도 낮음, 혜택 불만, 개인정보 우려, 다른 서비스 이용, 기타 |  |  |
| POL-MBR-LEAVE-001-09 | PG-MBR-LEAVE-001 | 사유 저장 항목 | 고객ID, 사유 코드, 기타 의견, 입력 일시, 처리 채널 |  |  |
| POL-MBR-LEAVE-001-10 | PG-MBR-LEAVE-001 | 사유 보관 기간 | 탈퇴 완료 후 3년 |  |  |
| POL-MBR-LEAVE-001-12 | PG-MBR-LEAVE-001 | 금칙어 처리 기준 | 욕설, 비속어, 주민등록번호, 카드번호, 계좌번호 등 민감정보 입력 제한 |  |  |
| POL-MBR-LEAVE-002-01 | PG-MBR-LEAVE-002 | 탈퇴 가능 회원 상태 | 정상 |  |  |
| POL-MBR-LEAVE-002-02 | PG-MBR-LEAVE-002 | 탈퇴 제한 회원 상태 | 휴면, 가입제한, 탈퇴 |  |  |
| POL-MBR-LEAVE-002-04 | PG-MBR-LEAVE-002 | 점검 대상 항목 | 미납 요금, 미완료 주문, 진행 중 업무, 보유 혜택·자산, 연계 서비스 |  |  |
| POL-MBR-LEAVE-002-06 | PG-MBR-LEAVE-002 | 탈퇴 불가 판정 기준 | 미납 요금 존재, 필수 선처리 업무 존재, BSS 상태 조회 실패 |  |  |
| POL-MBR-LEAVE-003-01 | PG-MBR-LEAVE-003 | 미납 조회 항목 | 통신요금, 단말기 할부금, 소액결제, 콘텐츠 이용료 |  |  |
| POL-MBR-LEAVE-003-02 | PG-MBR-LEAVE-003 | 미완료 주문 조회 항목 | 진행 중 주문, 배송 중 주문, 교환/반품/환불 진행 건 |  |  |
| POL-MBR-LEAVE-003-03 | PG-MBR-LEAVE-003 | 진행 중 업무 조회 항목 | 상담 접수, 민원 접수, AS 접수, 명의변경 진행 건 |  |  |
| POL-MBR-LEAVE-003-08 | PG-MBR-LEAVE-003 | 미처리 항목 안내 항목 | 항목명, 금액, 처리 상태, 처리 경로, 담당 채널 |  |  |
| POL-MBR-LEAVE-003-09 | PG-MBR-LEAVE-003 | 조회 수행 시스템 | BSS, 주문 시스템, 상담 시스템 |  |  |
| POL-MBR-LEAVE-003-12 | PG-MBR-LEAVE-003 | 확인 이력 저장 항목 | 고객ID, 조회 항목, 조회 결과, 조회 일시, 처리 채널 |  |  |
| POL-MBR-LEAVE-004-01 | PG-MBR-LEAVE-004 | 소멸 대상 자산 | 포인트, 쿠폰, 멤버십 혜택, 이벤트 응모권 |  |  |
| POL-MBR-LEAVE-004-03 | PG-MBR-LEAVE-004 | 소멸 예외 대상 | 법정 보관 대상, 환불 대상 자산 |  |  |
| POL-MBR-LEAVE-004-04 | PG-MBR-LEAVE-004 | 사용 유도 대상 | 잔여 포인트, 잔여 쿠폰 |  |  |
| POL-MBR-LEAVE-004-07 | PG-MBR-LEAVE-004 | 보유 자산 조회 기준 | 고객ID, CI |  |  |
| POL-MBR-LEAVE-004-08 | PG-MBR-LEAVE-004 | 보유 자산 조회 시스템 | BSS, 멤버십 시스템, 쿠폰 시스템 |  |  |
| POL-MBR-LEAVE-004-12 | PG-MBR-LEAVE-004 | 안내 이력 저장 항목 | 고객ID, 자산 유형, 안내 일시, 확인 일시, 처리 채널 |  |  |
| POL-MBR-LEAVE-005-01 | PG-MBR-LEAVE-005 | 영향 대상 서비스 | T 멤버십, T 우주, T 다이렉트샵, 구독, 결합, 제휴 서비스 |  |  |
| POL-MBR-LEAVE-005-02 | PG-MBR-LEAVE-005 | 자동 해지 대상 서비스 | 회원 기반 알림 설정, 개인화 설정, 내부 구독 연계 정보 |  |  |
| POL-MBR-LEAVE-005-03 | PG-MBR-LEAVE-005 | 별도 해지 필요 서비스 | 제휴사 직접 가입 서비스, 외부 계정 연결 서비스 |  |  |
| POL-MBR-LEAVE-005-09 | PG-MBR-LEAVE-005 | 영향 조회 수행 시스템 | BSS, 구독 시스템, 멤버십 시스템, 제휴 연동 시스템 |  |  |
| POL-MBR-LEAVE-006-01 | PG-MBR-LEAVE-006 | 필수 확인 항목 | 탈퇴 제한 항목, 소멸 자산, 연계 서비스 영향, 데이터 보관·파기, 재가입 기준 |  |  |
| POL-MBR-LEAVE-006-02 | PG-MBR-LEAVE-006 | 확인 방식 | 항목별 체크, 전체 확인 |  |  |
| POL-MBR-LEAVE-006-05 | PG-MBR-LEAVE-006 | 확인 유효시간 | 동일 세션 |  |  |
| POL-MBR-LEAVE-006-09 | PG-MBR-LEAVE-006 | 확인 증적 저장 항목 | 고객ID, 확인 항목, 확인 일시, 처리 채널, 세션ID |  |  |
| POL-MBR-LEAVE-006-10 | PG-MBR-LEAVE-006 | 확인 이력 보관 기간 | 탈퇴 완료 후 5년 |  |  |
| POL-MBR-LEAVE-007-02 | PG-MBR-LEAVE-007 | 탈퇴 최종 동의 방식 | 체크박스, 탈퇴 버튼 선택 |  |  |
| POL-MBR-LEAVE-007-04 | PG-MBR-LEAVE-007 | 동의 유효시간 | 동일 세션 |  |  |
| POL-MBR-LEAVE-007-05 | PG-MBR-LEAVE-007 | 동의 전 필수 조건 | 추가 인증 완료, 탈퇴 전 안내 확인 완료, 탈퇴 가능 판정 통과 |  |  |
| POL-MBR-LEAVE-007-10 | PG-MBR-LEAVE-007 | 동의 증적 저장 항목 | 고객ID, 동의 문구, 동의 일시, 처리 채널, 세션ID |  |  |
| POL-MBR-LEAVE-007-11 | PG-MBR-LEAVE-007 | 동의 이력 보관 기간 | 탈퇴 완료 후 5년 |  |  |
| POL-MBR-LEAVE-008-01 | PG-MBR-LEAVE-008 | 전환 전 상태 | 정상 |  |  |
| POL-MBR-LEAVE-008-02 | PG-MBR-LEAVE-008 | 탈퇴 요청 후 상태 | 탈퇴유예 |  |  |
| POL-MBR-LEAVE-008-03 | PG-MBR-LEAVE-008 | 유예 종료 후 상태 | 탈퇴 |  |  |
| POL-MBR-LEAVE-008-04 | PG-MBR-LEAVE-008 | 상태 전환 조건 | 탈퇴 최종 동의 완료, 추가 인증 성공, 탈퇴 가능 판정 통과 |  |  |
| POL-MBR-LEAVE-008-05 | PG-MBR-LEAVE-008 | 상태 전환 처리 시스템 | BSS |  |  |
| POL-MBR-LEAVE-008-08 | PG-MBR-LEAVE-008 | 상태 전환 실패 처리 | 업무 중단 및 오류 안내 |  |  |
| POL-MBR-LEAVE-008-09 | PG-MBR-LEAVE-008 | 상태 코드 | ACTIVE, DORMANT, LEAVE_PENDING, LEAVED, BLOCKED |  |  |
| POL-MBR-LEAVE-008-10 | PG-MBR-LEAVE-008 | 상태 전환 이력 저장 여부 | 저장 |  |  |
| POL-MBR-LEAVE-008-11 | PG-MBR-LEAVE-008 | 상태 전환 이력 저장 항목 | 고객ID, 이전 상태, 변경 상태, 처리 일시, 처리 채널, 처리 결과 코드 |  |  |
| POL-MBR-LEAVE-008-12 | PG-MBR-LEAVE-008 | 상태 전환 결과 코드 | 성공, 실패, 오류 |  |  |
| POL-MBR-LEAVE-009-01 | PG-MBR-LEAVE-009 | 세션 종료 대상 | 현재 세션, 전체 로그인 세션 |  |  |
| POL-MBR-LEAVE-009-02 | PG-MBR-LEAVE-009 | 토큰 폐기 대상 | 인증 토큰, 갱신 토큰, 자동 로그인 토큰 |  |  |
| POL-MBR-LEAVE-009-10 | PG-MBR-LEAVE-009 | 세션 종료 이력 저장 항목 | 고객ID, 세션ID, 종료 대상, 종료 일시, 종료 결과 코드 |  |  |
| POL-MBR-LEAVE-009-11 | PG-MBR-LEAVE-009 | 종료 결과 코드 | 성공, 실패, 오류 |  |  |
| POL-MBR-LEAVE-010-01 | PG-MBR-LEAVE-010 | 보관 대상 데이터 | 법정 보관 대상 거래 기록, 탈퇴 이력, 동의 이력 |  |  |
| POL-MBR-LEAVE-010-02 | PG-MBR-LEAVE-010 | 파기 대상 데이터 | 법정 보관 대상 제외 개인정보, 마케팅 수신 정보, 개인화 설정 |  |  |
| POL-MBR-LEAVE-010-04 | PG-MBR-LEAVE-010 | 법정 보관 기간 | 전자상거래 거래기록 5년, 소비자 불만·분쟁처리 기록 3년, 접속기록 3개월 |  |  |
| POL-MBR-LEAVE-010-05 | PG-MBR-LEAVE-010 | 즉시 파기 대상 | 자동 로그인 토큰, 개인화 설정, 마케팅 수신 설정 |  |  |
| POL-MBR-LEAVE-010-09 | PG-MBR-LEAVE-010 | 보관 기간 만료 후 처리 | 파기 |  |  |
| POL-MBR-LEAVE-010-11 | PG-MBR-LEAVE-010 | 파기 이력 저장 항목 | 고객ID, 데이터 유형, 파기 일시, 파기 결과 코드, 처리 시스템 |  |  |
| POL-MBR-LEAVE-010-12 | PG-MBR-LEAVE-010 | 데이터 처리 결과 코드 | 보관, 분리 보관, 파기, 오류 |  |  |
| POL-MBR-LEAVE-011-04 | PG-MBR-LEAVE-011 | 유예 중 회원 상태 | 탈퇴유예 |  |  |
| POL-MBR-LEAVE-011-07 | PG-MBR-LEAVE-011 | 철회 가능 조건 | 유예 기간 내, 본인인증 성공 |  |  |
| POL-MBR-LEAVE-011-08 | PG-MBR-LEAVE-011 | 철회 불가 조건 | 유예 기간 만료, 탈퇴 확정 처리 완료 |  |  |
| POL-MBR-LEAVE-011-09 | PG-MBR-LEAVE-011 | 철회 요청 채널 | 앱, 모바일웹, PC웹 |  |  |
| POL-MBR-LEAVE-011-12 | PG-MBR-LEAVE-011 | 철회 이력 저장 항목 | 고객ID, 철회 요청 일시, 처리 채널, 처리 결과 코드 |  |  |
| POL-MBR-LEAVE-012-01 | PG-MBR-LEAVE-012 | 안내 대상 | 탈퇴 요청 완료 고객, 탈퇴 확정 고객 |  |  |
| POL-MBR-LEAVE-012-02 | PG-MBR-LEAVE-012 | 안내 시점 | 탈퇴 요청 완료 즉시, 유예 종료 시 |  |  |
| POL-MBR-LEAVE-012-03 | PG-MBR-LEAVE-012 | 안내 채널 | 화면, 앱 푸시, SMS |  |  |
| POL-MBR-LEAVE-012-04 | PG-MBR-LEAVE-012 | 결과 안내 항목 | 탈퇴 요청 완료, 유예 기간, 철회 경로, 데이터 보관·파기 기준, 재가입 기준, 후속 문의 경로 |  |  |
| POL-MBR-LEAVE-012-06 | PG-MBR-LEAVE-012 | 데이터 보관 안내 항목 | 법정 보관 대상, 파기 대상, 보관 기간 |  |  |
| POL-MBR-LEAVE-012-07 | PG-MBR-LEAVE-012 | 후속 문의 경로 | 고객센터, 1:1 문의 |  |  |
| POL-MBR-LEAVE-012-08 | PG-MBR-LEAVE-012 | 발송 제외 조건 | 고객 연락처 부재, 알림 수신 불가 상태 |  |  |
| POL-MBR-LEAVE-012-11 | PG-MBR-LEAVE-012 | 결과 안내 이력 저장 항목 | 고객ID, 안내 유형, 안내 채널, 발송 일시, 발송 결과 코드 |  |  |
| POL-MBR-LOGIN-001-01 | PG-MBR-LOGIN-001 | 로그인 가능 상태 | 정상 |  |  |
| POL-MBR-LOGIN-001-02 | PG-MBR-LOGIN-001 | 휴면 진입 상태 | 휴면 |  |  |
| POL-MBR-LOGIN-001-03 | PG-MBR-LOGIN-001 | 탈퇴 상태 로그인 처리 | 재가입 안내 |  |  |
| POL-MBR-LOGIN-001-04 | PG-MBR-LOGIN-001 | 가입제한 상태 로그인 처리 | 로그인 불가 안내 |  |  |
| POL-MBR-LOGIN-001-08 | PG-MBR-LOGIN-001 | 로그인 상태 조회 기준 | 고객ID, CI |  |  |
| POL-MBR-LOGIN-001-10 | PG-MBR-LOGIN-001 | 로그인 이력 저장 항목 | 고객ID, 로그인 일시, 로그인 채널, 결과 코드 |  |  |
| POL-MBR-PROF-001-01 | PG-MBR-PROF-001 | 초기 프로필 항목 | 이름, 휴대폰번호, 이메일, 생년월일, 성별, 고객 유형 |  |  |
| POL-MBR-PROF-001-08 | PG-MBR-PROF-001 | 초기 권한 상태 | 일반 회원 |  |  |
| POL-MBR-PROF-001-11 | PG-MBR-PROF-001 | 프로필 변경 가능 항목 | 이메일, 연락처, 마케팅 수신 설정 |  |  |
| POL-MBR-PROF-001-12 | PG-MBR-PROF-001 | 프로필 변경 불가 항목 | CI, DI |  |  |
| POL-MBR-REJOIN-001-01 | PG-MBR-REJOIN-001 | 복원 가능 상태 | 탈퇴 철회 가능 상태 |  |  |
| POL-MBR-REJOIN-001-02 | PG-MBR-REJOIN-001 | 복원 불가 상태 | 탈퇴 확정, 가입제한 |  |  |
| POL-MBR-REJOIN-001-04 | PG-MBR-REJOIN-001 | 복원 대상 데이터 | 회원 계정, 기본 프로필, 약관 동의 이력, 알림 설정 |  |  |
| POL-MBR-REJOIN-001-10 | PG-MBR-REJOIN-001 | 판정 이력 저장 항목 | CI, 회원ID, 판정 결과, 판정 일시, 처리 채널 |  |  |
| POL-MBR-REJOIN-002-01 | PG-MBR-REJOIN-002 | 재가입 가능 상태 | 탈퇴 확정 |  |  |
| POL-MBR-REJOIN-002-02 | PG-MBR-REJOIN-002 | 재가입 불가 상태 | 정상, 휴면, 가입제한 |  |  |
| POL-MBR-REJOIN-002-05 | PG-MBR-REJOIN-002 | 중복 계정 기준 | CI, DI, 휴대폰번호 |  |  |
| POL-MBR-REJOIN-002-06 | PG-MBR-REJOIN-002 | 재가입 가능 채널 | 앱, 모바일웹, PC웹 |  |  |
| POL-MBR-REJOIN-002-07 | PG-MBR-REJOIN-002 | 재가입 불가 안내 항목 | 불가 사유, 후속 문의 경로 |  |  |
| POL-MBR-REJOIN-002-10 | PG-MBR-REJOIN-002 | 판정 이력 저장 항목 | CI, 회원ID, 판정 결과, 판정 일시, 처리 채널 |  |  |
| POL-MBR-REJOIN-003-01 | PG-MBR-REJOIN-003 | 제한 대상 | 가입제한 회원, 부정이용 이력 회원, 법령·약관 위반 회원 |  |  |
| POL-MBR-REJOIN-003-02 | PG-MBR-REJOIN-003 | 제한 기준 식별자 | CI, DI |  |  |
| POL-MBR-REJOIN-003-06 | PG-MBR-REJOIN-003 | 예외 승인 증빙 | 본인확인 결과, 제한 해소 증빙, 운영자 승인 사유 |  |  |
| POL-MBR-REJOIN-003-10 | PG-MBR-REJOIN-003 | 예외 이력 저장 항목 | 고객ID, CI, 승인자ID, 승인 일시, 승인 결과 |  |  |
| POL-MBR-REJOIN-004-01 | PG-MBR-REJOIN-004 | 복원 처리 조건 | 탈퇴 철회 가능 상태, 복원 가능 판정 통과 |  |  |
| POL-MBR-REJOIN-004-02 | PG-MBR-REJOIN-004 | 신규 생성 조건 | 탈퇴 확정, 재가입 가능 판정 통과 |  |  |
| POL-MBR-REJOIN-004-03 | PG-MBR-REJOIN-004 | 기존 회원ID 재사용 여부 | 탈퇴 철회 가능 상태에서는 재사용, 탈퇴 확정 후 재가입 시 재사용 불가 |  |  |
| POL-MBR-REJOIN-004-05 | PG-MBR-REJOIN-004 | CI/DI 연결 기준 | CI, DI |  |  |
| POL-MBR-REJOIN-004-06 | PG-MBR-REJOIN-004 | 기존 데이터 연결 대상 | 본인인증 결과 항목, 고객 식별 이력, 법정 보관 대상 이력 |  |  |
| POL-MBR-REJOIN-004-07 | PG-MBR-REJOIN-004 | 신규 생성 초기화 대상 | 기본 프로필, 약관 동의 이력, 수신 설정 |  |  |
| POL-MBR-REJOIN-004-08 | PG-MBR-REJOIN-004 | 실패 롤백 대상 | 계정 생성, CI/DI 매핑, 프로필 생성 |  |  |
| POL-MBR-REJOIN-004-11 | PG-MBR-REJOIN-004 | 처리 결과 저장 항목 | 고객ID, CI, 처리 유형, 처리 일시, 처리 결과 코드 |  |  |
| POL-MBR-REJOIN-005-01 | PG-MBR-REJOIN-005 | 완료 안내 항목 | 재가입 완료 여부, 이용 가능 상태, 로그인 상태, 후속 안내 |  |  |
| POL-MBR-REJOIN-005-02 | PG-MBR-REJOIN-005 | 통지 채널 | 앱 푸시, SMS, 이메일 |  |  |
| POL-MBR-REJOIN-005-05 | PG-MBR-REJOIN-005 | 이용 가능 상태 | 정상 |  |  |
| POL-MBR-REJOIN-005-09 | PG-MBR-REJOIN-005 | 재가입 이력 저장 항목 | 고객ID, CI, 재가입 일시, 처리 채널, 처리 결과 코드 |  |  |
| POL-MBR-ROUTE-001-01 | PG-MBR-ROUTE-001 | 미가입 상태 이동 경로 | 개인정보 입력 |  |  |
| POL-MBR-ROUTE-001-06 | PG-MBR-ROUTE-001 | 상태 조회 실패 이동 경로 | 오류 안내 |  |  |
| POL-MBR-ROUTE-001-07 | PG-MBR-ROUTE-001 | 고객 선택 가능 분기 | 로그인, 회원 가입, 재가입 |  |  |
| POL-MBR-ROUTE-001-09 | PG-MBR-ROUTE-001 | 분기 처리 시스템 | 채널, BSS |  |  |
| POL-MBR-ROUTE-001-11 | PG-MBR-ROUTE-001 | 분기 이력 저장 항목 | 고객ID, 상태 코드, 분기 경로, 처리 일시 |  |  |
| POL-MBR-SESS-001-03 | PG-MBR-SESS-001 | 가입 완료 후 로그인 상태 | 자동 로그인 |  |  |
| POL-MBR-SESS-001-04 | PG-MBR-SESS-001 | 세션 유효시간 | 24시간 |  |  |
| POL-MBR-SESS-001-06 | PG-MBR-SESS-001 | 세션 생성 기준 정보 | 고객ID, 인증 결과, 가입 완료 상태 |  |  |
| POL-MBR-SESS-001-09 | PG-MBR-SESS-001 | 임시 세션 데이터 삭제 대상 | 인증 세션, 입력 임시저장 정보 |  |  |
| POL-MBR-SESS-001-11 | PG-MBR-SESS-001 | 세션 전환 이력 저장 항목 | 고객ID, 전환 일시, 전환 결과 코드, 채널 |  |  |
| POL-MBR-SESS-002-01 | PG-MBR-SESS-002 | 세션 복구 조건 | 휴면 상태 전환 완료, 데이터 복원 완료 |  |  |
| POL-MBR-SESS-002-05 | PG-MBR-SESS-002 | 세션 유효시간 | 24시간 |  |  |
| POL-MBR-SESS-002-08 | PG-MBR-SESS-002 | 세션 저장 항목 | 고객ID, 세션ID, 생성 일시, 만료 일시, 채널 |  |  |
| POL-MBR-SESS-002-09 | PG-MBR-SESS-002 | 세션 보안 검증 항목 | 기기 식별값, 접속 IP, 토큰 유효성 |  |  |
| POL-MBR-SESS-002-11 | PG-MBR-SESS-002 | 세션 복구 이력 저장 항목 | 고객ID, 세션ID, 처리 일시, 처리 결과 코드 |  |  |
| POL-MBR-SESS-002-12 | PG-MBR-SESS-002 | 세션 복구 결과 코드 | 성공, 실패, 오류 |  |  |
| POL-MBR-STAT-001-01 | PG-MBR-STAT-001 | 상태 코드 | 미가입, 정상, 휴면, 탈퇴, 가입제한 |  |  |
| POL-MBR-STAT-001-02 | PG-MBR-STAT-001 | 상태 조회 기준 식별정보 | CI, DI, 고객ID |  |  |
| POL-MBR-STAT-001-03 | PG-MBR-STAT-001 | 상태 조회 수행 시스템 | BSS |  |  |
| POL-MBR-STAT-001-04 | PG-MBR-STAT-001 | 정상 상태 후속 처리 | 업무 계속 진행 |  |  |
| POL-MBR-STAT-001-05 | PG-MBR-STAT-001 | 휴면 상태 후속 처리 | 휴면 해제 프로세스 이동 |  |  |
| POL-MBR-STAT-001-06 | PG-MBR-STAT-001 | 탈퇴 상태 후속 처리 | 재가입 가능 여부 확인 |  |  |
| POL-MBR-STAT-001-07 | PG-MBR-STAT-001 | 가입제한 상태 후속 처리 | 가입 불가 안내 |  |  |
| POL-MBR-STAT-001-08 | PG-MBR-STAT-001 | 미가입 상태 후속 처리 | 신규 가입 가능 여부 확인 |  |  |
| POL-MBR-STAT-001-09 | PG-MBR-STAT-001 | 상태 불일치 처리 | BSS 기준 적용 |  |  |
| POL-MBR-STAT-001-11 | PG-MBR-STAT-001 | 상태 조회 이력 저장 여부 | 저장 |  |  |
| POL-MBR-STAT-001-12 | PG-MBR-STAT-001 | 상태 조회 이력 저장 항목 | 고객ID, CI 해시, 조회 일시, 조회 채널, 조회 결과 코드 |  |  |
| POL-MBR-TERM-001-01 | PG-MBR-TERM-001 | 회원 가입 필수 약관 | 서비스 이용약관, 개인정보 수집·이용 동의 |  |  |
| POL-MBR-TERM-001-02 | PG-MBR-TERM-001 | 회원 가입 선택 약관 | 마케팅 정보 수신 동의, 맞춤형 혜택 제공 동의 |  |  |
| POL-MBR-TERM-001-03 | PG-MBR-TERM-001 | 휴면 해제 재동의 대상 약관 | 개정된 필수 약관, 개인정보 수집·이용 동의 |  |  |
| POL-MBR-TERM-001-04 | PG-MBR-TERM-001 | 재가입 필수 약관 | 서비스 이용약관, 개인정보 수집·이용 동의 |  |  |
| POL-MBR-TERM-001-05 | PG-MBR-TERM-001 | 재가입 선택 약관 | 마케팅 정보 수신 동의, 맞춤형 혜택 제공 동의 |  |  |
| POL-MBR-TERM-001-08 | PG-MBR-TERM-001 | 전체 동의 적용 범위 | 필수 약관, 선택 약관 |  |  |
| POL-MBR-TERM-001-11 | PG-MBR-TERM-001 | 약관 상세 노출 대상 | 약관 전문, 핵심 요약, 개정 이력 |  |  |
| POL-MBR-TERM-001-12 | PG-MBR-TERM-001 | 휴면 해제 선택 약관 재동의 여부 | 필수 약관은 변경 시 재동의, 선택 약관은 고객 선택에 따라 재동의 |  |  |
| POL-MBR-TERM-002-02 | PG-MBR-TERM-002 | 법정대리인 동의 대상 업무 | 회원 가입, 재가입 |  |  |
| POL-MBR-TERM-002-03 | PG-MBR-TERM-002 | 법정대리인 인증수단 | 휴대폰 본인인증, PASS 인증 |  |  |
| POL-MBR-TERM-002-05 | PG-MBR-TERM-002 | 법정대리인 동의 유효시간 | 요청 발송 후 24시간 |  |  |
| POL-MBR-TERM-002-08 | PG-MBR-TERM-002 | 법정대리인 동의 증적 저장 항목 | 고객 CI 해시, 법정대리인 CI 해시, 법정대리인 이름, 법정대리인 휴대폰번호, 고객과의 관계, 동의 일시… |  |  |
| POL-MBR-TERM-002-10 | PG-MBR-TERM-002 | 법정대리인 동의 결과 통지 대상 | 고객, 법정대리인 |  |  |
| POL-MBR-TERM-003-01 | PG-MBR-TERM-003 | 동의 이력 저장 항목 | 고객ID, 약관ID, 약관버전, 동의 여부, 동의 일시, 동의 채널, IP, 세션ID |  |  |
| POL-MBR-TERM-003-02 | PG-MBR-TERM-003 | 약관 버전 이력 저장 항목 | 약관ID, 약관명, 버전, 시행일, 종료일 |  |  |
| POL-MBR-TERM-003-03 | PG-MBR-TERM-003 | 동의 채널 | 앱, 모바일웹, PC웹 |  |  |
| POL-MBR-TERM-003-09 | PG-MBR-TERM-003 | 약관 동의 이력 보관 기간 | 회원 탈퇴 후 5년 또는 관계 법령상 보관 기간 중 긴 기간 |  |  |
| POL-MBR-TERM-003-10 | PG-MBR-TERM-003 | 약관 동의 이력 조회 권한 | 회원 정책 담당자, 회원 시스템 운영자, 감사 권한자 |  |  |
| POL-MBR-TERM-003-12 | PG-MBR-TERM-003 | 동의 이력 마스킹 대상 | CI, DI, 휴대폰번호, IP |  |  |

## 3. 핵심 코드 셋 (BSS 코드테이블 1:1 매핑 대상)

- 회원상태코드: `PG-MBR-LEAVE-008` 항목값 `ACTIVE/DORMANT/LEAVE_PENDING/LEAVED/BLOCKED` ↔ 온톨로지 `:State.code(MBR_*)`
- 가입제한 사유코드: `PG-MBR-JOIN-001` `DUPLICATE_CI/DORMANT_MEMBER/...`
- 인증 결과코드: `PG-MBR-AUTH-006` `SUCCESS/FAIL/CANCEL/ERROR/TIMEOUT`

> 주의: BSS 상태코드(LEAVED 등)와 온톨로지 State.code(MBR_*)는 명칭 체계가 달라 명시 매핑표 필수.

