package com.example.sweetandkarak.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemResponse {
    private Long id;
    private String itemName;
    private String itemImage;
    private String itemDescription;
    private BigDecimal price;
    private Integer quantityAvailable;
    private Long cafeId;
    private String cafeName;
    private Integer isActive;
    private LocalDateTime createdOn;
}
