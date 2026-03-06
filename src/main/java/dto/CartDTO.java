package dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CartDTO {
	    private int cartId;
	    private String memberId;
	    private String productId;
	    private int quantity;
	    
		private String pname;
		private int unitPrice;
		private String description;
		private String category;
		private int totalPrice;
		
		public CartDTO() {
			super();
		}
		
		public CartDTO(String productId, String name, String unitPrice) {
			super();
			
			this.productId = productId;
			this.pname = pname;
			this.unitPrice = Integer.parseInt(unitPrice);
		}
		
		public int getTotalPrice() {
			return this.unitPrice * this.quantity;
		}

		public void setTotalPrice(int totalPrice) {
			this.totalPrice = totalPrice;
		}

		
		public int getCartId() {
			return cartId;
		}
		public void setCartId(int cartId) {
			this.cartId = cartId;
		}
		public String getMemberId() {
			return memberId;
		}
		public void setMemberId(String memberId) {
			this.memberId = memberId;
		}
		public String getProductId() {
			return productId;
		}
		public void setProductId(String productId) {
			this.productId = productId;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
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
}
