package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBManager {

    private static final Logger log = LoggerFactory.getLogger(DBManager.class);
    private static DataSource dataSource;

    static {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
            log.info("커넥션 풀 초기화 완료");
        } catch (Exception e) {
            log.error("커넥션 풀 초기화 실패", e);
        }
    }

    public static Connection getConnection() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource가 초기화되지 않았습니다. context.xml 설정을 확인하세요.");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("커넥션 획득 실패", e);
            throw new RuntimeException("DB 연결에 실패했습니다.", e);
        }
    }

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try { if (rs   != null) rs.close();   } catch (Exception e) { log.error("오류 발생", e); }
        try { if (pstmt != null) pstmt.close(); } catch (Exception e) { log.error("오류 발생", e); }
        try { if (conn  != null) conn.close();  } catch (Exception e) { log.error("오류 발생", e); }
    }
}
