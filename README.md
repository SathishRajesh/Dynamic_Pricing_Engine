# Dynamic Pricing Engine

A Spring Boot backend that changes product prices based on demand, time, and stock.

## Tech Stack

| Component | Choice |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Database | MySQL |
| Cache | Caffeine |
| Docs | Swagger (springdoc-openapi) |
| Build Tool | Maven |

## Architecture

Controller → Service → Repository → Database

Inside the service, a Strategy Factory picks the right pricing strategy based on rule type.

## Package Structure

| Package | Purpose |
|---|---|
| entity | Product, PricingRule, DynamicPrice, RuleType |
| repository | JPA repositories |
| dto | Request and response objects |
| service | Business logic |
| service.strategy | Strategy Pattern classes |
| controller | REST APIs |
| config | Cache configuration |
| exception | Custom exceptions and handler |

## Key Design Decisions

| Feature | How it's handled |
|---|---|
| Strategy Pattern | Each rule type has its own strategy class. Factory picks the right one. |
| Conflicting rules | Priority field decides order. Higher priority runs first. |
| Concurrency | @Version for optimistic locking. Pessimistic lock during calculation. |
| Rule updates mid-process | Cache is cleared on every rule change. |
| N+1 queries | Entity graph fetches product in one query. |
| Caching | Price result cached for 30 seconds. |

## Setup

1. Create the database (auto-created on first run):
```
CREATE DATABASE dynamic_db;
```

2. Set your DB password as an environment variable:
```
export DB_PASSWORD=your_password
```

3. Run the app:
```
mvn spring-boot:run
```

4. Open Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | /api/products | Create product |
| GET | /api/products/{id} | Get one product |
| GET | /api/products | List all products |
| PATCH | /api/products/{id}/stock | Update stock |
| POST | /api/admin/rules | Create rule |
| PUT | /api/admin/rules/{id} | Update rule |
| DELETE | /api/admin/rules/{id} | Deactivate rule |
| GET | /api/admin/rules | List active rules |
| POST | /api/prices/{productId}/calculate | Calculate price |

## Rule Condition Format

| Rule Type | Format | Example |
|---|---|---|
| SURGE | demand>N | demand>80 |
| TIME_BASED | hour>=X,hour<=Y | hour>=18,hour<=22 |
| INVENTORY | stock<N | stock<10 |

## Edge Cases Handled

| Case | Solution |
|---|---|
| Conflicting rules | Priority field resolves order |
| Demand spikes | Pessimistic lock prevents race conditions |
| Rule updates mid-process | Cache eviction keeps data fresh |

## Testing

Unit tests written using JUnit and Mockito. 9 test cases, all passing.

| Test Class | What it checks |
|---|---|
| SurgePricingStrategyTest | Surge rule applies above threshold, not below |
| TimeBasedPricingStrategyTest | Time rule applies inside hour range, not outside |
| InventoryBasedPricingStrategyTest | Inventory rule applies below stock threshold |
| PricingEngineServiceTest | Returns base price when no rules apply |

Also tested manually with Swagger UI. Positive case applies all rules correctly. Negative case returns base price with no rules applied.

Run tests:
```
mvn test
```# Dynamic Pricing Engine


Tested with Swagger UI. Positive case applies all rules correctly. Negative case returns base price with no rules applied.
