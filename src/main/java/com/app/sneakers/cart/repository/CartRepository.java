package com.app.sneakers.cart.repository;

import com.app.sneakers.cart.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, String> {

    Optional<CartEntity> findByUserId(String userId);

}
