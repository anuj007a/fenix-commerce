package com.fenix.commerce.service;

import com.fenix.commerce.model.Fulfillment;
import com.fenix.commerce.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ElasticsearchService {
    Mono<Order> saveOrder(Order order);

    Mono<Fulfillment> saveFulfillment(Fulfillment fulfillment);

    Flux<Order> getOrdersByTenant(String tenantId);

    Mono<Order> getOrderById(String tenantId, String orderId);

    Mono<Fulfillment> getFulfillmentById(String fulfillmentId);

    Mono<Void> deleteOrderById(String tenantId, String orderId);
}
