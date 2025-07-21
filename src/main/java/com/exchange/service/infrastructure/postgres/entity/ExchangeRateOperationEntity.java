package com.exchange.service.infrastructure.postgres.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rate_operation")
public class ExchangeRateOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public BigDecimal amount;
    @Column(name="convert_amount")
    public BigDecimal convertAmount;
    public double rate;
    @Column(name="operation_date")
    public LocalDateTime operationDate;
    @Column(name="currency_source")
    public String currencySource;
    @Column(name="currency_destination")
    public String currencyDestination;
}
