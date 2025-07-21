package com.exchange.service.infrastructure.postgres.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exchange_rate")
public class ExchangeRateEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public LocalDate date;
    @Column(name="currency_source")
    public String currencySource;
    @Column(name="currency_destination")
    public String currencyDestination;
    public double rate;
}
