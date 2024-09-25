package com.fenix.commerce.controller;

import com.fenix.commerce.model.Order;
import com.fenix.commerce.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{tenantId}/{orderId}")
    public Mono<Order> getOrder(@PathVariable String tenantId, @PathVariable String orderId) {
        return orderService.getOrderById(tenantId, orderId);
    }

    @GetMapping("/{tenantId}")
    public Flux<Order> getOrdersByTenant(@PathVariable String tenantId) {
        return orderService.getOrdersByTenant(tenantId);
    }
}
