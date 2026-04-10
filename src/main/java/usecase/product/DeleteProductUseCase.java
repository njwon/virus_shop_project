package usecase.product;

import domain.repository.ProductRepository;
import infrastructure.persistence.ProductRepositoryImpl;

public class DeleteProductUseCase {
    private final ProductRepository productRepo;

    public DeleteProductUseCase() { this(ProductRepositoryImpl.getInstance()); }
    public DeleteProductUseCase(ProductRepository productRepo) { this.productRepo = productRepo; }

    public void execute(String uuid) {
        productRepo.deleteByUuid(uuid);
    }
}
