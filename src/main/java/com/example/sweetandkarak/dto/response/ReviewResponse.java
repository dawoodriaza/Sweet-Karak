package com.example.sweetandkarak.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private String reviewDescription;
    private Integer rating;
    private Long userId;
    private String userFullName;
    private Long cafeId;
    private String cafeName;
    private Long itemId;
    private String itemName;
    private LocalDateTime createdOn;
}
