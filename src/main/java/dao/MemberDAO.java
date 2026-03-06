package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dto.MemberDTO;
import util.DBManager;
import util.PasswordManager;

public class MemberDAO {
	private static MemberDAO instance = new MemberDAO();

	public static MemberDAO getInstance() {
		return instance;
	}

	public MemberDTO getMember(String id) {
		MemberDTO member = null;

		ResultSet result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "SELECT id, password, name, mail, phone, role FROM NJW_MEMBER WHERE id = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeQuery();

			if (result.next()) {
				member = new MemberDTO();

				member.setId(result.getString("id"));
				member.setPw(result.getString("password"));
				member.setName(result.getString("name"));
				member.setMail(result.getString("mail"));
				member.setPhone(result.getString("phone"));
				member.setRole(result.getString("role"));
			}
		} catch (Exception e) {
			System.out.println("회원 정보 조회 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, result);
		}

		return member;
	}

	public boolean[] isIdDuplicate(String id, String mail, String phone) {
		Connection conn = null;
		PreparedStatement pstmtId = null;
		PreparedStatement pstmtMail = null;
		PreparedStatement pstmtPhone = null;

		ResultSet rs = null;
		final boolean[] isDuplicate = { false, false, false }; // [ID, Mail, Phone] 순서

		try {
			conn = DBManager.getConnection();

			String sqlId = "SELECT COUNT(*) FROM NJW_MEMBER WHERE id = ?";
			pstmtId = conn.prepareStatement(sqlId);
			pstmtId.setString(1, id);
			rs = pstmtId.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				isDuplicate[0] = true;
			}
			DBManager.close(null, pstmtId, rs);
			rs = null;

			String sqlMail = "SELECT COUNT(*) FROM NJW_MEMBER WHERE mail = ?";
			pstmtMail = conn.prepareStatement(sqlMail);
			pstmtMail.setString(1, mail);
			rs = pstmtMail.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				isDuplicate[1] = true;
			}
			DBManager.close(null, pstmtMail, rs);
			rs = null;

			String sqlPhone = "SELECT COUNT(*) FROM NJW_MEMBER WHERE phone = ?";
			pstmtPhone = conn.prepareStatement(sqlPhone);
			pstmtPhone.setString(1, phone);
			rs = pstmtPhone.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				isDuplicate[2] = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					DBManager.close(null, null, rs);
				if (pstmtId != null)
					DBManager.close(null, pstmtId, null);
				if (pstmtMail != null)
					DBManager.close(null, pstmtMail, null);
				if (pstmtPhone != null)
					DBManager.close(null, pstmtPhone, null);
				if (conn != null)
					DBManager.close(conn, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isDuplicate;
	}
	
