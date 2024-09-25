package com.fenix.commerce.service.impl;

import com.fenix.commerce.model.Order;
import com.fenix.commerce.repositories.OrderRepository;
import com.fenix.commerce.service.OrderService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Mono<Order> getOrderById(String tenantId, String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Flux<Order> getOrdersByTenant(String tenantId) {
        return orderRepository.findByTenantId(tenantId);
    }

    @Override
    public Mono<Order> saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
