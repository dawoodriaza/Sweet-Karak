package com.example.sweetandkarak.controller;

import com.example.sweetandkarak.dto.request.ReviewRequest;
import com.example.sweetandkarak.dto.response.ApiResponse;
import com.example.sweetandkarak.dto.response.ReviewResponse;
import com.example.sweetandkarak.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/item/{itemId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ReviewResponse>> addItemReview(Principal principal, @PathVariable Long itemId, @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Item review added", reviewService.addItemReview(principal.getName(), itemId, request)));
    }

    @PostMapping("/cafe/{cafeId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ReviewResponse>> addCafeReview(Principal principal, @PathVariable Long cafeId, @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cafe review added", reviewService.addCafeReview(principal.getName(), cafeId, request)));
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getItemReviews(
            @PathVariable Long itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("Item reviews", reviewService.getItemReviews(itemId, pageable)));
    }

    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getCafeReviews(
            @PathVariable Long cafeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("Cafe reviews", reviewService.getCafeReviews(cafeId, pageable)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getMyReviews(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("My reviews", reviewService.getUserReviews(principal.getName(), pageable)));
    }

    @DeleteMapping("/item/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> deleteItemReview(Principal principal, @PathVariable Long reviewId) {
        reviewService.deleteItemReview(principal.getName(), reviewId);
        return ResponseEntity.ok(ApiResponse.success("Item review deleted"));
    }

    @DeleteMapping("/cafe/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> deleteCafeReview(Principal principal, @PathVariable Long reviewId) {
        reviewService.deleteCafeReview(principal.getName(), reviewId);
        return ResponseEntity.ok(ApiResponse.success("Cafe review deleted"));
    }
}
