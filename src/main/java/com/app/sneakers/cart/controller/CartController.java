package com.app.sneakers.cart.controller;

import com.app.sneakers.cart.dto.CartDto;
import com.app.sneakers.cart.entities.CartItem;
import com.app.sneakers.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-service/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Cart API", description = "Operations related to cart management like creating cart, adding/removing items, etc.")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "Create a new cart", description = "Initializes a new cart for a user.")
    @PostMapping("/cart")
    public ResponseEntity<CartDto> createCart(@RequestBody CartDto cartDto){
        return ResponseEntity.ok(cartService.saveCart(cartDto));
    }

    @Operation(summary = "Get Cart by cartId", description = "Retrieves users cart by cartId")
    @GetMapping("/cart/{id}")
    public ResponseEntity<CartDto> getCartById(@PathVariable("id") String id){
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @Operation(summary = "Get Cart by userId", description = "Retrieves users cart by userId")
    @GetMapping("/cart/user/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @Operation(summary = "Add Item to Cart", description = "This Operation auto calculates Quantity and update totalCost from Backend, UI needs to pass Item which needs to be added")
    @PatchMapping("/cart/{cartId}/add-item")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String cartId, @RequestBody CartItem itemDto) {
        CartDto updatedCart = cartService.addItemToCart(cartId, itemDto);
        return ResponseEntity.ok(updatedCart);
    }

    @Operation(summary = "Remove Item from Cart", description = "This Operation auto calculates Quantity and update totalCost from Backend, UI needs to pass Item which needs to be removed")
    @PatchMapping("/cart/{cartId}/remove-item")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable String cartId, @RequestBody CartItem itemDto) {
        CartDto updatedCart = cartService.removeItemFromCart(cartId, itemDto);
        return ResponseEntity.ok(updatedCart);
    }
}
