package com.app.sneakers.cart.service;

import com.app.sneakers.cart.dto.CartDto;
import com.app.sneakers.cart.entities.CartEntity;
import com.app.sneakers.cart.entities.CartItem;
import com.app.sneakers.cart.entities.Money;
import com.app.sneakers.cart.mapper.CartMapper;
import com.app.sneakers.cart.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public CartDto saveCart(CartDto cartDto) {
        try {
            CartEntity cartEntity = cartMapper.mapCartDtoToEntity(cartDto);

            // Generate cartId if not set
            if (cartEntity.getCartId() == null) {
                cartEntity.setCartId(UUID.randomUUID().toString());
            }

            // Set bidirectional relationship
            if (cartEntity.getItems() != null) {
                cartEntity.getItems().forEach(item -> item.setCart(cartEntity));
            }

            CartEntity savedCart = cartRepository.save(cartEntity);
            return cartMapper.mapCartEntityToDto(savedCart);

        } catch (Exception ex) {
            // Log (if logger present), and rethrow for global handler to catch
            throw new RuntimeException("Failed to save cart: " + ex.getMessage(), ex);
        }
    }

    public CartDto getCartById(String id) {
        try {
            CartEntity entity = cartRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Cart not found with ID: " + id));

            return cartMapper.mapCartEntityToDto(entity);

        } catch (EntityNotFoundException enfe) {
            // Known exception, rethrow directly for GlobalHandler
            throw enfe;

        } catch (Exception ex) {
            // Unknown exception
            throw new RuntimeException("Error retrieving cart with ID: " + id, ex);
        }
    }

    public CartDto getCartByUserId(String userId) {
        try {
            CartEntity cartEntity = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Cart not found for userId: " + userId));

            return cartMapper.mapCartEntityToDto(cartEntity);

        } catch (EntityNotFoundException enfe) {
            throw enfe;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch cart for userId: " + userId, ex);
        }
    }

    public CartDto addItemToCart(String cartId, CartItem itemDto) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with ID: " + cartId));

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(existing ->
                        existing.getProductId().equals(itemDto.getProductId()) &&
                                existing.getSize().equals(itemDto.getSize()) &&
                                existing.getColor().equals(itemDto.getColor()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + itemDto.getQuantity());
            Double updatedItemTotalCost = itemDto.getUnitPrice().getValue() * itemDto.getQuantity();
            Double totalUpdatedPrice = existingItem.getTotalPrice().getValue() + updatedItemTotalCost;
            existingItem.setTotalPrice(new Money(existingItem.getTotalPrice().getCurrency(), totalUpdatedPrice));
        } else {
            itemDto.setId(UUID.randomUUID().toString());
            itemDto.setCart(cart);
            cart.getItems().add(itemDto);
        }

        updateTotalCartPrice(cart);
        CartEntity saved = cartRepository.save(cart);
        return cartMapper.mapCartEntityToDto(saved);
    }


    public CartDto removeItemFromCart(String cartId, CartItem itemDto) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with ID: " + cartId));

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(existing ->
                        existing.getProductId().equals(itemDto.getProductId()) &&
                                existing.getSize().equals(itemDto.getSize()) &&
                                existing.getColor().equals(itemDto.getColor()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();

            if (existingItem.getQuantity() > 1) {
                int updatedQuantity = existingItem.getQuantity() - 1;
                existingItem.setQuantity(updatedQuantity);
                Double updatedTotalPrice = existingItem.getTotalPrice().getValue() - existingItem.getUnitPrice().getValue();
                existingItem.setTotalPrice(new Money(existingItem.getTotalPrice().getCurrency(), updatedTotalPrice));

            } else {
                cart.getItems().remove(existingItem);
            }

            updateTotalCartPrice(cart);
            CartEntity saved = cartRepository.save(cart);
            return cartMapper.mapCartEntityToDto(saved);
        } else {
            throw new EntityNotFoundException("Item not found in cart with matching productId, size, and color");
        }
    }

//    private void updateTotalCartPrice(CartEntity cart) {
//        double total = cart.getItems().stream()
//                .mapToDouble(item -> item.getTotalPrice() != null ? item.getTotalPrice().getValue() : 0.0)
//                .sum();
//
//        cart.setTotalCartPrice(new Money(cart.getItems().get(0).getUnitPrice().getCurrency(), total));
//    }

    private void updateTotalCartPrice(CartEntity cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getTotalPrice() != null ? item.getTotalPrice().getValue() : 0.0)
                .sum();

        // Use default currency when cart is empty
        String currency = cart.getItems().isEmpty() ? "INR" : cart.getItems().get(0).getUnitPrice().getCurrency();
        cart.setTotalCartPrice(new Money(currency, total));
    }


}
