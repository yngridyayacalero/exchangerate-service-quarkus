package com.exchange.service.infrastructure.postgres.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rate_operation")
public class ExchangeRateOperationEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public BigDecimal amount;
    @Column(name="operation_date")
    public LocalDateTime operationDate;
    @Column(name="currency_source")
    public String currencySource;
    @Column(name="currency_destination")
    public String currencyDestination;
}
