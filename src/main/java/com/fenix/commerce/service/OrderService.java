package com.fenix.commerce.service;

import com.fenix.commerce.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<Order> getOrderById(String tenantId, String orderId);

    Flux<Order> getOrdersByTenant(String tenantId);

    Mono<Order> saveOrder(Order order);
}
