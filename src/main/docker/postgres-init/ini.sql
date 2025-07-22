CREATE SCHEMA IF NOT EXISTS mibanco;

CREATE TABLE IF NOT EXISTS mibanco.exchange_rate (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    currency_source VARCHAR(10) NOT NULL,
    currency_destination VARCHAR(10) NOT NULL,
    rate NUMERIC(10, 4) NOT NULL
);

CREATE TABLE mibanco.exchange_rate_operation (
    id SERIAL PRIMARY KEY,
    amount NUMERIC(19, 2) NOT NULL,
    operation_date TIMESTAMP NOT NULL,
    currency_source VARCHAR(50) NOT NULL,
    currency_destination VARCHAR(50) NOT NULL,
	convert_amount NUMERIC(19, 2) NOT NULL,
	rate NUMERIC(19, 2) NOT NULL

);


INSERT INTO mibanco.exchange_rate (date, currency_source, currency_destination, rate) VALUES
  ('2025-07-21', 'PEN', 'USD', 3.75),
  ('2025-07-21', 'USD', 'PEN', 0.26),
  ('2025-07-22', 'PEN', 'USD', 3.75),
  ('2025-07-22', 'USD', 'PEN', 0.26);