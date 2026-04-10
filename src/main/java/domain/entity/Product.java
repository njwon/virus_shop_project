package domain.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String date;
    private String uuid;
    private String productId;
    private String pname;
    private int unitPrice;
    private String description;
    private String category;
    private String productImage;

    public Product() {}

    public Product(String productId, String pname, String unitPrice) {
        this.date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        this.productId = productId;
        this.pname = pname;
        this.unitPrice = Integer.parseInt(unitPrice);
    }

    public String getSimpleDate() {
        if (this.date == null) return "";
        try {
            Date dateObj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.date);
            return new SimpleDateFormat("yyyy-MM-dd").format(dateObj);
        } catch (Exception e) {
            return this.date;
        }
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getPname() { return pname; }
    public void setPname(String pname) { this.pname = pname; }

    public int getUnitPrice() { return unitPrice; }
    public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
}
