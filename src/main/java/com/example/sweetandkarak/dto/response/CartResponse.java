package com.example.sweetandkarak.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CartResponse {
    private Long id;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Long userId;
    private Long itemId;
    private String itemName;
    private String itemImage;
    private Long cafeId;
    private String cafeName;
    private LocalDateTime createdOn;
}
