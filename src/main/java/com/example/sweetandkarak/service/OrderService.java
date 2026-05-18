package com.example.sweetandkarak.service;


import com.example.sweetandkarak.dto.request.OrderRequest;
import com.example.sweetandkarak.dto.response.OrderResponse;
import com.example.sweetandkarak.enums.OrderStatusEnum;
import com.example.sweetandkarak.exception.InvalidOrderException;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.exception.StockUnavailableException;
import com.example.sweetandkarak.mapper.OrderMapper;
import com.example.sweetandkarak.model.Item;
import com.example.sweetandkarak.model.Order;
import com.example.sweetandkarak.model.User;
import com.example.sweetandkarak.repository.ItemRepository;
import com.example.sweetandkarak.repository.OrderRepository;
import com.example.sweetandkarak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;



    @Transactional
    public OrderResponse placeOrder(String email, OrderRequest request) {
        log.info("Placing order for user: {}, item: {}, quantity: {}", email, request.getItemId(), request.getOrderQuantity());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Item item = itemRepository.findByIdWithLock(request.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + request.getItemId()));



        try {
            if (item.getQuantityAvailable() < request.getOrderQuantity()) {
                throw new StockUnavailableException(
                        "Insufficient stock. Available: " + item.getQuantityAvailable() + ", Requested: " + request.getOrderQuantity()
                );
            }

            item.setQuantityAvailable(item.getQuantityAvailable() - request.getOrderQuantity());
            itemRepository.save(item);
            log.info("Stock deducted for item: {}. Remaining: {}", item.getId(), item.getQuantityAvailable());

        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Optimistic lock conflict for item: {}", item.getId());
            throw new StockUnavailableException("Order conflict detected. Please try again.");
        }

        BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(request.getOrderQuantity()));

        boolean paymentSuccessful = simulatePayment(request.getPaymentReference());
        OrderStatusEnum orderStatus = paymentSuccessful ? OrderStatusEnum.PAID : OrderStatusEnum.FAILED_PAYMENT;

        Order order = Order.builder()
                .user(user)
                .item(item)
                .cafe(item.getCafe())
                .orderQuantity(request.getOrderQuantity())
                .totalOrderPrice(totalPrice)
                .paymentReference(request.getPaymentReference())
                .orderStatus(orderStatus)
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order placed: #{}, status: {}", savedOrder.getId(), savedOrder.getOrderStatus());


        return orderMapper.toResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {
        return orderMapper.toResponse(findOrderById(id));
    }

    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toResponse);
    }

    public Page<OrderResponse> getOrdersByUser(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByUserId(user.getId(), pageable).map(orderMapper::toResponse);
    }

    public Page<OrderResponse> getOrdersByCafe(Long cafeId, Pageable pageable) {
        return orderRepository.findByCafeId(cafeId, pageable).map(orderMapper::toResponse);
    }

    public Page<OrderResponse> getOrdersByStatus(String status, Pageable pageable) {
        return orderRepository.findByOrderStatus(OrderStatusEnum.valueOf(status.toUpperCase()), pageable).map(orderMapper::toResponse);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatusEnum.valueOf(status.toUpperCase()));
        Order updatedOrder = orderRepository.save(order);
        log.info("Order #{} status updated to: {}", orderId, status);
        return orderMapper.toResponse(updatedOrder);
    }

    @Transactional
    public void cancelOrder(String email, Long orderId) {
        Order order = findOrderById(orderId);

        if (!order.getUser().getEmail().equals(email)) {
            throw new InvalidOrderException("You can only cancel your own orders.");
        }

        if (order.getOrderStatus() == OrderStatusEnum.DELIVERED || order.getOrderStatus() == OrderStatusEnum.CANCELLED) {
            throw new InvalidOrderException("Cannot cancel order with status: " + order.getOrderStatus());
        }

        Item item = itemRepository.findByIdWithLock(order.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));




        order.setOrderStatus(OrderStatusEnum.CANCELLED);
        orderRepository.save(order);
        log.info("Order #{} cancelled", orderId);
    }

    private boolean simulatePayment(String paymentReference) {
        return paymentReference != null && !paymentReference.isBlank();
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }
}
