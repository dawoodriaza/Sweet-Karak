package com.example.sweetandkarak.controller;

import com.example.sweetandkarak.dto.request.CartRequest;
import com.example.sweetandkarak.dto.response.ApiResponse;
import com.example.sweetandkarak.dto.response.CartResponse;
import com.example.sweetandkarak.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody CartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Item added to cart", cartService.addToCart(request)));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Page<CartResponse>>> getMyCart(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("Cart items", cartService.getCartByEmail(principal.getName(), pageable)));
    }

    @PatchMapping("/{cartId}/quantity")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(@PathVariable Long cartId, @RequestParam Integer quantity) {
        return ResponseEntity.ok(ApiResponse.success("Quantity updated", cartService.updateCartQuantity(cartId, quantity)));
    }

    @DeleteMapping("/{cartId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Object>> removeCartItem(@PathVariable Long cartId) {
        cartService.removeCartItem(cartId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart"));
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Object>> clearCart(Principal principal) {
        cartService.clearCartByEmail(principal.getName());
        return ResponseEntity.ok(ApiResponse.success("Cart cleared"));
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Object>> checkout(Principal principal) {
        cartService.checkoutCart(principal.getName());
        return ResponseEntity.ok(ApiResponse.success("Checkout successful"));
    }
}
