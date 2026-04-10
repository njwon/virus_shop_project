package usecase.product;

import domain.entity.Product;
import domain.repository.ProductRepository;
import infrastructure.persistence.ProductRepositoryImpl;

public class GetProductUseCase {
    private final ProductRepository productRepo;

    public GetProductUseCase() { this(ProductRepositoryImpl.getInstance()); }
    public GetProductUseCase(ProductRepository productRepo) { this.productRepo = productRepo; }

    public Product execute(String uuid) {
        return productRepo.findByUuid(uuid);
    }
}
