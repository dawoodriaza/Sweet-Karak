package com.example.sweetandkarak.repository;

import com.example.sweetandkarak.model.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.cafe.id = :cafeId AND i.isActive = :isActive")
    Page<Item> findByCafeIdAndIsActive(@Param("cafeId") Long cafeId, @Param("isActive") Integer isActive, Pageable pageable);

    Page<Item> findByCafeId(Long cafeId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.itemName = :itemName AND i.isActive = :isActive")
    Page<Item> findByItemNameAndIsActive(@Param("itemName") String itemName, @Param("isActive") Integer isActive, Pageable pageable);

    Page<Item> findByItemName(String itemName, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.cafe.id = :cafeId AND i.itemName = :itemName AND i.isActive = :isActive")
    Page<Item> findByCafeIdAndItemNameAndIsActive(@Param("cafeId") Long cafeId, @Param("itemName") String itemName, @Param("isActive") Integer isActive, Pageable pageable);

    Page<Item> findByCafeIdAndItemName(Long cafeId, String itemName, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Optional<Item> findByIdWithLock(@Param("id") Long id);
}