package com.exchange.service.domain.service;

import com.exchange.service.domain.port.out.ExchangeRateCalculateRepository;
import com.exchange.service.infrastructure.common.ExchangeRateCalculateRepositoryImpl;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeCalculatorTest {

    @Test
    public void shouldCalculateExchangeCorrectly() {
        ExchangeRateCalculateRepository repository = new ExchangeRateCalculateRepositoryImpl();
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal rate = BigDecimal.valueOf(3.75);
        BigDecimal expected = BigDecimal.valueOf(375.00);
        Uni<BigDecimal> resultUni = repository.calculate(amount, rate);
        BigDecimal result = resultUni.await().indefinitely();
        assertEquals(0, expected.compareTo(result));
    }
}
