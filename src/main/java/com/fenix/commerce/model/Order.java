package com.fenix.commerce.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "orders")
public class Order {
    @Id
    private String orderId;
    private String tenantId;
    private String status;
    private List<String> fulfillmentIds;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getFulfillmentIds() {
        return fulfillmentIds;
    }

    public void setFulfillmentIds(List<String> fulfillmentIds) {
        this.fulfillmentIds = fulfillmentIds;
    }
}