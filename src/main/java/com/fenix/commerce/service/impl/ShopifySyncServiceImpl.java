package com.fenix.commerce.service.impl;

import com.fenix.commerce.model.Fulfillment;
import com.fenix.commerce.model.Order;
import com.fenix.commerce.service.ShopifySyncService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class ShopifySyncServiceImpl implements ShopifySyncService {

    private final WebClient webClient;

    @Value("${shopify.apiKey}")
    private String shopifyApiKey;

    @Value("${shopify.apiSecret}")
    private String shopifyApiSecret;

    @Value("${shopify.storeUrl}")
    private String shopifyStoreUrl;

    public ShopifySyncServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(shopifyStoreUrl).build();
    }

    @Override
    public Flux<Order> fetchOrdersFromShopify(String tenantId) {
        // Construct the Shopify API endpoint for fetching orders
        String shopifyApiEndpoint = "/admin/api/2024-04/orders.json";

        return webClient.get()
                .uri(shopifyApiEndpoint)
                .header("X-Shopify-Access-Token", shopifyApiKey)
                .retrieve()
                .bodyToFlux(Order.class);  // Assumes that `Order` is the appropriate model class
    }

    @Override
    public Flux<Fulfillment> fetchFulfillmentsFromShopify(String orderId) {
        String fulfillmentEndpoint = String.format("/admin/api/2024-04/orders/%s/fulfillments.json", orderId);

        return webClient.get()
                .uri(fulfillmentEndpoint)
                .header("X-Shopify-Access-Token", shopifyApiKey)
                .retrieve()
                .bodyToFlux(Fulfillment.class);
    }
}
