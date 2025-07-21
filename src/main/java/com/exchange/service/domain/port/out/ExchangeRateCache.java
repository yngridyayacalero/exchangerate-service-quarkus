package com.exchange.service.domain.port.out;

import com.exchange.service.domain.model.ExchangeRate;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateCache {

    Uni<Optional<ExchangeRate>> get(String source, String destination, LocalDate date);

    Uni<Void> put(String source, String destination, LocalDate date, ExchangeRate rate);
}
