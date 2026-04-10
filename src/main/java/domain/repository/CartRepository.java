package domain.repository;

import java.util.ArrayList;

import domain.entity.Cart;

public interface CartRepository {
    void save(Cart cart);
    ArrayList<Cart> findByMemberId(String memberId);
    void deleteByMemberId(String memberId);
}