	public boolean[] isIdDuplicateUpdate(String id, String mail, String phone) {
		Connection conn = null;
		PreparedStatement pstmtMail = null;
		PreparedStatement pstmtPhone = null;

		ResultSet rs = null;
		final boolean[] isDuplicate = { false, false }; // [ID, Mail, Phone] 순서

		try {
			conn = DBManager.getConnection();

			String sqlMail = "SELECT COUNT(*) FROM NJW_MEMBER WHERE mail = ? AND id != ?";
			pstmtMail = conn.prepareStatement(sqlMail);
			pstmtMail.setString(1, mail);
			pstmtMail.setString(2, id);
			rs = pstmtMail.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				isDuplicate[0] = true;
			}
			DBManager.close(null, pstmtMail, rs);
			rs = null;

			String sqlPhone = "SELECT COUNT(*) FROM NJW_MEMBER WHERE phone = ? AND id != ?";
			pstmtPhone = conn.prepareStatement(sqlPhone);
			pstmtPhone.setString(1, phone);
			pstmtPhone.setString(2, id);
			rs = pstmtPhone.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				isDuplicate[1] = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					DBManager.close(null, null, rs);
				if (pstmtMail != null)
					DBManager.close(null, pstmtMail, null);
				if (pstmtPhone != null)
					DBManager.close(null, pstmtPhone, null);
				if (conn != null)
					DBManager.close(conn, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isDuplicate;
	}

	public void addMember(MemberDTO dto) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "insert into njw_member values(?,?,?,?,?,SYS_GUID(),njw_member_seq_num.nextval,SYSDATE,SYSDATE,DEFAULT)";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getId());
			pstmt.setString(2, PasswordManager.hashPassword(dto.getPw()));
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getMail());
			pstmt.setString(5, dto.getPhone());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println("상품 등록 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}
	}

	public int updateMemberDetail(MemberDTO dto) {
	    int result = 0;
	    Connection conn = null;
	    PreparedStatement pstmt = null;

	    // [핵심] INSERT -> UPDATE로 변경
	    // 컬럼명(name, mail, phone)은 실제 DB 테이블의 컬럼명과 일치해야 합니다.
	    String sql = "UPDATE njw_member SET name = ?, mail = ?, phone = ?, updatetime = SYSDATE WHERE id = ?";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);

	        // SQL문의 물음표(?) 순서에 맞춰서 값을 넣어줍니다.
	        pstmt.setString(1, dto.getName());  // 첫 번째 ? : 이름 변경
	        pstmt.setString(2, dto.getMail());  // 두 번째 ? : 이메일 변경
	        pstmt.setString(3, dto.getPhone()); // 세 번째 ? : 전화번호 변경
	        
	        // [중요] WHERE절의 ID (누구를 수정할지 기준)
	        pstmt.setString(4, dto.getId());    // 네 번째 ? : 아이디

	        result = pstmt.executeUpdate(); // 성공 시 1 반환

	    } catch (Exception e) {
	        System.out.println("회원 정보 수정 중 에러 발생!");
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, null);
	    }
	    
	    return result; // 결과 반환
	}
	
	public int updateMemberPw(MemberDTO dto) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		// [핵심] INSERT -> UPDATE로 변경
		// 컬럼명(name, mail, phone)은 실제 DB 테이블의 컬럼명과 일치해야 합니다.
		String sql = "UPDATE njw_member SET password = ?, updatetime = SYSDATE WHERE id = ?";
		
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			// SQL문의 물음표(?) 순서에 맞춰서 값을 넣어줍니다.
			pstmt.setString(1, dto.getPw());
			
			// [중요] WHERE절의 ID (누구를 수정할지 기준)
			pstmt.setString(2, dto.getId());    // 네 번째 ? : 아이디
			
			result = pstmt.executeUpdate(); // 성공 시 1 반환
			
		} catch (Exception e) {
			System.out.println("회원 정보 수정 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}
		
		return result; // 결과 반환
	}

	public void insertRememberMeToken(String userId, String token, String ip, String userAgent) {
		String sql = "INSERT INTO remember_me_tokens (token_id, user_id, token_value, expiry_date, ip_address, user_agent) "
				+ "VALUES (njw_remember_me_seq_num.NEXTVAL, ?, ?, SYSDATE + 30, ?, ?)";

		try (Connection conn = DBManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, userId);
			pstmt.setString(2, token);
			pstmt.setString(3, ip);
			pstmt.setString(4, userAgent);

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MemberDTO checkRememberMeToken(String token) {
		MemberDTO member = null;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// NJW_MEMBER와 remember_me_tokens를 조인하여 회원 정보와 토큰 검증 정보를 함께 가져옴
		String sql = "SELECT m.id, m.password, m.name, m.mail, m.phone, m.role, r.ip_address, r.user_agent "
				+ "FROM NJW_MEMBER m " + "JOIN remember_me_tokens r ON m.id = r.user_id "
				+ "WHERE r.token_value = ? AND r.expiry_date > SYSDATE";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, token);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				member = new MemberDTO();
				// 1. 회원 기본 정보 매핑
				member.setId(rs.getString("id"));
				member.setPw(rs.getString("password"));
				member.setName(rs.getString("name"));
				member.setMail(rs.getString("mail"));
				member.setPhone(rs.getString("phone"));
				member.setRole(rs.getString("role"));

				// 2. (선택사항) 보안 검증을 위해 DTO에 필드가 있다면 IP와 User-Agent도 담습니다.
				// 만약 DTO에 해당 필드가 없다면 이 부분은 생략하거나 DTO를 수정해야 합니다.
				member.setLastIp(rs.getString("ip_address"));
				member.setLastUserAgent(rs.getString("user_agent"));
			}
		} catch (Exception e) {
			System.out.println("자동 로그인(토큰 조회) 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}

		return member; // 해당 토큰이 없거나 만료되었으면 null 반환
	}

	// 2. 사용한 토큰 삭제 (토큰 로테이션/로그아웃 용)
	public void deleteRememberMeToken(String token) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "DELETE FROM remember_me_tokens WHERE token_value = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, token);

			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("기존 토큰 삭제 완료 (토큰 로테이션)");
			}
		} catch (Exception e) {
			System.out.println("토큰 삭제 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}
	}
	
	public void deleteAccount(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "DELETE FROM njw_member WHERE id = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("계정 삭제 완료");
			}
		} catch (Exception e) {
			System.out.println("계정 삭제 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}
	}
}
