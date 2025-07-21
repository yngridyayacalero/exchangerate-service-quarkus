package com.exchange.service.interfaces.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExchangeRateRequest {
    private BigDecimal amount;
    private String currencySource;
    private String currencyDestination;
}
