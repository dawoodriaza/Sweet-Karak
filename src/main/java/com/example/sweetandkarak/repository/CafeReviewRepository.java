package com.example.sweetandkarak.repository;

import com.example.sweetandkarak.model.CafeReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeReviewRepository extends JpaRepository<CafeReview, Long> {
    Page<CafeReview> findByCafeId(Long cafeId, Pageable pageable);
    Page<CafeReview> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM CafeReview r WHERE r.cafe.id = :cafeId")
    Double findAverageRatingByCafeId(@Param("cafeId") Long cafeId);
}
