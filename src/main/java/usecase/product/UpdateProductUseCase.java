package usecase.product;

import domain.entity.Product;
import domain.repository.ProductRepository;
import infrastructure.persistence.ProductRepositoryImpl;
import usecase.UseCaseException;

public class UpdateProductUseCase {
    private final ProductRepository productRepo;

    public UpdateProductUseCase() { this(ProductRepositoryImpl.getInstance()); }
    public UpdateProductUseCase(ProductRepository productRepo) { this.productRepo = productRepo; }

    public void execute(Product product) {
        String err = validate(product);
        if (err != null) throw new UseCaseException(err);
        productRepo.update(product);
    }

    private String validate(Product product) {
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
