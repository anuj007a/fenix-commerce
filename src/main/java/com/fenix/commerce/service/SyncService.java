package com.fenix.commerce.service;

import reactor.core.publisher.Mono;

public interface SyncService {
    Mono<Void> syncOrders(String tenantId);

    Mono<Void> syncFulfillments(String tenantId);

    Mono<Void> fetchOrderWithFulfillments(String tenantId, String orderId);
}
