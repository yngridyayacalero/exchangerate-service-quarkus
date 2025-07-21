package com.exchange.service.application.exception;

import lombok.Getter;

@Getter
public class ExchangeRateNotFoundException extends RuntimeException {
    private final String code;

    public ExchangeRateNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

}
