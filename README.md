# Fenix Commerce

Fenix Commerce is a multi-tenant service for syncing order and fulfillment data from Shopify to Elasticsearch. It leverages the power of Spring Boot, Elasticsearch, and reactive programming using Project Reactor to provide efficient and scalable data synchronization for e-commerce platforms.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Elasticsearch Setup](#elasticsearch-setup)
- [Scheduled Jobs](#scheduled-jobs)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Multi-Tenant Syncing**: Handles syncing of multiple tenants (Shopify stores) in parallel.
- **Reactive Programming**: Fully asynchronous, non-blocking data processing using Spring WebFlux and Project Reactor.
- **Scheduled Syncs**: Automatically syncs order and fulfillment data at configurable intervals.
- **Elasticsearch Integration**: Persistent data storage and querying using Elasticsearch.
- **Configurable Modes**: Supports syncing either all tenants or specific ones.

## Technologies

- **Java 17**
- **Spring Boot 3.x**
- **Spring WebFlux**
- **Elasticsearch 8.x**
- **Reactor (Project Reactor)**

## Architecture

The project architecture consists of several key components:

- **Sync Services**: Services responsible for pulling data from Shopify and pushing it to Elasticsearch.
- **Elasticsearch Service**: Interface for communicating with Elasticsearch for CRUD operations.
- **Scheduler**: Automates the syncing process at predefined intervals.
- **Configuration**: Flexible and easy-to-configure properties, supporting multi-tenant setups.

## Installation

### Prerequisites

- Java 17 or later
- Maven
- Elasticsearch 8.x running locally or remotely

### Steps

### 1. Clone the repository:

```bash
    git clone https://github.com/anuj007a/fenix-commerce.git
    cd fenix-commerce
  ```

### 2. Build the project:

```bash
    mvn clean install
  ```

### 3. Run the application:
#### Maven
```bash
    Dev: mvn spring-boot:run -Dspring-boot.run.profiles=dev
    Prod: mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```
#### Jar
   ```bash
    Dev: java -jar your-app.jar --spring.profiles.active=dev
    Prod: java -jar your-app.jar --spring.profiles.active=dev
   ```

## Configuration

Configuration is managed through the `application.yaml` file located in `src/main/resources/`. You can adjust the following settings:

```properties
# Default properties (applies to all profiles unless overridden)
spring:
    application:
        name: shopify-elasticsearch-poc
    elasticsearch:
        uris: http://localhost:9200  # Default Elasticsearch URI (can be overridden by profiles)

shopify:
    apiVersion: 2024-04

# Development environment specific settings
---
spring:
    profiles: dev
    elasticsearch:
        username: elastic
        password: pass
    shopify:
        apiKey: pass
        apiSecret: pass
        storeUrl: https://mystore.myshopify.com/
    sync:
        mode: all
        tenant:
            id: tenant_1 # Used if sync.mode=tenant

# Production environment specific settings
---
spring:
    profiles: prod
    elasticsearch:
        uris: http://localhost:9200
        username: elastic
        password: pass
    shopify:
        apiKey: pass
        apiSecret: pass
        storeUrl: https://mystore.myshopify.com/
    sync:
        mode: all
        tenant:
            id: tenant_1 # Used if sync.mode=tenant
```

## Usage

Once the application is running, it will automatically begin syncing data from the specified Shopify stores to Elasticsearch based on the configured schedules.

### Elasticsearch Endpoints

You can query the synced data directly from Elasticsearch using standard Elasticsearch queries.

### Syncing Data

The data sync is automated using scheduled jobs, but you can also trigger it manually through the service APIs.

## Elasticsearch Setup

Make sure Elasticsearch is up and running on the specified host and port. You can check its status by visiting [http://localhost:9200](http://localhost:9200).

### Index Creation

The necessary indices for orders and fulfillments will be created dynamically when you run the application, if they don't already exist.

## Scheduled Jobs

The application includes scheduled tasks for syncing orders and fulfillments:

- **Order Sync**: Runs every hour (configurable via `@Scheduled` in `DataSyncScheduler`).
- **Fulfillment Sync**: Runs every 15 minutes (configurable as well).

These schedules can be adjusted in the `DataSyncScheduler` class.
