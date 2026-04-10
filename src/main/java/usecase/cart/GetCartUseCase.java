package usecase.cart;

import java.util.ArrayList;

import domain.entity.Cart;
import domain.repository.CartRepository;
import infrastructure.persistence.CartRepositoryImpl;

public class GetCartUseCase {
    private final CartRepository cartRepo;

    public GetCartUseCase() { this(CartRepositoryImpl.getInstance()); }
    public GetCartUseCase(CartRepository cartRepo) { this.cartRepo = cartRepo; }

    public ArrayList<Cart> execute(String memberId) {
        return cartRepo.findByMemberId(memberId);
    }
}
