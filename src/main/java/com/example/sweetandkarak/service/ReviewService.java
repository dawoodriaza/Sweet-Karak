package com.example.sweetandkarak.service;



import com.example.sweetandkarak.model.*;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReviewService {

    private final ItemReviewRepository itemReviewRepository;

    private final ItemRepository itemRepository;
    private final CafeRepository cafeRepository;


    public ReviewService(ItemReviewRepository itemReviewRepository,

                         ItemRepository itemRepository,
                         CafeRepository cafeRepository) {
        this.itemReviewRepository = itemReviewRepository;

        this.itemRepository = itemRepository;
        this.cafeRepository = cafeRepository;

    }

    @Transactional
    public ItemReview addItemReview(Long itemId, Long userId, String description, int rating) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));




        ItemReview review = new ItemReview();
        review.setItem(item);
        review.setCafe(item.getCafe());

        review.setReviewDescription(description);
        review.setRating(rating);

        ItemReview savedReview = itemReviewRepository.save(review);

        log.info("Item review added by user {} for item {}", userId, itemId);

        return savedReview;
    }



    public Page<ItemReview> getItemReviews(Long itemId, Pageable pageable) {
        return itemReviewRepository.findByItemId(itemId, pageable);
    }



    public Page<ItemReview> getUserReviews(Long userId, Pageable pageable) {
        return itemReviewRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public void deleteItemReview(Long reviewId) {

        ItemReview review = itemReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Item review not found with ID: " + reviewId));

        itemReviewRepository.delete(review);

        log.info("Item review deleted {}", reviewId);
    }


}