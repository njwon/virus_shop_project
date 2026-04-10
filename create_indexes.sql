--------------------------------------------------------
-- 인덱스 생성 스크립트
-- 각 테이블의 자주 사용하는 쿼리 기준으로 최적화
-- 실행 전: 기존 인덱스 중복 여부를 아래 확인 쿼리로 점검
--   SELECT index_name, table_name, column_name
--   FROM user_ind_columns
--   ORDER BY table_name, index_name;
--------------------------------------------------------


-- ============================================================
-- [1] MEMBER 테이블
-- 사용 쿼리:
--   WHERE id = ?                             → PK (이미 인덱스 있음)
--   WHERE email = ? [AND id != ?]            → 회원가입/정보수정 중복 검사
--   WHERE phone = ? [AND id != ?]            → 회원가입/정보수정 중복 검사
-- ============================================================
CREATE UNIQUE INDEX IDX_MEMBER_EMAIL
    ON MEMBER(email);

CREATE UNIQUE INDEX IDX_MEMBER_PHONE
    ON MEMBER(phone);


-- ============================================================
-- [2] board 테이블
-- 사용 쿼리:
--   WHERE b.uuid = HEXTORAW(?)              → 게시글 상세/수정폼/수정/삭제
--   JOIN MEMBER m ON b.member_id = m.id    → 목록/상세 JOIN
--   ORDER BY b.num DESC                    → 목록 정렬 (PK 역방향 스캔)
-- ============================================================
-- uuid는 RAW(16) 컬럼, HEXTORAW(?)는 바인드 변수에만 적용되므로 일반 인덱스 사용 가능
CREATE UNIQUE INDEX IDX_BOARD_UUID
    ON board(uuid);

CREATE INDEX IDX_BOARD_MEMBER_ID
    ON board(member_id);


-- ============================================================
-- [3] product 테이블
-- 사용 쿼리:
--   WHERE uuid = HEXTORAW(?)               → 상품 상세/수정폼/삭제
--   WHERE product_code = ?                 → 상품 조회/수정/삭제
--   ORDER BY created_at DESC               → 전체 상품 목록 정렬
-- ============================================================
CREATE UNIQUE INDEX IDX_PRODUCT_UUID
    ON product(uuid);

-- product_code는 비즈니스 키 역할, 없으면 UNIQUE 제약 추가 권장
CREATE UNIQUE INDEX IDX_PRODUCT_CODE
    ON product(product_code);

CREATE INDEX IDX_PRODUCT_CREATED_AT
    ON product(created_at DESC);


-- ============================================================
-- [4] Cart 테이블
-- 사용 쿼리:
--   WHERE member_id = ? AND product_id = ? FOR UPDATE   → addCart 중복 체크 (트랜잭션)
--   WHERE member_id = ?                                 → getCartList, deleteCartByUserId
-- 복합 인덱스가 member_id 단일 조건도 선행 컬럼으로 커버
-- ============================================================
CREATE INDEX IDX_CART_MEMBER_PRODUCT
    ON Cart(member_id, product_id);


-- ============================================================
-- [5] SCAN 테이블
-- 사용 쿼리:
--   WHERE member_id = ? ORDER BY num DESC  → getHistory (사용자별 스캔 이력)
--   WHERE num = ?                          → getScanOwner, deleteScan (PK, 이미 인덱스 있음)
-- 복합 인덱스: member_id 조건 + num 내림차순 정렬을 인덱스만으로 처리
-- ============================================================
CREATE INDEX IDX_SCAN_MEMBER_NUM
    ON SCAN(member_id, num DESC);


-- ============================================================
-- [6] REMEMBER_ME_TOKEN 테이블
-- 사용 쿼리:
--   WHERE token_value = ? AND expiry_date > SYSDATE  → checkRememberMeToken
--   WHERE token_value = ?                            → deleteRememberMeToken
--   WHERE user_id = ?                                → deleteAllRememberMeTokensByUserId
-- ============================================================
-- token_value는 UUID 문자열, 유일값이므로 UNIQUE 인덱스
CREATE UNIQUE INDEX IDX_RMT_TOKEN_VALUE
    ON REMEMBER_ME_TOKEN(token_value);

CREATE INDEX IDX_RMT_USER_ID
    ON REMEMBER_ME_TOKEN(user_id);

-- expiry_date 단독 인덱스: 만료 토큰 배치 정리 시 유용
CREATE INDEX IDX_RMT_EXPIRY
    ON REMEMBER_ME_TOKEN(expiry_date);


-- ============================================================
-- 인덱스 생성 결과 확인
-- ============================================================
SELECT
    i.index_name,
    i.table_name,
    i.uniqueness,
    LISTAGG(ic.column_name || ' ' || ic.descend, ', ')
        WITHIN GROUP (ORDER BY ic.column_position) AS columns
FROM user_indexes i
JOIN user_ind_columns ic ON i.index_name = ic.index_name
WHERE i.table_name IN ('MEMBER', 'BOARD', 'PRODUCT', 'CART', 'SCAN', 'REMEMBER_ME_TOKEN')
GROUP BY i.index_name, i.table_name, i.uniqueness
ORDER BY i.table_name, i.index_name;
