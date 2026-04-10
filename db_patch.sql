--------------------------------------------------------
-- DB 구조 개선 패치 스크립트
-- 적용 순서대로 실행할 것
--------------------------------------------------------

-- ============================================================
-- [1] BOARD.ID VARCHAR2(10) -> VARCHAR2(20)
--     NJW_MEMBER.ID(20)를 참조하는 FK인데 크기가 작아 truncation 위험
-- ============================================================
-- FK 먼저 삭제
ALTER TABLE "C##DBEXAM"."BOARD" DROP CONSTRAINT "FK_BOARD_MEMBER";

-- 컬럼 크기 확장
ALTER TABLE "C##DBEXAM"."BOARD" MODIFY ("ID" VARCHAR2(20 BYTE));

-- FK 재생성
ALTER TABLE "C##DBEXAM"."BOARD" ADD CONSTRAINT "FK_BOARD_MEMBER"
    FOREIGN KEY ("ID") REFERENCES "C##DBEXAM"."NJW_MEMBER" ("ID")
    ON DELETE CASCADE ENABLE;

-- ============================================================
-- [2] BOARD.NAME VARCHAR2(10) -> VARCHAR2(60)
--     NJW_MEMBER.NAME이 VARCHAR2(60)인데 BOARD.NAME은 10으로 너무 짧음
-- ============================================================
ALTER TABLE "C##DBEXAM"."BOARD" MODIFY ("NAME" VARCHAR2(60 BYTE));

-- ============================================================
-- [3] BOARD.REGIST_DAY VARCHAR2(30) -> DATE 타입으로 변경
--     날짜를 문자열로 저장하면 정렬/비교가 불안정함
-- ============================================================
-- 임시 DATE 컬럼 추가
ALTER TABLE "C##DBEXAM"."BOARD" ADD ("REGIST_DAY_TMP" DATE);

-- 기존 문자열 데이터를 DATE로 변환하여 복사 (형식: RR/MM/DD)
UPDATE "C##DBEXAM"."BOARD"
SET REGIST_DAY_TMP = TO_DATE(REGIST_DAY, 'RR/MM/DD')
WHERE REGIST_DAY IS NOT NULL;

-- NULL인 경우 SYSDATE로 채움
UPDATE "C##DBEXAM"."BOARD"
SET REGIST_DAY_TMP = SYSDATE
WHERE REGIST_DAY_TMP IS NULL;

-- 기존 VARCHAR2 컬럼 삭제
ALTER TABLE "C##DBEXAM"."BOARD" DROP COLUMN "REGIST_DAY";

-- 임시 컬럼을 REGIST_DAY로 이름 변경
ALTER TABLE "C##DBEXAM"."BOARD" RENAME COLUMN "REGIST_DAY_TMP" TO "REGIST_DAY";

-- NOT NULL 제약 추가
ALTER TABLE "C##DBEXAM"."BOARD" MODIFY ("REGIST_DAY" NOT NULL);

COMMIT;

-- ============================================================
-- [4] PRODUCT.DESCRIPTION VARCHAR2(1000) -> CLOB
--     긴 상품 설명은 VARCHAR2(1000) 한계를 초과할 수 있음
-- ============================================================
-- 임시 CLOB 컬럼 추가
ALTER TABLE "C##DBEXAM"."PRODUCT" ADD ("DESCRIPTION_TMP" CLOB);

-- 기존 데이터 복사
UPDATE "C##DBEXAM"."PRODUCT" SET DESCRIPTION_TMP = DESCRIPTION;

-- 기존 NOT NULL + VARCHAR2 컬럼 삭제
ALTER TABLE "C##DBEXAM"."PRODUCT" DROP COLUMN "DESCRIPTION";

-- 이름 변경
ALTER TABLE "C##DBEXAM"."PRODUCT" RENAME COLUMN "DESCRIPTION_TMP" TO "DESCRIPTION";

-- NOT NULL 제약 재추가
ALTER TABLE "C##DBEXAM"."PRODUCT" MODIFY ("DESCRIPTION" NOT NULL);

COMMIT;

-- ============================================================
-- [5] CART 테이블에 UUID 컬럼 추가
--     다른 테이블과 달리 CART만 UUID가 없었음
-- ============================================================
ALTER TABLE "C##DBEXAM"."CART" ADD ("UUID" RAW(16) DEFAULT SYS_GUID());

-- 기존 행에 UUID 채우기 (DEFAULT SYS_GUID()로 자동 생성됨)
-- UNIQUE 제약 추가
ALTER TABLE "C##DBEXAM"."CART" ADD UNIQUE ("UUID");

COMMIT;

-- ============================================================
-- [6] SCAN_HISTORY -> NJW_MEMBER FK 추가
--     scan_history.id가 njw_member.id를 참조하지만 FK 제약이 없었음
--     주의: 고아 데이터(member 삭제 후 남은 scan)가 있으면 먼저 정리 필요
-- ============================================================

-- 고아 스캔 기록 확인 (실행 후 0건이어야 FK 추가 가능)
-- SELECT * FROM "C##DBEXAM"."SCAN_HISTORY" s
-- WHERE NOT EXISTS (SELECT 1 FROM "C##DBEXAM"."NJW_MEMBER" m WHERE m.id = s.id);

-- 고아 데이터 삭제 (위 SELECT로 확인 후 필요시 실행)
DELETE FROM "C##DBEXAM"."SCAN_HISTORY" s
WHERE NOT EXISTS (
    SELECT 1 FROM "C##DBEXAM"."NJW_MEMBER" m WHERE m.id = s.id
);

-- FK 추가 (ON DELETE CASCADE: 회원 삭제 시 스캔 기록도 함께 삭제)
ALTER TABLE "C##DBEXAM"."SCAN_HISTORY" ADD CONSTRAINT "FK_SCAN_MEMBER"
    FOREIGN KEY ("ID") REFERENCES "C##DBEXAM"."NJW_MEMBER" ("ID")
    ON DELETE CASCADE ENABLE;

COMMIT;

-- ============================================================
-- 최종 확인 쿼리
-- ============================================================
-- 테이블 컬럼 목록 확인
SELECT table_name, column_name, data_type, data_length, nullable
FROM user_tab_columns
WHERE table_name IN ('BOARD', 'PRODUCT', 'CART', 'SCAN_HISTORY', 'NJW_MEMBER')
ORDER BY table_name, column_id;

-- FK 제약 확인
SELECT constraint_name, table_name, r_constraint_name, delete_rule
FROM user_constraints
WHERE constraint_type = 'R'
ORDER BY table_name;
