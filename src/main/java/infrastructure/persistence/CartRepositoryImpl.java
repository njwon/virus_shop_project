package infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.entity.Cart;
import domain.repository.CartRepository;
import util.DBManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartRepositoryImpl implements CartRepository {
    private static final Logger log = LoggerFactory.getLogger(CartRepositoryImpl.class);
    private static final CartRepositoryImpl instance = new CartRepositoryImpl();

    public static CartRepositoryImpl getInstance() {
        return instance;
    }

    @Override
    public void save(Cart cart) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false);

            String selectSql = "SELECT cart_id FROM CART WHERE member_id = ? AND product_id = ? FOR UPDATE";
            pstmt = conn.prepareStatement(selectSql);
            pstmt.setString(1, cart.getMemberId());
            pstmt.setString(2, cart.getProductId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pstmt.close();
                String updateSql = "UPDATE CART SET quantity = quantity + ? WHERE member_id = ? AND product_id = ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setInt(1, cart.getQuantity());
                pstmt.setString(2, cart.getMemberId());
                pstmt.setString(3, cart.getProductId());
                pstmt.executeUpdate();
            } else {
                pstmt.close();
                String insertSql = "INSERT INTO CART (member_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(insertSql);
                pstmt.setString(1, cart.getMemberId());
                pstmt.setString(2, cart.getProductId());
                pstmt.setInt(3, cart.getQuantity());
                pstmt.setInt(4, cart.getUnitPrice());
                pstmt.executeUpdate();
            }

            conn.commit();
            log.info("장바구니 담기 완료 - 회원 ID: {}, 상품 ID: {}", cart.getMemberId(), cart.getProductId());

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException re) { log.error("오류 발생", re); }
            log.error("오류 발생", e);
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { log.error("오류 발생", e); }
            DBManager.close(conn, pstmt, rs);
        }
    }

    @Override
    public ArrayList<Cart> findByMemberId(String memberId) {
        ArrayList<Cart> list = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.member_id, c.product_id, c.quantity, p.product_name, p.price "
                + "FROM CART c, PRODUCT p "
                + "WHERE c.product_id = p.product_code AND c.member_id = ? "
                + "ORDER BY c.cart_id DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setMemberId(rs.getString("member_id"));
                cart.setProductId(rs.getString("product_id"));
                cart.setQuantity(rs.getInt("quantity"));
                cart.setPname(rs.getString("product_name"));
                cart.setUnitPrice(rs.getInt("price"));
                list.add(cart);
            }
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public void deleteByMemberId(String memberId) {
        String sql = "DELETE FROM CART WHERE member_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            int result = pstmt.executeUpdate();
            log.info("장바구니 삭제 완료 - 회원 ID: {}, 삭제된 항목 수: {}", memberId, result);
        } catch (SQLException e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
    }
}
