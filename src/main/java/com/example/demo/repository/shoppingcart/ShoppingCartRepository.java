package com.example.demo.repository.shoppingcart;

import com.example.demo.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends
        JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserIdAndIsDeletedFalse(Long userId);

    @Query("SELECT sc FROM ShoppingCart sc JOIN FETCH sc.cartItems WHERE sc.id = :cartId")
    ShoppingCart findByIdWithCartItems(@Param("cartId") Long cartId);
}
