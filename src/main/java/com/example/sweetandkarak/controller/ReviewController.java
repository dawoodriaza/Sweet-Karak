package com.example.sweetandkarak.controller;


import com.example.sweetandkarak.model.ItemReview;
import com.example.sweetandkarak.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping("/item/{itemId}")
    public ItemReview addItemReview(
            @PathVariable Long itemId,
            @RequestParam Long userId,
            @RequestParam String description,
            @RequestParam int rating) {

        return reviewService.addItemReview(itemId, userId, description, rating);
    }




    @GetMapping("/item/{itemId}")
    public Page<ItemReview> getItemReviews(
            @PathVariable Long itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getItemReviews(itemId, pageable);
    }




    @GetMapping("/user/{userId}")
    public Page<ItemReview> getUserReviews(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getUserReviews(userId, pageable);
    }


    @DeleteMapping("/item/{reviewId}")
    public String deleteItemReview(@PathVariable Long reviewId) {
        reviewService.deleteItemReview(reviewId);
        return "Item review deleted";
    }




}