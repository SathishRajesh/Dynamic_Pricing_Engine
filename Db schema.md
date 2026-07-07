# Database Schema

Database: dynamic_db (MySQL)

## product

| Column | Type | Notes |
|---|---|---|
| id | BIGINT | Primary Key |
| name | VARCHAR(255) | Product name |
| base_price | DECIMAL(12,2) | Original price |
| stock_quantity | INT | Current stock |
| version | BIGINT | For locking |

## pricing_rule

| Column | Type | Notes |
|---|---|---|
| id | BIGINT | Primary Key |
| type | VARCHAR(20) | SURGE, TIME_BASED, or INVENTORY |
| value | DECIMAL(8,4) | Multiplier |
| rule_condition | VARCHAR(255) | Example: demand>80 |
| priority | INT | Higher applies first |
| active | BOOLEAN | Enable or disable |
| version | BIGINT | For locking |

## dynamic_price

| Column | Type | Notes |
|---|---|---|
| id | BIGINT | Primary Key |
| product_id | BIGINT | Links to product |
| final_price | DECIMAL(12,2) | Calculated price |
| timestamp | DATETIME | When calculated |

## Relationship

One product has many dynamic_price records.