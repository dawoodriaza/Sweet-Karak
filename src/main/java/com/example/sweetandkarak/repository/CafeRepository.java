package com.example.sweetandkarak.repository;

import com.example.sweetandkarak.enums.CafeStatusEnum;
import com.example.sweetandkarak.model.Cafe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {

    @Query("SELECT c FROM Cafe c WHERE c.cafeStatus = :status AND c.isActive = :isActive")
    Page<Cafe> findPublicCafes(@Param("status") CafeStatusEnum status, @Param("isActive") Integer isActive, Pageable pageable);

    @Query("SELECT c FROM Cafe c WHERE c.cafeName = :name AND c.cafeStatus = :status AND c.isActive = :isActive")
    Page<Cafe> findPublicCafesByName(@Param("name") String name, @Param("status") CafeStatusEnum status, @Param("isActive") Integer isActive, Pageable pageable);

    Page<Cafe> findByCafeName(String cafeName, Pageable pageable);
    Page<Cafe> findByCafeStatus(CafeStatusEnum status, Pageable pageable);
    Page<Cafe> findByCafeAdminId(Long adminId, Pageable pageable);
    List<Cafe> findByCafeAdminIdAndCafeStatus(Long adminId, CafeStatusEnum status);
    boolean existsByCafeName(String cafeName);
}