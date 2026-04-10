package infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import domain.entity.Product;
import domain.repository.ProductRepository;
import util.DBManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductRepositoryImpl implements ProductRepository {
    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);
    private static final ProductRepositoryImpl instance = new ProductRepositoryImpl();

    public static ProductRepositoryImpl getInstance() {
        return instance;
    }

    private static final String SELECT_COLS =
        "SELECT product_code, product_name, price, category, description, image_name, TO_CHAR(created_at, 'YYYY-MM-DD') AS created_at, uuid FROM product";

    private Product mapRow(ResultSet rs) throws Exception {
        Product p = new Product();
        p.setProductId(rs.getString("product_code"));
        p.setPname(rs.getString("product_name"));
        p.setUnitPrice(rs.getInt("price"));
        p.setCategory(rs.getString("category"));
        p.setDescription(rs.getString("description"));
        p.setProductImage(rs.getString("image_name"));
        p.setDate(rs.getString("created_at"));
        p.setUuid(rs.getString("uuid"));
        return p;
    }

    @Override
    public ArrayList<Product> findAll() {
        ArrayList<Product> list = new ArrayList<>();
        String sql = SELECT_COLS + " ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public Product findById(String productId) {
        String sql = SELECT_COLS + " WHERE product_code = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, productId);
            rs = pstmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public Product findByUuid(String uuid) {
        String sql = SELECT_COLS + " WHERE uuid = HEXTORAW(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uuid);
            rs = pstmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, rs);
        }
        return null;
    }

    @Override
    public int save(Product product) {
        String sql = "INSERT INTO product (product_code, product_name, price, category, description, image_name, created_at, uuid) "
                   + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, SYS_GUID())";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getProductId());
            pstmt.setString(2, product.getPname());
            pstmt.setInt(3, product.getUnitPrice());
            pstmt.setString(4, product.getCategory());
            pstmt.setString(5, product.getDescription());
            pstmt.setString(6, product.getProductImage());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("오류 발생", e);
        } finally {
            DBManager.close(conn, pstmt, null);
        }
        return 0;
    }

    @Override
    public int update(Product product) {
        String sql = "UPDATE product SET product_name = ?, price = ?, category = ?, description = ?, image_name = ? WHERE product_code = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getPname());
            pstmt.setInt(2, product.getUnitPrice());
            pstmt.setString(3, product.getCategory());
            pstmt.setString(4, product.getDescription());
            pstmt.setString(5, product.getProductImage());
            pstmt.setString(6, product.getProductId());
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
        String sql = "DELETE FROM product WHERE uuid = HEXTORAW(?)";
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
}
