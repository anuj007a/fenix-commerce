package com.fenix.commerce.service;

import com.fenix.commerce.model.Fulfillment;
import com.fenix.commerce.model.Order;
import reactor.core.publisher.Flux;

public interface ShopifySyncService {
    Flux<Order> fetchOrdersFromShopify(String tenantId);

    Flux<Fulfillment> fetchFulfillmentsFromShopify(String orderId);
}
