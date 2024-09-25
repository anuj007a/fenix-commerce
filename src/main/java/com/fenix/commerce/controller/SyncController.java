package com.fenix.commerce.controller;

import com.fenix.commerce.service.SyncService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/sync")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/orders/{tenantId}")
    public Mono<Void> syncOrders(@PathVariable String tenantId) {
        return syncService.syncOrders(tenantId);
    }

    @PostMapping("/fulfillments/{tenantId}")
    public Mono<Void> syncFulfillments(@PathVariable String tenantId) {
        return syncService.syncFulfillments(tenantId);
    }

    @GetMapping("/orders/{tenantId}/{orderId}")
    public Mono<Void> fetchOrderWithFulfillments(@PathVariable String tenantId, @PathVariable String orderId) {
        return syncService.fetchOrderWithFulfillments(tenantId, orderId);
    }
}
