package dto.old;

import java.io.Serializable;

public class Product implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String date; //날짜
	private String productId; //상품 아이디
	private String pname; //상품명
	private int unitPrice; //상품가격
	private String description; //상품설명
	private String category; //분류
	
	//기본 생성자 함수
	public Product() {
		super();
	}
	//매개변수 3개짜리 생성자 함수
	public Product(String productId, String pname, int unitPrice) {
		super();
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy.MM.dd");
	    java.util.Date now = new java.util.Date();
	    this.date = sdf.format(now);
		this.productId = productId;
		this.pname = pname;
		this.unitPrice = unitPrice;
	}
	//getter, setter
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
}
