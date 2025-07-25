package com.exchange.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExchangeRateOperation {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime operationDate;
    private String currencySource;
    private String currencyDestination;
    private BigDecimal convertAmount;
    private double rate;
    public ExchangeRateOperation(BigDecimal amount,
                                 LocalDateTime operationDate,
                                 String currencySource,
                                 String currencyDestination,
                                 BigDecimal convertAmount,
                                 double rate) {
        this.amount = amount;
        this.operationDate = operationDate;
        this.currencySource = currencySource;
        this.currencyDestination = currencyDestination;
        this.convertAmount = convertAmount;
        this.rate = rate;

    }
}

