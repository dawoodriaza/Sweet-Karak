package com.example.sweetandkarak.service;

import com.example.sweetandkarak.dto.request.CartRequest;
import com.example.sweetandkarak.dto.response.CartResponse;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.exception.StockUnavailableException;
import com.example.sweetandkarak.mapper.CartMapper;
import com.example.sweetandkarak.model.Cart;
import com.example.sweetandkarak.model.Item;
import com.example.sweetandkarak.model.User;
import com.example.sweetandkarak.repository.CartRepository;
import com.example.sweetandkarak.repository.ItemRepository;
import com.example.sweetandkarak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartResponse addToCart(CartRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + request.getItemId()));

        if (item.getQuantityAvailable() < request.getQuantity()) {
            throw new StockUnavailableException("Only " + item.getQuantityAvailable() + " units available for: " + item.getItemName());
        }

        Optional<Cart> existing = cartRepository.findByUserIdAndItemId(user.getId(), item.getId());
        if (existing.isPresent()) {
            Cart cart = existing.get();
            int newQty = cart.getQuantity() + request.getQuantity();
            cart.setQuantity(newQty);
            cart.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(newQty)));
            return cartMapper.toResponse(cartRepository.save(cart));
        }

        Cart cart = Cart.builder()
                .user(user)
                .item(item)
                .cafe(item.getCafe())
                .quantity(request.getQuantity())
                .totalPrice(item.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .build();

        log.info("Item added to cart for user: {}", user.getId());
        return cartMapper.toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse updateCartQuantity(Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + cartId));
        Item item = cart.getItem();
        if (item.getQuantityAvailable() < quantity) {
            throw new StockUnavailableException("Only " + item.getQuantityAvailable() + " units available for: " + item.getItemName());
        }
        cart.setQuantity(quantity);
        cart.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return cartMapper.toResponse(cartRepository.save(cart));
    }

    public Page<CartResponse> getCartByEmail(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return cartRepository.findByUserId(user.getId(), pageable).map(cartMapper::toResponse);
    }

    @Transactional
    public void removeCartItem(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + cartId));
        cartRepository.delete(cart);
        log.info("Cart item removed: {}", cartId);
    }

    @Transactional
    public void clearCartByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        cartRepository.deleteByUserId(user.getId());
        log.info("Cart cleared for: {}", email);
    }

    @Transactional
    public void checkoutCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Cart> items = cartRepository.findByUserId(user.getId());
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("No items in cart for user: " + email);
        }
        cartRepository.deleteByUserId(user.getId());
        log.info("Checkout complete for: {}", email);
    }
}
