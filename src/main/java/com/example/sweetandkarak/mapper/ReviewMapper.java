package com.example.sweetandkarak.mapper;

import com.example.sweetandkarak.dto.response.ReviewResponse;
import com.example.sweetandkarak.model.CafeReview;
import com.example.sweetandkarak.model.ItemReview;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(ItemReview review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .reviewDescription(review.getReviewDescription())
                .rating(review.getRating())
                .userId(review.getUser().getId())
                .userFullName(review.getUser().getFullName())
                .cafeId(review.getCafe().getId())
                .cafeName(review.getCafe().getCafeName())
                .itemId(review.getItem().getId())
                .itemName(review.getItem().getItemName())
                .createdOn(review.getCreatedOn())
                .build();
    }

    public ReviewResponse toResponse(CafeReview review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .reviewDescription(review.getReviewDescription())
                .rating(review.getRating())
                .userId(review.getUser().getId())
                .userFullName(review.getUser().getFullName())
                .cafeId(review.getCafe().getId())
                .cafeName(review.getCafe().getCafeName())
                .itemId(null)
                .itemName(null)
                .createdOn(review.getCreatedOn())
                .build();
    }
}
