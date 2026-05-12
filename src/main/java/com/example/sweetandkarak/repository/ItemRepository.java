package com.example.sweetandkarak.repository;


import com.example.sweetandkarak.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByCafeId(Long cafeId, Pageable pageable);

    Page<Item> findByItemName(String itemName, Pageable pageable);

    Page<Item> findByCafeIdAndItemName(Long cafeId, String itemName, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Optional<Item> findByIdWithLock(@Param("id") Long id);
}