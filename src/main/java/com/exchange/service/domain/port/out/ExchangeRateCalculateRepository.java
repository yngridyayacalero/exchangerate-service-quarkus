package com.exchange.service.domain.port.out;

import io.smallrye.mutiny.Uni;
import java.math.BigDecimal;

public interface ExchangeRateCalculateRepository {

    Uni<BigDecimal> calculate(BigDecimal amount, BigDecimal rate);

}
