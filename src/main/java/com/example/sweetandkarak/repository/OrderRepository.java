package com.example.sweetandkarak.repository;



import com.example.sweetandkarak.enums.OrderStatusEnum;
import com.example.sweetandkarak.model.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByCafeId(Long cafeId, Pageable pageable);

    Page<Order> findByOrderStatus(OrderStatusEnum status, Pageable pageable);

    Page<Order> findByCafeIdAndOrderStatus(Long cafeId, OrderStatusEnum status, Pageable pageable);

}