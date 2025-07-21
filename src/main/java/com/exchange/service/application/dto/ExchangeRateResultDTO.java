package com.exchange.service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ExchangeRateResultDTO {
    private BigDecimal amount;
    private BigDecimal exchangedAmount;
    private String currencySource;
    private String currencyDestination;
    private double rate;
}
