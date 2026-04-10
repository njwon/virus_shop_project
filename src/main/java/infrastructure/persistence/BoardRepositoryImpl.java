package infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import domain.entity.Board;
import domain.repository.BoardRepository;
import util.DBManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardRepositoryImpl implements BoardRepository {
    private static final Logger log = LoggerFactory.getLogger(BoardRepositoryImpl.class);
    private static final BoardRepositoryImpl instance = new BoardRepositoryImpl();

    public static BoardRepositoryImpl getInstance() {
        return instance;
    }

    @Override
    public int countAll() {
        String sql = "SELECT count(*) FROM board";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return 0;
    }

    @Override
    public List<Board> findAll(int page, int pageSize) {
        List<Board> list = new ArrayList<>();
        int startNum = (page - 1) * pageSize + 1;
        int endNum   = page * pageSize;
        String sql = "SELECT * FROM ("
                + "    SELECT A.*, ROWNUM AS RNUM FROM ("
                + "        SELECT b.num, b.member_id, m.name AS member_name, b.title, b.content, TO_CHAR(b.created_at, 'YYYY-MM-DD') AS created_at, b.view_count, b.ip, b.uuid"
                + "        FROM board b JOIN MEMBER m ON b.member_id = m.id ORDER BY b.num DESC"
                + "    ) A WHERE ROWNUM <= ?"
                + ") WHERE RNUM >= ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, endNum);
            pstmt.setInt(2, startNum);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Board board = new Board();
                board.setId(rs.getInt("num"));
                board.setMemberId(rs.getString("member_id"));
                board.setMemberName(rs.getString("member_name"));
                board.setSubject(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setHit(rs.getInt("view_count"));
                board.setIp(rs.getString("ip"));
                board.setDate(rs.getString("created_at"));
                board.setUuid(rs.getString("uuid"));
                list.add(board);
            }
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public Board findByUuid(String uuid) {
        String sql = "SELECT b.num, b.member_id, m.name AS member_name, b.title, b.content, TO_CHAR(b.created_at, 'YYYY-MM-DD') AS created_at, b.view_count, b.ip, b.uuid"
                + " FROM board b JOIN MEMBER m ON b.member_id = m.id WHERE b.uuid = HEXTORAW(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uuid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Board board = new Board();
                board.setId(rs.getInt("num"));
                board.setUuid(rs.getString("uuid"));
                board.setMemberId(rs.getString("member_id"));
                board.setMemberName(rs.getString("member_name"));
                board.setSubject(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setDate(rs.getString("created_at"));
                board.setHit(rs.getInt("view_count"));
                board.setIp(rs.getString("ip"));
                return board;
            }
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public int save(Board board) {
        String sql = "INSERT INTO board (num, member_id, title, content, created_at, view_count, ip, uuid) VALUES(njw_board_seq_num.nextval, ?, ?, ?, SYSDATE, ?, ?, SYS_GUID())";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, board.getMemberId());
            pstmt.setString(2, board.getSubject());
            pstmt.setString(3, board.getContent());
            pstmt.setInt(4, board.getHit());
            pstmt.setString(5, board.getIp());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
        return 0;
    }

    @Override
    public int updateByUuid(Board board) {
        String sql = "UPDATE board SET title = ?, content = ? WHERE uuid = HEXTORAW(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, board.getSubject());
            pstmt.setString(2, board.getContent());
            pstmt.setString(3, board.getUuid());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
        return 0;
    }

    @Override
    public void deleteByUuid(String uuid) {
        String sql = "DELETE FROM board WHERE uuid = HEXTORAW(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uuid);
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
    }

    @Override
    public int increaseHit(Board board) {
        String sql = "UPDATE board SET view_count = board.view_count + 1 WHERE num = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, board.getId());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
        return 0;
    }
}
