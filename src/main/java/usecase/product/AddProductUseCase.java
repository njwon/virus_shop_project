package usecase.product;

import domain.entity.Product;
import domain.repository.ProductRepository;
import infrastructure.persistence.ProductRepositoryImpl;
import usecase.UseCaseException;

public class AddProductUseCase {
    private final ProductRepository productRepo;

    public AddProductUseCase() { this(ProductRepositoryImpl.getInstance()); }
    public AddProductUseCase(ProductRepository productRepo) { this.productRepo = productRepo; }

    public void execute(Product product) {
        String err = validate(product);
        if (err != null) throw new UseCaseException(err);
        productRepo.save(product);
    }

    private String validate(Product product) {
        String id = product.getProductId();
        if (id == null || !id.matches("^P[0-9]{2,9}$"))
            return "상품 코드는 P로 시작하는 숫자 조합 3~10자리여야 합니다. (예: P123456)";
        String name = product.getPname();
        if (name == null || name.trim().length() < 3 || name.trim().length() > 12)
            return "상품명은 3~12자로 입력해주세요.";
        if (product.getUnitPrice() <= 0)
            return "가격은 1 이상의 양의 정수로 입력해주세요.";
        String cat = product.getCategory();
        if (cat == null || cat.trim().isEmpty())
            return "카테고리를 입력해주세요.";
        if (cat.trim().length() > 20)
            return "카테고리는 20자 이하로 입력해주세요.";
        return null;
    }
}
