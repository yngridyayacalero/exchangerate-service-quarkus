package com.exchange.service.infrastructure.common;

import com.exchange.service.domain.port.out.ExchangeRateCalculateRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ApplicationScoped
public class ExchangeRateCalculateRepositoryImpl implements ExchangeRateCalculateRepository {
    public Uni<BigDecimal> calculate(BigDecimal amount, BigDecimal rate) {
        return Uni.createFrom().item(() -> amount.multiply(rate).setScale(2, RoundingMode.HALF_UP));
    }
}
