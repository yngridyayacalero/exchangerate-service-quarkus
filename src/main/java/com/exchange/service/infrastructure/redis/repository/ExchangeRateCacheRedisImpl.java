package com.exchange.service.infrastructure.redis.repository;

import com.exchange.service.domain.model.ExchangeRate;
import com.exchange.service.domain.port.out.ExchangeRateCache;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class ExchangeRateCacheRedisImpl implements ExchangeRateCache {

    @Inject
    ReactiveRedisDataSource dataSource;

    private String buildKey(String currencySource, String currencyDestination, LocalDate date) {
        return String.format("exchange-rate:%s:%s:%s", currencySource, currencyDestination, date);
    }

    @Override
    public Uni<Optional<ExchangeRate>> get(String currencySource, String currencyDestination, LocalDate date) {
        String key = buildKey(currencySource, currencyDestination, date);
        return dataSource.value(String.class, ExchangeRate.class)
                .get(key)
                .map(Optional::ofNullable);
    }

    @Override
    public Uni<Void> put(String currencySource, String currencyDestination, LocalDate date, ExchangeRate rate) {
        String key = buildKey(currencySource, currencyDestination, date);
        return dataSource.value(String.class, ExchangeRate.class)
                .setex(key, 300, rate)
                .replaceWithVoid();
    }
}
