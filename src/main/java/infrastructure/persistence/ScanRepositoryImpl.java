package infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import domain.entity.Scan;
import domain.repository.ScanRepository;
import util.DBManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScanRepositoryImpl implements ScanRepository {
    private static final Logger log = LoggerFactory.getLogger(ScanRepositoryImpl.class);
    private static final ScanRepositoryImpl instance = new ScanRepositoryImpl();

    public static ScanRepositoryImpl getInstance() {
        return instance;
    }

    @Override
    public void save(Scan scan) {
        String sql = "INSERT INTO SCAN "
                + "(num, member_id, scan_logtime, file_name, malicious, suspicious, undetected, harmless, uuid) "
                + "VALUES (SCAN_SEQ.NEXTVAL, ?, SYSDATE, ?, ?, ?, ?, ?, SYS_GUID())";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, scan.getUserId());
            pstmt.setString(2, scan.getFileName());
            pstmt.setInt(3, scan.getMalicious());
            pstmt.setInt(4, scan.getSuspicious());
            pstmt.setInt(5, scan.getUndetected());
            pstmt.setInt(6, scan.getHarmless());
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
    }

    @Override
    public String findOwnerById(String id) {
        String sql = "SELECT member_id FROM SCAN WHERE num = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("member_id");
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM SCAN WHERE num = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
    }

    @Override
    public List<Scan> findHistoryByUserId(String userId) {
        List<Scan> list = new ArrayList<>();
        String sql = "SELECT num, uuid, file_name, TO_CHAR(scan_logtime, 'YYYY-MM-DD') as s_date, "
                + "malicious, suspicious, undetected, harmless "
                + "FROM SCAN WHERE member_id = ? ORDER BY num DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Scan scan = new Scan();
                scan.setNum(rs.getInt("num"));
                scan.setUuid(rs.getString("uuid"));
                scan.setFileName(rs.getString("file_name"));
                scan.setScanDate(rs.getString("s_date"));
                scan.setMalicious(rs.getInt("malicious"));
                scan.setHarmless(rs.getInt("harmless"));
                scan.setSuspicious(rs.getInt("suspicious"));
                scan.setUndetected(rs.getInt("undetected"));
                list.add(scan);
            }
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return list;
    }
}
