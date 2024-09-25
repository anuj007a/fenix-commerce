package com.fenix.commerce.scheduler;

import com.fenix.commerce.service.SyncService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class DataSyncScheduler {

    private final SyncService syncService;

    // Read configuration values from application.properties
    // 'all' or 'tenant'
//    @Value("${sync.mode}")
    private String syncMode = "all";

    // Default tenant ID if mode is 'tenant'
//    @Value("${sync.tenant.id:tenant_1}")
    private String specificTenantId = "tenant_1";

    public DataSyncScheduler(SyncService syncService) {
        this.syncService = syncService;
    }

    // Sync orders periodically based on configuration
    @Scheduled(fixedRate = 3600000)
    public void syncOrders() {
        if ("all".equalsIgnoreCase(syncMode)) {
            syncAllTenantsOrders();
        } else if ("tenant".equalsIgnoreCase(syncMode)) {
            syncTenantOrders(specificTenantId);
        }
    }

    // Sync fulfillments periodically based on configuration
    @Scheduled(fixedRate = 900000) // Example: Sync every 15 minutes
    public void syncFulfillments() {
        if ("all".equalsIgnoreCase(syncMode)) {
            syncAllTenantsFulfillments();
        } else if ("tenant".equalsIgnoreCase(syncMode)) {
            syncTenantFulfillments(specificTenantId);
        }
    }

    // Sync orders for all tenants
    private void syncAllTenantsOrders() {
        // You can fetch tenant IDs from a database or hardcoded for this example
        List<String> tenantIds = List.of("tenant_1", "tenant_2", "tenant_3");

        Flux.fromIterable(tenantIds)
                .flatMap(syncService::syncOrders)
                .subscribe(null,
                        error -> System.err.println("Error syncing orders for tenants: " + error.getMessage()),
                        () -> System.out.println("Successfully synced orders for all tenants."));
    }

    // Sync orders for a specific tenant
    private void syncTenantOrders(String tenantId) {
        syncService.syncOrders(tenantId)
                .subscribe(null,
                        error -> System.err.println("Error syncing orders for tenant " + tenantId + ": " + error.getMessage()),
                        () -> System.out.println("Successfully synced orders for tenant: " + tenantId));
    }

    // Sync fulfillments for all tenants
    private void syncAllTenantsFulfillments() {
        // Fetch tenant IDs from database or hardcoded for this example
        List<String> tenantIds = List.of("tenant_1", "tenant_2", "tenant_3");

        Flux.fromIterable(tenantIds)
                .flatMap(syncService::syncFulfillments)
                .subscribe(null,
                        error -> System.err.println("Error syncing fulfillments for tenants: " + error.getMessage()),
                        () -> System.out.println("Successfully synced fulfillments for all tenants."));
    }

    // Sync fulfillments for a specific tenant
    private void syncTenantFulfillments(String tenantId) {
        syncService.syncFulfillments(tenantId)
                .subscribe(null,
                        error -> System.err.println("Error syncing fulfillments for tenant " + tenantId + ": " + error.getMessage()),
                        () -> System.out.println("Successfully synced fulfillments for tenant: " + tenantId));
    }
}