package com.exchange.service.domain.port.out;

import com.exchange.service.domain.model.ExchangeRate;
import io.smallrye.mutiny.Uni;
import java.time.LocalDate;

public interface ExchangeRateRepository {
    Uni<ExchangeRate> findBySourceAndDestinationAndDate(String currencySource, String currencyDestination, LocalDate date);

}
