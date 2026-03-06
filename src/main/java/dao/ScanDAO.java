package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.ScanDTO;
import util.DBManager; 

public class ScanDAO {
	
	// 싱글톤 패턴
	private ScanDAO() {}
	private static ScanDAO instance = new ScanDAO();
	public static ScanDAO getInstance() {
		return instance;
	}

	// [1] 검사 결과 저장하기 (Oracle 버전)
	public void saveResult(ScanDTO dto) {
		// ★ 수정된 부분: 시퀀스(SCAN_SEQ.NEXTVAL)를 사용합니다.
		String sql = "INSERT INTO SCAN_HISTORY "
				   + "(num, id, logtime, file_name, malicious, suspicious, undetected, harmless, total_scans, result_status, uuid) "
				   + "VALUES (SCAN_SEQ.NEXTVAL, ?, SYSDATE, ?, ?, ?, ?, ?, ?, ?, SYS_GUID())";
		
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			// ? 순서대로 값 넣기 (num은 시퀀스가 처리하므로 제외)
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getFileName());
			pstmt.setInt(3, dto.getMalicious());
			pstmt.setInt(4, dto.getSuspicious());
			pstmt.setInt(5, dto.getUndetected());
			pstmt.setInt(6, dto.getHarmless());
			pstmt.setInt(7, dto.getTotal());
			pstmt.setString(8, dto.getResultStatus());

			pstmt.executeUpdate(); // 실행

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}
	}

	public void deleteScan(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println(id);
		String sql = "DELETE FROM scan_history WHERE num = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("상품 삭제 완료");
			}
		} catch (Exception e) {
			System.out.println("상품 삭제 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}
	}
	
	// [2] 전체 기록 가져오기 (Oracle 버전)
	public List<ScanDTO> getHistory(String userId	) {
		List<ScanDTO> list = new ArrayList<>();
		// 오라클은 날짜 포맷팅을 위해 TO_CHAR를 쓰면 예쁘게 나옵니다 (선택사항)
		String sql = "SELECT num, file_name, TO_CHAR(scan_date, 'YYYY-MM-DD') as s_date, "
				   + "malicious, harmless, total_scans, result_status "
				   + "FROM SCAN_HISTORY WHERE id = ? ORDER BY num DESC";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ScanDTO dto = new ScanDTO();
				dto.setNum(rs.getInt("num"));
				dto.setFileName(rs.getString("file_name"));
				// 위 쿼리에서 별칭(alias)을 s_date로 줬으므로 s_date로 꺼냅니다
				dto.setScanDate(rs.getString("s_date")); 
				dto.setMalicious(rs.getInt("malicious"));
				dto.setHarmless(rs.getInt("harmless"));
				dto.setTotal(rs.getInt("total_scans"));
				dto.setResultStatus(rs.getString("result_status"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		
		return list;
	}
}