-- ============================================================
-- 악성코드 스캐너 & 쇼핑몰 통합 웹 프로젝트
-- Oracle DB Schema (C##DBEXAM 사용자 기준)
-- 추출일: 2026-04-09
-- ============================================================

-- ============================================================
-- [0] 사용자 생성 (최초 설치 시에만 실행 / DBA 권한 필요)
-- ============================================================
-- CREATE USER C##DBEXAM IDENTIFIED BY m1234;
-- GRANT CONNECT, RESOURCE, DBA TO C##DBEXAM;

-- ============================================================
-- [1] 시퀀스 생성
-- ============================================================

CREATE SEQUENCE NJW_MEMBER_SEQ_NUM
    MINVALUE 1 MAXVALUE 9999999999999999999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

CREATE SEQUENCE NJW_BOARD_SEQ_NUM
    MINVALUE 1 MAXVALUE 9999999999999999999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

CREATE SEQUENCE NJW_PRODUCT_SEQ_NUM
    MINVALUE 1 MAXVALUE 9999999999999999999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

CREATE SEQUENCE NJW_CART_SEQ_NUM
    MINVALUE 1 MAXVALUE 9999999999999999999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

CREATE SEQUENCE NJW_REMEMBER_ME_SEQ_NUM
    MINVALUE 1 MAXVALUE 9999999999999999999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

CREATE SEQUENCE SCAN_SEQ
    MINVALUE 1 MAXVALUE 9999999999999999999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

-- ============================================================
-- [2] 테이블 생성
-- ============================================================

-- 회원 테이블
CREATE TABLE MEMBER (
    MEMBER_SEQ_ID   NUMBER          NOT NULL,         -- PK (시퀀스)
    ID              VARCHAR2(20)    NOT NULL UNIQUE,  -- 로그인 ID (4~20자)
    PASSWORD        VARCHAR2(100)   NOT NULL,          -- BCrypt 해시
    NAME            VARCHAR2(60)    NOT NULL,          -- 이름 (2~20자)
    EMAIL           VARCHAR2(50)    UNIQUE,            -- 이메일
    PHONE           VARCHAR2(50)    UNIQUE,            -- 전화번호 (숫자 10~11자리)
    UUID            RAW(16)         UNIQUE,            -- SYS_GUID()
    ROLE            VARCHAR2(10)    DEFAULT 'USER',   -- USER | ADMIN
    LAST_LOGIN_AT   DATE,                             -- 마지막 로그인 시각
    UPDATED_AT      DATE,                             -- 정보 수정 시각
    CONSTRAINT PK_MEMBER PRIMARY KEY (MEMBER_SEQ_ID)
);

-- 자동 로그인 토큰 테이블
CREATE TABLE REMEMBER_ME_TOKEN (
    TOKEN_ID        NUMBER          NOT NULL,          -- PK (시퀀스)
    MEMBER_ID       VARCHAR2(50)    NOT NULL,          -- MEMBER.ID 참조
    TOKEN_VALUE     VARCHAR2(255)   NOT NULL UNIQUE,  -- UUID 토큰
    EXPIRY_DATE     DATE            NOT NULL,          -- 만료일 (발급일 + 30일)
    IP_ADDRESS      VARCHAR2(500),                    -- 발급 시 IP
    USER_AGENT      VARCHAR2(1000),                   -- 발급 시 브라우저 정보
    CONSTRAINT PK_REMEMBER_ME_TOKEN PRIMARY KEY (TOKEN_ID),
    CONSTRAINT FK_RMT_MEMBER FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER (ID) ON DELETE CASCADE
);

-- 상품 테이블
CREATE TABLE PRODUCT (
    PRODUCT_SEQ_ID  NUMBER          NOT NULL,          -- PK (시퀀스)
    PRODUCT_CODE    VARCHAR2(10)    NOT NULL UNIQUE,  -- 상품 코드 (P + 숫자 2~9자리)
    PRODUCT_NAME    VARCHAR2(100)   NOT NULL,          -- 상품명 (3~12자)
    PRICE           NUMBER(38,0)    NOT NULL,          -- 가격 (양수)
    CATEGORY        VARCHAR2(20)    NOT NULL,          -- 카테고리 (최대 20자)
    DESCRIPTION     CLOB            NOT NULL,          -- 상품 설명
    IMAGE_NAME      VARCHAR2(200),                    -- 저장된 이미지 파일명 (UUID 형식)
    CREATED_AT      DATE            NOT NULL,          -- 등록일
    UUID            RAW(16)         NOT NULL UNIQUE,  -- SYS_GUID()
    CONSTRAINT PK_PRODUCT PRIMARY KEY (PRODUCT_SEQ_ID)
);

-- 장바구니 테이블
CREATE TABLE CART (
    CART_ID         NUMBER          NOT NULL,          -- PK (시퀀스)
    MEMBER_ID       VARCHAR2(50)    NOT NULL,          -- MEMBER.ID 참조
    PRODUCT_ID      VARCHAR2(50)    NOT NULL,          -- PRODUCT.PRODUCT_CODE 참조
    QUANTITY        NUMBER          DEFAULT 1,        -- 수량 (최소 1)
    PRICE           NUMBER(38,0)    NOT NULL,          -- 담을 당시 단가
    CREATED_AT      DATE            DEFAULT SYSDATE,  -- 담은 시각
    UUID            RAW(16)         DEFAULT SYS_GUID() UNIQUE,
    CONSTRAINT PK_CART PRIMARY KEY (CART_ID),
    CONSTRAINT FK_CART_MEMBER  FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER (ID) ON DELETE CASCADE,
    CONSTRAINT FK_CART_PRODUCT FOREIGN KEY (PRODUCT_ID)
        REFERENCES PRODUCT (PRODUCT_CODE) ON DELETE CASCADE
);

-- 게시판(문의) 테이블
CREATE TABLE BOARD (
    NUM             NUMBER          NOT NULL,          -- PK (시퀀스)
    MEMBER_ID       VARCHAR2(20)    NOT NULL,          -- MEMBER.ID 참조
    TITLE           VARCHAR2(100)   NOT NULL,          -- 제목 (최대 100자)
    CONTENT         CLOB,                             -- 본문
    VIEW_COUNT      NUMBER          DEFAULT 0,        -- 조회수
    IP              VARCHAR2(20),                     -- 작성자 IP
    UUID            RAW(16)         NOT NULL UNIQUE,  -- SYS_GUID()
    CREATED_AT      DATE            NOT NULL,          -- 작성일
    CONSTRAINT PK_BOARD PRIMARY KEY (NUM),
    CONSTRAINT FK_BOARD_MEMBER FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER (ID) ON DELETE CASCADE
);

-- 악성코드 스캔 기록 테이블
CREATE TABLE SCAN (
    NUM             NUMBER          NOT NULL,          -- PK (시퀀스)
    MEMBER_ID       VARCHAR2(20),                     -- MEMBER.ID 참조
    FILE_NAME       VARCHAR2(255)   NOT NULL,          -- 업로드된 파일 원본명
    SCAN_LOGTIME    DATE,                             -- 스캔 요청 시각
    CREATED_AT      DATE            DEFAULT SYSDATE,  -- DB 저장 시각
    MALICIOUS       NUMBER          DEFAULT 0,        -- 악성 탐지 수
    SUSPICIOUS      NUMBER          DEFAULT 0,        -- 의심 탐지 수
    UNDETECTED      NUMBER          DEFAULT 0,        -- 미탐지 수
    HARMLESS        NUMBER          DEFAULT 0,        -- 무해 판정 수
    UUID            RAW(16)         UNIQUE,            -- SYS_GUID()
    CONSTRAINT PK_SCAN PRIMARY KEY (NUM),
    CONSTRAINT FK_SCAN_MEMBER FOREIGN KEY (MEMBER_ID)
        REFERENCES MEMBER (ID) ON DELETE CASCADE
);

-- ============================================================
-- [3] 인덱스 생성 (조회 성능 최적화)
-- ============================================================

-- MEMBER: 이메일·전화번호 중복 검사용
CREATE UNIQUE INDEX IDX_MEMBER_EMAIL ON MEMBER (EMAIL);
CREATE UNIQUE INDEX IDX_MEMBER_PHONE ON MEMBER (PHONE);

-- BOARD: UUID 조회 + 작성자 JOIN
CREATE UNIQUE INDEX IDX_BOARD_UUID      ON BOARD (UUID);
CREATE        INDEX IDX_BOARD_MEMBER_ID ON BOARD (MEMBER_ID);

-- PRODUCT: UUID·코드 조회 + 최신순 정렬
CREATE UNIQUE INDEX IDX_PRODUCT_UUID       ON PRODUCT (UUID);
CREATE UNIQUE INDEX IDX_PRODUCT_CODE       ON PRODUCT (PRODUCT_CODE);
CREATE        INDEX IDX_PRODUCT_CREATED_AT ON PRODUCT (CREATED_AT DESC);

-- CART: 중복 담기 체크 (FOR UPDATE) + 목록 조회
CREATE INDEX IDX_CART_MEMBER_PRODUCT ON CART (MEMBER_ID, PRODUCT_ID);

-- SCAN: 사용자별 스캔 이력 조회 + NUM 역순 정렬
CREATE INDEX IDX_SCAN_MEMBER_NUM ON SCAN (MEMBER_ID, NUM DESC);

-- REMEMBER_ME_TOKEN: 토큰 조회·삭제·만료 정리
CREATE UNIQUE INDEX IDX_RMT_TOKEN_VALUE ON REMEMBER_ME_TOKEN (TOKEN_VALUE);
CREATE        INDEX IDX_RMT_MEMBER_ID   ON REMEMBER_ME_TOKEN (MEMBER_ID);
CREATE        INDEX IDX_RMT_EXPIRY      ON REMEMBER_ME_TOKEN (EXPIRY_DATE);

-- ============================================================
-- [4] 초기 관리자 계정 (비밀번호: admin1234 / BCrypt 해시)
-- ============================================================
INSERT INTO MEMBER (MEMBER_SEQ_ID, ID, PASSWORD, NAME, EMAIL, PHONE, UUID, ROLE, LAST_LOGIN_AT, UPDATED_AT)
VALUES (
    NJW_MEMBER_SEQ_NUM.NEXTVAL,
    'admin',
    '$2a$10$oM6y9Tg8sml28r.4GcwzXub7k78Co0mQjSz0ZlMUN5xX8iu6.Goq.',
    '관리자',
    'admin@admin.com',
    '01000000000',
    SYS_GUID(),
    'ADMIN',
    SYSDATE,
    SYSDATE
);

COMMIT;

-- ============================================================
-- [5] 설치 확인 쿼리
-- ============================================================
SELECT table_name FROM user_tables ORDER BY table_name;
SELECT sequence_name FROM user_sequences ORDER BY sequence_name;
SELECT index_name, table_name, uniqueness FROM user_indexes
WHERE table_name IN ('MEMBER','BOARD','PRODUCT','CART','SCAN','REMEMBER_ME_TOKEN')
ORDER BY table_name, index_name;
