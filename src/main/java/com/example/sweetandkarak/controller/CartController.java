package com.example.sweetandkarak.controller;

import com.example.sweetandkarak.model.Cart;
import com.example.sweetandkarak.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PostMapping
    public Cart addToCart(
            @RequestParam Long userId,
            @RequestParam Long itemId,
            @RequestParam Integer quantity) {

        return cartService.addToCart(userId, itemId, quantity);
    }


    @GetMapping("/user/{userId}")
    public Page<Cart> getCartByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return cartService.getCartByUser(userId, pageable);
    }


    @PatchMapping("/{cartId}/quantity")
    public Cart updateCartQuantity(
            @PathVariable Long cartId,
            @RequestParam Integer quantity) {

        return cartService.updateCartQuantity(cartId, quantity);
    }


    @DeleteMapping("/{cartId}")
    public String removeCartItem(@PathVariable Long cartId) {
        cartService.removeCartItem(cartId);
        return "Cart item removed";
    }


    @DeleteMapping("/user/{userId}/clear")
    public String clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return "Cart cleared";
    }


    @PostMapping("/user/{userId}/checkout")
    public String checkoutCart(@PathVariable Long userId) {
        cartService.checkoutCart(userId);
        return "Checkout successful. Cart cleared.";
    }

}