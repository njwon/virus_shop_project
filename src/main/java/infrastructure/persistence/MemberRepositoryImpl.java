package infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import domain.entity.Member;
import domain.repository.MemberRepository;
import util.DBManager;
import util.PasswordManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberRepositoryImpl implements MemberRepository {
    private static final Logger log = LoggerFactory.getLogger(MemberRepositoryImpl.class);
    private static final MemberRepositoryImpl instance = new MemberRepositoryImpl();

    public static MemberRepositoryImpl getInstance() {
        return instance;
    }

    @Override
    public Member findById(String id) {
        String sql = "SELECT id, password, name, email, phone, role, uuid FROM MEMBER WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getString("id"));
                member.setPw(rs.getString("password"));
                member.setName(rs.getString("name"));
                member.setMail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setRole(rs.getString("role"));
                member.setUuid(rs.getString("uuid"));
                return member;
            }
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public boolean[] checkDuplicateOnSignUp(String id, String mail, String phone) {
        final boolean[] result = {false, false, false};
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM MEMBER WHERE id = ?) AS id_cnt, "
                + "(SELECT COUNT(*) FROM MEMBER WHERE email = ?) AS mail_cnt, "
                + "(SELECT COUNT(*) FROM MEMBER WHERE phone = ?) AS phone_cnt "
                + "FROM DUAL";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, mail);
            pstmt.setString(3, phone);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result[0] = rs.getInt("id_cnt") > 0;
                result[1] = rs.getInt("mail_cnt") > 0;
                result[2] = rs.getInt("phone_cnt") > 0;
            }
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return result;
    }

    @Override
    public boolean[] checkDuplicateOnUpdate(String id, String mail, String phone) {
        final boolean[] result = {false, false};
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM MEMBER WHERE email = ? AND id != ?) AS mail_cnt, "
                + "(SELECT COUNT(*) FROM MEMBER WHERE phone = ? AND id != ?) AS phone_cnt "
                + "FROM DUAL";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, mail);
            pstmt.setString(2, id);
            pstmt.setString(3, phone);
            pstmt.setString(4, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result[0] = rs.getInt("mail_cnt") > 0;
                result[1] = rs.getInt("phone_cnt") > 0;
            }
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return result;
    }

    @Override
    public void save(Member member) {
        String sql = "INSERT INTO MEMBER (id, password, name, email, phone, uuid, njw_member_seq_num, logtime, updatetime, role) "
                   + "VALUES (?, ?, ?, ?, ?, SYS_GUID(), njw_member_seq_num.nextval, SYSDATE, SYSDATE, DEFAULT)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getId());
            pstmt.setString(2, PasswordManager.hashPassword(member.getPw()));
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getMail());
            pstmt.setString(5, member.getPhone());
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
    }

    @Override
    public int updateDetail(Member member) {
        String sql = "UPDATE MEMBER SET name = ?, email = ?, phone = ?, updated_at = SYSDATE WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getMail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getId());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
        return 0;
    }

    @Override
    public int updatePassword(Member member) {
        String sql = "UPDATE MEMBER SET password = ?, updated_at = SYSDATE WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getPw());
            pstmt.setString(2, member.getId());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
        return 0;
    }

    @Override
    public void insertRememberMeToken(String userId, String token, String ip, String userAgent) {
        String sql = "INSERT INTO REMEMBER_ME_TOKEN (token_id, member_id, token_value, expiry_date, ip_address, user_agent) "
                + "VALUES (njw_remember_me_seq_num.NEXTVAL, ?, ?, SYSDATE + 30, ?, ?)";
        try (Connection conn = DBManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, token);
            pstmt.setString(3, ip);
            pstmt.setString(4, userAgent);
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        }
    }

    @Override
    public Member findByRememberMeToken(String token) {
        String sql = "SELECT m.id, m.password, m.name, m.email, m.phone, m.role, r.ip_address, r.user_agent "
                + "FROM MEMBER m JOIN REMEMBER_ME_TOKEN r ON m.id = r.member_id "
                + "WHERE r.token_value = ? AND r.expiry_date > SYSDATE";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, token);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getString("id"));
                member.setPw(rs.getString("password"));
                member.setName(rs.getString("name"));
                member.setMail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setRole(rs.getString("role"));
                member.setLastIp(rs.getString("ip_address"));
                member.setLastUserAgent(rs.getString("user_agent"));
                return member;
            }
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public void deleteRememberMeToken(String token) {
        String sql = "DELETE FROM REMEMBER_ME_TOKEN WHERE token_value = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, token);
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
    }

    @Override
    public void deleteAllRememberMeTokensByUserId(String userId) {
        String sql = "DELETE FROM REMEMBER_ME_TOKEN WHERE member_id = ?";
        try (Connection conn = DBManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            int result = pstmt.executeUpdate();
            log.info("자동 로그인 토큰 전체 삭제 완료 - 회원 ID: {}, 삭제된 토큰 수: {}", userId, result);
        } catch (Exception e) {
            log.error("오류 발생", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM MEMBER WHERE id = ?";
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
}
