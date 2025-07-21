package com.exchange.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ExchangeRate {
    private Long id;
    private double rate;
    private LocalDate date;
    private String currencySource;
    private String currencyDestination;
    public ExchangeRate() {
    }
}

