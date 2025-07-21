package com.exchange.service.infrastructure.postgres.mapper;

import com.exchange.service.domain.model.ExchangeRate;
import com.exchange.service.infrastructure.postgres.entity.ExchangeRateEntity;

public class ExchangeRateMapper {

    public static ExchangeRate toDomain(ExchangeRateEntity entity) {
        return new ExchangeRate(
                entity.id,
                entity.rate,
                entity.date,
                entity.currencySource,
                entity.currencyDestination
        );
    }
}

