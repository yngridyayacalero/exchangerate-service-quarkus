package com.exchange.service.interfaces.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ExchangeRateResponse {
    private BigDecimal amount;
    private BigDecimal exchangedAmount;
    private String currencySource;
    private String currencyDestination;
    private double rate;
}
