package com.example.sweetandkarak.mapper;

import com.example.sweetandkarak.dto.response.CartResponse;
import com.example.sweetandkarak.model.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .totalPrice(cart.getTotalPrice())
                .userId(cart.getUser().getId())
                .itemId(cart.getItem().getId())
                .itemName(cart.getItem().getItemName())
                .itemImage(cart.getItem().getItemImage())
                .cafeId(cart.getCafe().getId())
                .cafeName(cart.getCafe().getCafeName())
                .createdOn(cart.getCreatedOn())
                .build();
    }
}
