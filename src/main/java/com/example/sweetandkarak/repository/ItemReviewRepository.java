package com.example.sweetandkarak.repository;



import com.example.sweetandkarak.model.ItemReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemReviewRepository extends JpaRepository<ItemReview, Long> {
    Page<ItemReview> findByItemId(Long itemId, Pageable pageable);
    Page<ItemReview> findByCafeId(Long cafeId, Pageable pageable);
    Page<ItemReview> findByUserId(Long userId, Pageable pageable);
    @Query("SELECT AVG(r.rating) FROM ItemReview r WHERE r.item.id = :itemId")
    Double findAverageRatingByItemId(@Param("itemId") Long itemId);

}