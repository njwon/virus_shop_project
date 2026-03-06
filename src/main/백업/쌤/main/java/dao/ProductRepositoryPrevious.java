package dao;

import java.util.ArrayList;
import dto.Product;

public class ProductRepositoryPrevious {
	//상품 목록을 저장하기 위한 ArrayList<Product> 객체 타입의 변수
	private ArrayList<Product> listOfProducts = new ArrayList<Product>();
	//싱글톤 생성
	private static ProductRepositoryPrevious instance = new ProductRepositoryPrevious();
	public static ProductRepositoryPrevious getInstance() {
		return instance;
	}
	
	public ProductRepositoryPrevious() {
		Product phone = new Product("P12345","갤럭시탭S11",998800);
		phone.setCategory("Smart Phone");
		phone.setManufacturer("삼성전자");
		phone.setUnitsInStock(500);
		phone.setCondition("New");
		phone.setDescription("태블릿PC/Wi-Fi/11인치/120Hz/12GB/128GB/microSD지원/\r\n"
				+ "[프로세서/AI] 디멘시티 9400+/AP CPU: 99점/AP 게이밍: 100점/AI TOPS: 50 TOPS/요약/편집/교정/이미지 생성/편집/실시간 통역/서클 검색/음성비서/");
		phone.setFilename("masonry-portfolio-4.jpg");
		
		Product notebook = new Product("P12346","lg 그램 노트북",2500000);
		notebook.setCategory("notebook");
		notebook.setManufacturer("LG전자");
		notebook.setUnitsInStock(1000);
		notebook.setCondition("recycle");
		notebook.setDescription("노트북/39.6cm(15.6인치)/1.29kg/윈도우11홈/\r\n"
				+ "[화면] 해상도: 1920x1080(FHD)/밝기: 350nit/\r\n"
				+ "[CPU] 인텔/코어 울트라5(S2)/225H (4.9GHz)/NPU: 13TOPS/\r\n"
				+ "[그래픽] 내장그래픽/Arc 130T/7core/");
		notebook.setFilename("masonry-portfolio-3.jpg");
		
		Product tablet = new Product("P12347","	\r\n"
				+ "WACOM 무빙크패드 11",
				581250);
		tablet.setCategory("tablet");
		tablet.setManufacturer("WACOM ");
		tablet.setUnitsInStock(3000);
		tablet.setCondition("old");
		tablet.setDescription("태블릿PC/Wi-Fi/11.45인치/90Hz/8GB/128GB/\r\n"
				+ "[프로세서/AI] Helio G99/AP CPU: 26점/AP 게이밍: 12점/\r\n"
				+ "[카메라] 후면카메라: 싱글/전면카메라: 싱글/");
		tablet.setFilename("masonry-portfolio-2.jpg");
		
		//상품목록을 리스트에 추가
		listOfProducts.add(phone);
		listOfProducts.add(notebook);
		listOfProducts.add(tablet);
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







