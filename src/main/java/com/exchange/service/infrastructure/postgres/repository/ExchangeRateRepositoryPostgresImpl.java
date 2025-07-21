package com.exchange.service.infrastructure.postgres.repository;

import com.exchange.service.domain.model.ExchangeRate;
import com.exchange.service.domain.port.out.ExchangeRateRepository;
import com.exchange.service.infrastructure.postgres.entity.ExchangeRateEntity;
import com.exchange.service.infrastructure.postgres.mapper.ExchangeRateMapper;
import io.smallrye.mutiny.Uni;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class ExchangeRateRepositoryPostgresImpl implements ExchangeRateRepository, PanacheRepositoryBase<ExchangeRateEntity, Long> {

    @Override
    public Uni<ExchangeRate> findBySourceAndDestinationAndDate(String currencySource, String currencyDestination, LocalDate date) {
        return find("currencySource = ?1 and currencyDestination = ?2 and date = ?3",
                currencySource, currencyDestination, date)
                .firstResult()
                .onItem().ifNotNull().transform(ExchangeRateMapper::toDomain);
    }



}
