package com.exchange.service.domain.port.out;

import com.exchange.service.domain.model.ExchangeRateOperation;
import io.smallrye.mutiny.Uni;

public interface ExchangeRateOperationRepository {
    Uni<Void> save(ExchangeRateOperation operation);
}
