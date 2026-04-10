package usecase.cart;

import domain.repository.CartRepository;
import infrastructure.persistence.CartRepositoryImpl;

public class ClearCartUseCase {
    private final CartRepository cartRepo;

    public ClearCartUseCase() { this(CartRepositoryImpl.getInstance()); }
    public ClearCartUseCase(CartRepository cartRepo) { this.cartRepo = cartRepo; }

    public void execute(String memberId) {
        cartRepo.deleteByMemberId(memberId);
    }
}
