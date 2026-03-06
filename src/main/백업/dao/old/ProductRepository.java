package dao.old;

import java.util.ArrayList;
import dto.old.Product;

public class ProductRepository {
	//상품 목록을 저장하기 위한 ArrayList<Product> 객체 타입의 변수
	//싱글톤 생성
	private static ProductRepository instance = new ProductRepository();
	public static ProductRepository getInstance() {
		return instance;
	}

	private ArrayList<Product> listOfProducts;
	
	public ProductRepository() {
		Product token = new Product("P12345","검사 토큰",10000);
		token.setCategory("token");
		token.setDescription("AI 보안 검사를 하기 위해 필요한 토큰입니다. AI 보안 검사는 휴리스틱 알고리즘을 통해 이용자의 보안 허점을 검사합니다.");
		
		Product basicPlan = new Product("P12346","Basic 플랜",280000);
		basicPlan.setCategory("plan");
		basicPlan.setDescription("보안팀에게 검사 요청을 할 수 있습니다. 토큰이 무제한으로 제공됩니다. 단, 하루에 한번만 검사할 수 있습니다.");
		
		
		Product proPlan = new Product("P12347","Pro 플랜",350000);
		proPlan.setCategory("plan");
		proPlan.setDescription("관리자에게 검사 요청을 할 수 있습니다. 토큰이 무제한으로 제공됩니다. 제한 없이 무제한으로 모든 서비스를 이용할 수 있습니다.");
		
		//상품목록을 리스트에 추가
		listOfProducts.add(token);
		listOfProducts.add(basicPlan);
		listOfProducts.add(proPlan);
	}
	//리스트에 저장된 모든 상품 목록을 가져오는 메소드 추가
	public ArrayList<Product> getAllProducts(){
		return listOfProducts;
	}
	
	public Product getProductById(String productId) {
		Product productById = null;
		for(int i=0; i<listOfProducts.size(); i++) {
			Product product = listOfProducts.get(i);
			if(product != null && product.getProductId() != null && product.getProductId().equals(productId)) {
				productById = product;
				break;
			}
		}
		return productById;
	}
	
	//ProductRepository 클래스에 하나의 제품에 대한 다양한 정보를 제품정보 리스트에 추가할 addProduct() 메소드 생성
	public void addProduct(Product product) {
		listOfProducts.add(product);
	}
	
	
}







