package com.fenix.commerce.repositories;

import com.fenix.commerce.model.Order;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveElasticsearchRepository<Order, String> {
    Flux<Order> findByTenantId(String tenantId);
}
