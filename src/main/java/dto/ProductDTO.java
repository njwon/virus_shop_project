package dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String date;        // 날짜
	private String productId;   // 상품 아이디
	private String pname;       // 상품명
	private int unitPrice;      // 상품가격
	private String description; // 상품설명
	private String category;    // 분류
	private String productImage;    // 이미지
	
	public ProductDTO() {
		super();
	}
	
	public ProductDTO(String productId, String pname, String unitPrice) {
		super();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date now = new Date();
		this.date = sdf.format(now);
		
		this.productId = productId;
		this.pname = pname;
		this.unitPrice = Integer.parseInt(unitPrice);
	}
	
	public String getSimpleDate() {
        if (this.date == null) return ""; // 날짜가 없으면 빈칸 반환
        try {
            // 1. 원본 문자열을 날짜 객체로 변환
            Date dateObj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.date);
            // 2. 원하는 형식으로 변환해서 반환
            return new SimpleDateFormat("yyyy-MM-dd").format(dateObj);
        } catch (Exception e) {
            return this.date; // 에러나면 그냥 원본 반환
        }
    }
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
}