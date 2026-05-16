package com.example.sweetandkarak.service;



import com.example.sweetandkarak.model.Cart;


import com.example.sweetandkarak.exception.ResourceNotFoundException;


import com.example.sweetandkarak.repository.CartRepository;
import com.example.sweetandkarak.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    public CartService(CartRepository cartRepository,
                       ItemRepository itemRepository
                      ) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;

    }


    @Transactional
    public void removeCartItem(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + cartId));
        cartRepository.delete(cart);
        log.info("Cart item removed: {}", cartId);
    }


    public Cart addToCart(Long userId, Long itemId, Integer quantity) {

        return null;
    }

    public Page<Cart> getCartByUser(Long userId, Pageable pageable) {
        return null;
    }

    public Cart updateCartQuantity(Long cartId, Integer quantity) {
        return null;
    }

    public void clearCart(Long userId) {
    }

    public void checkoutCart(Long userId) {
    }
}