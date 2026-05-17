package com.example.sweetandkarak.dto.response;

import com.example.sweetandkarak.enums.OrderStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Integer orderQuantity;
    private BigDecimal totalOrderPrice;
    private String paymentReference;
    private OrderStatusEnum orderStatus;
    private Long userId;
    private String userFullName;
    private Long itemId;
    private String itemName;
    private Long cafeId;
    private String cafeName;
    private LocalDateTime createdOn;
}
