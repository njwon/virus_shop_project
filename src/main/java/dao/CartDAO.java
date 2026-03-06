package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.CartDTO;
import util.DBManager;

public class CartDAO {
	// CartDTO를 매개변수로 받습니다 (여기엔 memberId, productId, quantity가 들어있어야 함)
	private static CartDAO instance = new CartDAO();
	public static CartDAO getInstance() {
		return instance;
	}
	public void addCart(CartDTO cart) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        conn = DBManager.getConnection();

	        // 1단계: 해당 회원이 해당 상품을 이미 담았는지 확인 (SELECT)
	        String selectSql = "SELECT cart_id FROM Cart WHERE member_id = ? AND product_id = ?";
	        pstmt = conn.prepareStatement(selectSql);
	        pstmt.setString(1, cart.getMemberId());
	        pstmt.setString(2, cart.getProductId());
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            // 2단계: 이미 있다면 -> 수량만 증가 (UPDATE)
	            // 쿼리 준비를 위해 이전 자원 닫기
	            pstmt.close(); 
	            
	            String updateSql = "UPDATE Cart SET quantity = quantity + ? WHERE member_id = ? AND product_id = ?";
	            pstmt = conn.prepareStatement(updateSql);
	            pstmt.setInt(1, cart.getQuantity()); // 담으려는 수량만큼 더하기
	            pstmt.setString(2, cart.getMemberId());
	            pstmt.setString(3, cart.getProductId());
	            pstmt.executeUpdate(); // 실행

	        } else {
	            // 3단계: 없다면 -> 새로 장바구니에 추가 (INSERT)
	            pstmt.close();
	            
	            String insertSql = "INSERT INTO Cart (cart_id, member_id, product_id, quantity, price) VALUES (njw_cart_seq_num.nextval, ?, ?, ?, ?)";
	            pstmt = conn.prepareStatement(insertSql);
	            pstmt.setString(1, cart.getMemberId());
	            pstmt.setString(2, cart.getProductId());
	            pstmt.setInt(3, cart.getQuantity()); // 기본 수량 (보통 1)
	            pstmt.setInt(4, cart.getUnitPrice());
	            pstmt.executeUpdate(); // 실행
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs);
	    }
	}

	public ArrayList<CartDTO> getCartList(String memberId) {
	    ArrayList<CartDTO> list = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    // [중요] Cart(c)와 Product(p) 테이블을 합치는 쿼리
	    String sql = "SELECT c.cart_id, c.member_id, c.product_id, c.quantity, p.name, p.price " +
                "FROM Cart c, Product p " +
                "WHERE c.product_id = p.id AND c.member_id = ? " +
                "ORDER BY c.cart_id DESC";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, memberId); // 로그인한 사용자 아이디
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            CartDTO cart = new CartDTO();
	            // 1. 장바구니 기본 정보
	         // 1. 기존 장바구니 정보 담기
	            cart.setCartId(rs.getInt("cart_id"));
	            cart.setMemberId(rs.getString("member_id"));
	            cart.setProductId(rs.getString("product_id"));
	            cart.setQuantity(rs.getInt("quantity"));

	            // 2. [추가] JOIN으로 가져온 상품 정보 담기 (여기서 에러가 났던 겁니다)
	            // 아까 SQL에서 AS로 별칭 지어준 이름 그대로 사용합니다.
	            cart.setPname(rs.getString("name"));           // 상품명
	            cart.setUnitPrice(rs.getInt("price"));      // 가격

	            // 3. 리스트에 추가
	            list.add(cart);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs);
	    }
	    return list;
	}
	
	public void deleteCartByUserId(String userId) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    // ResultSet은 DELETE 문에 필요 없습니다.
	    
	    // [중요] 위쪽 메서드들과 컬럼명을 통일했습니다 (user_id -> member_id)
	    String query = "DELETE FROM Cart WHERE member_id = ?"; 

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(query);
	        
	        // [순서 수정] 1. 먼저 물음표(?)를 채웁니다.
	        pstmt.setString(1, userId);
	        
	        // [메서드 수정] 2. 그 다음 실행합니다 (DELETE는 executeUpdate)
	        int result = pstmt.executeUpdate();
	        
	        System.out.println("삭제된 장바구니 건수: " + result);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // [자원 해제] 반드시 닫아줘야 나중에 에러가 안 납니다.
	    	DBManager.close(conn, pstmt, null);
	    }
	}
}
