package usecase.cart;

import domain.entity.Cart;
import domain.repository.CartRepository;
import infrastructure.persistence.CartRepositoryImpl;
import usecase.UseCaseException;

public class AddToCartUseCase {
    private final CartRepository cartRepo;

    public AddToCartUseCase() { this(CartRepositoryImpl.getInstance()); }
    public AddToCartUseCase(CartRepository cartRepo) { this.cartRepo = cartRepo; }

    public void execute(Cart cart) {
        String err = validate(cart);
        if (err != null) throw new UseCaseException(err);
        cartRepo.save(cart);
    }

    private String validate(Cart cart) {
        if (cart.getProductId() == null || cart.getProductId().trim().isEmpty())
            return "상품 ID가 유효하지 않습니다.";
        if (cart.getQuantity() < 1)
            return "수량은 1 이상이어야 합니다.";
        if (cart.getUnitPrice() <= 0)
            return "가격이 유효하지 않습니다.";
        return null;
    }
}
