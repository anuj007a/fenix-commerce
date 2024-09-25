package com.fenix.commerce.service.impl;

import com.fenix.commerce.model.Fulfillment;
import com.fenix.commerce.model.Order;
import com.fenix.commerce.service.ElasticsearchService;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private final ReactiveElasticsearchOperations elasticsearchOperations;

    public ElasticsearchServiceImpl(ReactiveElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public Mono<Order> saveOrder(Order order) {
        return elasticsearchOperations.save(order);
    }

    @Override
    public Mono<Fulfillment> saveFulfillment(Fulfillment fulfillment) {
        return elasticsearchOperations.save(fulfillment);
    }

    @Override
    public Flux<Order> getOrdersByTenant(String tenantId) {
        Query query = new CriteriaQuery(Criteria.where("tenantId").is(tenantId));
        return elasticsearchOperations.search(query, Order.class)
                .flatMap(searchHit -> Mono.just(searchHit.getContent())); // Extract content from SearchHit
    }

    @Override
    public Mono<Order> getOrderById(String tenantId, String orderId) {
        Query query = new CriteriaQuery(
                Criteria.where("tenantId").is(tenantId)
                        .and(Criteria.where("orderId").is(orderId))
        );
        return elasticsearchOperations.search(query, Order.class)
                .next()
                .map(SearchHit::getContent);
    }

    @Override
    public Mono<Fulfillment> getFulfillmentById(String fulfillmentId) {
        return elasticsearchOperations.get(fulfillmentId, Fulfillment.class);
    }

    @Override
    public Mono<Void> deleteOrderById(String tenantId, String orderId) {
        return getOrderById(tenantId, orderId)
                .flatMap(order -> elasticsearchOperations.delete(order)
                        .then(Mono.empty()));
    }
}
