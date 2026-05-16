package com.example.sweetandkarak.controller;


import com.example.sweetandkarak.model.Order;
import com.example.sweetandkarak.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")

public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }


    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }


    @GetMapping
    public Page<Order> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return orderService.getAllOrders(pageable);
    }


    @GetMapping("/user/{userId}")
    public Page<Order> getOrdersByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrdersByUser(userId, pageable);
    }

    @GetMapping("/cafe/{cafeId}")
    public Page<Order> getOrdersByCafe(
            @PathVariable Long cafeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrdersByCafe(cafeId, pageable);
    }

    @GetMapping("/status")
    public Page<Order> getOrdersByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return orderService.getOrdersByStatus(status, pageable);
    }


    @PatchMapping("/{id}/status")
    public Order updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return orderService.updateOrderStatus(id, status);
    }


    @PatchMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return "Order cancelled";
    }

}