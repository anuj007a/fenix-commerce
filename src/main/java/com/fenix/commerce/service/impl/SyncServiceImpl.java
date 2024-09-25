package com.fenix.commerce.service.impl;

import com.fenix.commerce.model.Fulfillment;
import com.fenix.commerce.service.ElasticsearchService;
import com.fenix.commerce.service.ShopifySyncService;
import com.fenix.commerce.service.SyncService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class SyncServiceImpl implements SyncService {

    private final ShopifySyncService shopifySyncService;
    private final ElasticsearchService elasticsearchService;

    public SyncServiceImpl(ShopifySyncService shopifySyncService, ElasticsearchService elasticsearchService) {
        this.shopifySyncService = shopifySyncService;
        this.elasticsearchService = elasticsearchService;
    }

    @Override
    public Mono<Void> syncOrders(String tenantId) {
        // Fetch orders from Shopify for the given tenant and save them to Elasticsearch
        return shopifySyncService.fetchOrdersFromShopify(tenantId)
                .flatMap(order -> elasticsearchService.saveOrder(order))
                .then();  // Signal completion
    }

    @Override
    public Mono<Void> syncFulfillments(String tenantId) {
        // Fetch orders for the tenant and sync their fulfillments
        return elasticsearchService.getOrdersByTenant(tenantId)
                .flatMap(order -> shopifySyncService.fetchFulfillmentsFromShopify(order.getOrderId())
                        .flatMap(fulfillment -> {
                            // Save each fulfillment and update the corresponding order
                            return elasticsearchService.saveFulfillment(fulfillment)
                                    .then(Mono.just(fulfillment));  // Ensure the fulfillment is returned after saving
                        })
                        .collectList()  // Collect fulfillments into a list to update order fulfillment IDs
                        .flatMap(fulfillments -> {
                            List<String> fulfillmentIds = fulfillments.stream()
                                    .map(Fulfillment::getFulfillmentId)
                                    .toList();
                            order.setFulfillmentIds(fulfillmentIds);  // Update the order with new fulfillment IDs
                            return elasticsearchService.saveOrder(order);  // Save the updated order
                        })
                )
                .then();  // Signal completion
    }

    @Override
    public Mono<Void> fetchOrderWithFulfillments(String tenantId, String orderId) {
        // Fetch the order from Elasticsearch; if fulfillments are missing, fetch them from Shopify
        return elasticsearchService.getOrderById(tenantId, orderId)
                .flatMap(order -> {
                    // Check if fulfillments are missing or need to be updated
                    if (Objects.isNull(order.getFulfillmentIds()) || order.getFulfillmentIds().isEmpty()) {
                        return shopifySyncService.fetchFulfillmentsFromShopify(orderId)
                                .collectList()
                                .flatMap(fulfillments -> {
                                    // Update the order with the fetched fulfillments
                                    List<String> fulfillmentIds = fulfillments.stream()
                                            .map(Fulfillment::getFulfillmentId)
                                            .toList();
                                    order.setFulfillmentIds(fulfillmentIds);
                                    return elasticsearchService.saveOrder(order);  // Save the updated order
                                });
                    }
                    // If fulfillments already exist, no further action needed
                    return Mono.just(order);
                })
                .then();  // Signal completion
    }
}