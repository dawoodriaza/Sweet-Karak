package com.example.sweetandkarak.mapper;

import com.example.sweetandkarak.dto.response.OrderResponse;
import com.example.sweetandkarak.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderQuantity(order.getOrderQuantity())
                .totalOrderPrice(order.getTotalOrderPrice())
                .paymentReference(order.getPaymentReference())
                .orderStatus(order.getOrderStatus())
                .userId(order.getUser().getId())
                .userFullName(order.getUser().getFullName())
                .itemId(order.getItem().getId())
                .itemName(order.getItem().getItemName())
                .cafeId(order.getCafe().getId())
                .cafeName(order.getCafe().getCafeName())
                .createdOn(order.getCreatedOn())
                .build();
    }
}
