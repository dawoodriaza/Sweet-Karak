package com.example.sweetandkarak.service;

import com.example.sweetandkarak.enums.OrderStatusEnum;
import com.example.sweetandkarak.exception.InvalidOrderException;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.exception.StockUnavailableException;
import com.example.sweetandkarak.model.Item;
import com.example.sweetandkarak.model.Order;

import com.example.sweetandkarak.repository.ItemRepository;
import com.example.sweetandkarak.repository.OrderRepository;

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
public class OrderService {

    private final OrderRepository orderRepository;

    private final ItemRepository itemRepository;

    private final ReentrantLock stockLock = new ReentrantLock();

    public OrderService(OrderRepository orderRepository,
                        ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Order placeOrder(Long itemId, int quantity, String paymentReference) {



        Item item = itemRepository.findByIdWithLock(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));

        stockLock.lock();
        try {

            if (item.getQuantityAvailable() < quantity) {
                throw new StockUnavailableException(
                        "Insufficient stock. Available: " + item.getQuantityAvailable()
                                + ", Requested: " + quantity
                );
            }

            item.setQuantityAvailable(item.getQuantityAvailable() - quantity);
            itemRepository.save(item);

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new StockUnavailableException("Order conflict detected. Please try again.");
        } finally {
            stockLock.unlock();
        }

        BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(quantity));

        OrderStatusEnum orderStatus =
                (paymentReference != null && !paymentReference.isBlank())
                        ? OrderStatusEnum.PAID
                        : OrderStatusEnum.FAILED_PAYMENT;

        Order order = Order.builder()
                .item(item)
                .cafe(item.getCafe())
                .orderQuantity(quantity)
                .totalOrderPrice(totalPrice)
                .paymentReference(paymentReference)
                .orderStatus(orderStatus)
                .build();

        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }



    public Page<Order> getOrdersByCafe(Long cafeId, Pageable pageable) {
        return orderRepository.findByCafeId(cafeId, (java.awt.print.Pageable) pageable);
    }

    public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
        OrderStatusEnum orderStatus = OrderStatusEnum.valueOf(status.toUpperCase());
        return orderRepository.findByOrderStatus(orderStatus, (java.awt.print.Pageable) pageable);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {

        Order order = getOrderById(orderId);

        OrderStatusEnum newStatus = OrderStatusEnum.valueOf(status.toUpperCase());
        order.setOrderStatus(newStatus);

        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = getOrderById(orderId);

        if (order.getOrderStatus() == OrderStatusEnum.DELIVERED
                || order.getOrderStatus() == OrderStatusEnum.CANCELLED) {

            throw new InvalidOrderException(
                    "Cannot cancel order with status: " + order.getOrderStatus()
            );
        }

        Item item = itemRepository.findByIdWithLock(order.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        stockLock.lock();
        try {

            item.setQuantityAvailable(
                    item.getQuantityAvailable() + order.getOrderQuantity()
            );

            itemRepository.save(item);

        } finally {
            stockLock.unlock();
        }

        order.setOrderStatus(OrderStatusEnum.CANCELLED);
        orderRepository.save(order);
    }
}