package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dto.MemberDTO;
import dto.ProductDTO;
import util.DBManager;

public class ProductDAO {
	private static ProductDAO instance = new ProductDAO();
	public static ProductDAO getInstance() {
		return instance;
	}
	public ArrayList<ProductDTO> getAllProducts() {
		ArrayList<ProductDTO> list = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM product ORDER BY date_ DESC";
		
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ProductDTO dto = new ProductDTO();
				dto.setProductId(rs.getString("id"));
				dto.setPname(rs.getString("name"));
				dto.setUnitPrice(rs.getInt("price"));
				dto.setDescription(rs.getString("description"));
				dto.setCategory(rs.getString("category"));
				dto.setDate(rs.getString("date_"));
				dto.setProductImage(rs.getString("img"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		
		return list;
	}
	
	public int updateProductDetail(ProductDTO dto) {
	    int result = 0;
	    Connection conn = null;
	    PreparedStatement pstmt = null;

	    // [핵심] INSERT -> UPDATE로 변경
	    // 컬럼명(name, mail, phone)은 실제 DB 테이블의 컬럼명과 일치해야 합니다.
	    String sql = "UPDATE product SET name = ?, price = ?, category = ?, description = ?, img = ?, date_ = ? WHERE id = ?";
	    System.out.printf(dto.getProductId());
	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);

	        pstmt.setString(1, dto.getPname());
	        pstmt.setInt(2, dto.getUnitPrice());
	        pstmt.setString(3, dto.getCategory());
	        pstmt.setString(4, dto.getDescription());
	        pstmt.setString(5, dto.getProductImage());
	        pstmt.setString(6, dto.getDate());
	        
	        // [중요] WHERE절의 ID (누구를 수정할지 기준)
	        pstmt.setString(7, dto.getProductId());

	        result = pstmt.executeUpdate();

	    } catch (Exception e) {
	        System.out.println("상품 정보 수정 중 에러 발생!");
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, null);
	    }
	    
	    return result;
	}
	
	public int addProduct(ProductDTO dto) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "INSERT INTO product VALUES(njw_product_seq_num.nextval, ?, ?, ?, ?, ?, ?, ?, SYS_GUID())";
		
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getProductId());
			pstmt.setString(2, dto.getPname());
			pstmt.setInt(3, dto.getUnitPrice());
			pstmt.setString(4, dto.getCategory());
			pstmt.setString(5, dto.getDescription());
			pstmt.setString(6, dto.getProductImage());
			pstmt.setString(7, dto.getDate());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("상품 등록 중 에러 발생!");
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, null);
		}
		
		return result;
	}
	
	public ProductDTO getProductById(String productId) {
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ProductDTO product = null;

	    String sql = "SELECT * FROM product WHERE id = ?"; 

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, productId); 
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            product = new ProductDTO();
	            product.setProductId(rs.getString("id"));
	            product.setPname(rs.getString("name"));
	            product.setUnitPrice(rs.getInt("price"));
	            product.setCategory(rs.getString("category"));
	            product.setDescription(rs.getString("description"));
	            product.setProductImage(rs.getString("img"));
	            product.setDate(rs.getString("date_"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBManager.close(conn, pstmt, rs); 
	    }
	    return product;
	}

	public void deleteProduct(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println(id);
		String sql = "DELETE FROM product WHERE id = ?";

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
}