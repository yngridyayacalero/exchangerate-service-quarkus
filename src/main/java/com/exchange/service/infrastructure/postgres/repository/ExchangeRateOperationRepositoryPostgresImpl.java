package com.exchange.service.infrastructure.postgres.repository;

import com.exchange.service.domain.model.ExchangeRateOperation;
import com.exchange.service.domain.port.out.ExchangeRateOperationRepository;
import com.exchange.service.infrastructure.postgres.entity.ExchangeRateOperationEntity;
import com.exchange.service.infrastructure.postgres.mapper.ExchangeRateOperationMapper;
import io.smallrye.mutiny.Uni;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExchangeRateOperationRepositoryPostgresImpl
        implements ExchangeRateOperationRepository, PanacheRepositoryBase<ExchangeRateOperationEntity, Long> {

    @Override
    public Uni<Void> save(ExchangeRateOperation operation) {
        return Uni.createFrom().item(operation)
                .map(ExchangeRateOperationMapper.toEntity)
                .onItem().transformToUni(entity -> this.persist(entity))
                .replaceWithVoid();
    }
}
