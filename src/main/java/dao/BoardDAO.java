package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dto.BoardDTO;
import dto.MemberDTO;
import dto.ProductDTO;
import util.DBManager;

public class BoardDAO {
	private static BoardDAO instance = new BoardDAO();

	public static BoardDAO getInstance() {
		return instance;
	}

	// 페이징 처리를 위한 리스트 조회 메서드
	public List<BoardDTO> selectAll(int page, int pageSize) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardDTO> list = new ArrayList<>();

		// 1. 오라클 페이징 계산 (ROWNUM은 1부터 시작)
		int startNum = (page - 1) * pageSize + 1; // 시작 번호 (예: 1, 11, 21...)
		int endNum = page * pageSize; // 끝 번호 (예: 10, 20, 30...)

		// 2. Oracle용 페이징 쿼리 (3중 쿼리 구조)
		// 설명:
		// - 안쪽: 게시글을 번호 역순(최신순)으로 정렬
		// - 중간: ROWNUM을 붙여서 끝 번호(endNum)까지 자름
		// - 바깥: 시작 번호(startNum) 이상인 것만 가져옴
		String sql = "SELECT * FROM (" + "    SELECT A.*, ROWNUM AS RNUM " + "    FROM ("
				+ "        SELECT * FROM board ORDER BY num DESC" + "    ) A " + "    WHERE ROWNUM <= ? "
				+ ") WHERE RNUM >= ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, endNum); // 먼저 끝 번호를 세팅 (안쪽 WHERE절)
			pstmt.setInt(2, startNum); // 나중에 시작 번호를 세팅 (바깥 WHERE절)

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardDTO dto = new BoardDTO();

				// DB 컬럼 -> DTO 매핑
				// (이전 대화의 PDF 이미지와 DTO 기준)
				dto.setId(rs.getInt("num")); // DB: num -> DTO: id
				dto.setMemberId(rs.getString("id")); // DB: id -> DTO: memberId
				dto.setMemberName(rs.getString("name")); // DB: name -> DTO: memberName
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHit(rs.getInt("hit"));
				dto.setIp(rs.getString("ip"));

				// 날짜 처리 (오라클 DATE 타입 -> 문자열 변환)
				// 시간까지 필요하면 getTimestamp, 날짜만 필요하면 getDate
				// DTO의 date 필드가 String이므로 변환해서 넣습니다.
				dto.setDate(rs.getString("regist_day"));

				list.add(dto);
			}

		} catch (Exception e) {
			System.out.println("게시글 목록 조회 중 에러 발생!");
			e.printStackTrace();
		} finally {
			// DBManager에 close(conn, pstmt, rs) 메서드가 있다면 그것을 사용하고,
			// 없다면 아래처럼 각각 닫거나 수정하셔야 합니다.
			DBManager.close(conn, pstmt, rs);
		}

		return list;
	}

	public int countAll() {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(*) FROM board"; // 전체 글 개수 세기

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return count;
	}

	public BoardDTO getBoardById(String boardId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDTO board = null;

		String sql = "SELECT * FROM board WHERE num = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				board = new BoardDTO();
				board.setId(rs.getInt("num"));
				board.setMemberId(rs.getString("id"));
				board.setMemberName(rs.getString("name"));
				board.setSubject(rs.getString("subject"));
				board.setContent(rs.getString("content"));
				board.setDate(rs.getString("regist_day"));
				board.setHit(rs.getInt("hit"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return board;
	}

	public int addPost(BoardDTO dto) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "INSERT INTO board VALUES(njw_board_seq_num.nextval, ?, ?, ?, ?, SYSDATE, ?, ?, SYS_GUID())";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getMemberId());
			pstmt.setString(2, dto.getMemberName());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getHit());
			pstmt.setString(6, dto.getIp());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("상품 등록 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}

		return result;
	}

	public int updatePost(BoardDTO dto) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		// [핵심] INSERT -> UPDATE로 변경
		// 컬럼명(name, mail, phone)은 실제 DB 테이블의 컬럼명과 일치해야 합니다.
		String sql = "UPDATE board SET subject = ?, content = ? WHERE num = ?";
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());

			// [중요] WHERE절의 ID (누구를 수정할지 기준)
			pstmt.setInt(3, dto.getId());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("상품 정보 수정 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}

		return result;
	}

	public void deleteBoard(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "DELETE FROM board WHERE num = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("글 삭제 완료");
			}
		} catch (Exception e) {
			System.out.println("글 삭제 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}
	}
	
	public int increaseHit(BoardDTO dto) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		// [핵심] INSERT -> UPDATE로 변경
		// 컬럼명(name, mail, phone)은 실제 DB 테이블의 컬럼명과 일치해야 합니다.
		String sql = "UPDATE board SET hit = board.hit + 1 WHERE num = ?";
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			// [중요] WHERE절의 ID (누구를 수정할지 기준)
			pstmt.setInt(1, dto.getId());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("상품 정보 수정 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}

		return result;
	}
}