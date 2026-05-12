package com.example.sweetandkarak.repository;

import com.example.sweetandkarak.enums.CafeStatusEnum;
import com.example.sweetandkarak.model.Cafe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Page<Cafe> findByCafeName(String cafeName, Pageable pageable);

    Page<Cafe> findByCafeStatus(CafeStatusEnum status, Pageable pageable);

    Page<Cafe> findByCafeAdminId(Long adminId, Pageable pageable);

    List<Cafe> findByCafeAdminIdAndCafeStatus(Long adminId, CafeStatusEnum status);

    boolean existsByCafeNameIgnoreCase(String cafeName);

}