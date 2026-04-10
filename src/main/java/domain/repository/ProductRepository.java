package domain.repository;

import java.util.ArrayList;

import domain.entity.Product;

public interface ProductRepository {
    ArrayList<Product> findAll();
    Product findById(String productId);
    Product findByUuid(String uuid);
    int save(Product product);
    int update(Product product);
    void deleteByUuid(String uuid);
}
