package com.example.sweetandkarak.service;

import com.example.sweetandkarak.dto.request.ReviewRequest;
import com.example.sweetandkarak.dto.response.ReviewResponse;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.exception.UnauthorizedActionException;
import com.example.sweetandkarak.mapper.ReviewMapper;
import com.example.sweetandkarak.model.*;
import com.example.sweetandkarak.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ItemReviewRepository itemReviewRepository;
    private final CafeReviewRepository cafeReviewRepository;
    private final ItemRepository itemRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse addItemReview(String email, Long itemId, ReviewRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));
        User user = findByEmail(email);

        ItemReview review = ItemReview.builder()
                .item(item)
                .cafe(item.getCafe())
                .user(user)
                .reviewDescription(request.getReviewDescription())
                .rating(request.getRating())
                .build();

        log.info("Item review added by: {} for item: {}", email, itemId);
        return reviewMapper.toResponse(itemReviewRepository.save(review));
    }

    @Transactional
    public ReviewResponse addCafeReview(String email, Long cafeId, ReviewRequest request) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new ResourceNotFoundException("Cafe not found with ID: " + cafeId));
        User user = findByEmail(email);

        CafeReview review = CafeReview.builder()
                .cafe(cafe)
                .user(user)
                .reviewDescription(request.getReviewDescription())
                .rating(request.getRating())
                .build();

        CafeReview saved = cafeReviewRepository.save(review);

        Double avg = cafeReviewRepository.findAverageRatingByCafeId(cafeId);
        if (avg != null) {
            cafe.setRatingOutOf5Star(Math.round(avg * 10.0) / 10.0);
            cafeRepository.save(cafe);
        }

        log.info("Cafe review added by: {} for cafe: {}", email, cafeId);
        return reviewMapper.toResponse(saved);
    }

    public Page<ReviewResponse> getItemReviews(Long itemId, Pageable pageable) {
        return itemReviewRepository.findByItemId(itemId, pageable).map(reviewMapper::toResponse);
    }

    public Page<ReviewResponse> getCafeReviews(Long cafeId, Pageable pageable) {
        return cafeReviewRepository.findByCafeId(cafeId, pageable).map(reviewMapper::toResponse);
    }

    public Page<ReviewResponse> getUserReviews(String email, Pageable pageable) {
        User user = findByEmail(email);
        return itemReviewRepository.findByUserId(user.getId(), pageable).map(reviewMapper::toResponse);
    }

    @Transactional
    public void deleteItemReview(String email, Long reviewId) {
        ItemReview review = itemReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Item review not found with ID: " + reviewId));
        if (!review.getUser().getEmail().equals(email)) {
            throw new UnauthorizedActionException("You can only delete your own reviews.");
        }
        itemReviewRepository.delete(review);
        log.info("Item review deleted: {}", reviewId);
    }

    @Transactional
    public void deleteCafeReview(String email, Long reviewId) {
        CafeReview review = cafeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Cafe review not found with ID: " + reviewId));
        if (!review.getUser().getEmail().equals(email)) {
            throw new UnauthorizedActionException("You can only delete your own reviews.");
        }
        cafeReviewRepository.delete(review);
        log.info("Cafe review deleted: {}", reviewId);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
