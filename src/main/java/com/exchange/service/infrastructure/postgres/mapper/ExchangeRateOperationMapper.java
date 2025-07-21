package com.exchange.service.infrastructure.postgres.mapper;

import com.exchange.service.domain.model.ExchangeRateOperation;
import com.exchange.service.infrastructure.postgres.entity.ExchangeRateOperationEntity;
import java.util.function.Function;

public class ExchangeRateOperationMapper {

    public static final Function<ExchangeRateOperation, ExchangeRateOperationEntity> toEntity = operation -> {
        ExchangeRateOperationEntity entity = new ExchangeRateOperationEntity();
        entity.amount = operation.getAmount();
        entity.operationDate = operation.getOperationDate();
        entity.currencySource = operation.getCurrencySource();
        entity.currencyDestination = operation.getCurrencyDestination();
        entity.convertAmount = operation.getConvertAmount();
        entity.rate = operation.getRate();
        return entity;
    };
}
