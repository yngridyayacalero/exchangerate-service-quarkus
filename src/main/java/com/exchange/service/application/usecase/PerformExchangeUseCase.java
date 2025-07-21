package com.exchange.service.application.usecase;

import com.exchange.service.application.dto.ExchangeRateResultDTO;
import com.exchange.service.application.exception.ExchangeRateNotFoundException;
import com.exchange.service.domain.model.ExchangeRate;
import com.exchange.service.domain.model.ExchangeRateOperation;
import com.exchange.service.domain.port.out.ExchangeRateCache;
import com.exchange.service.domain.port.out.ExchangeRateCalculateRepository;
import com.exchange.service.domain.port.out.ExchangeRateRepository;
import com.exchange.service.domain.port.out.ExchangeRateOperationRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import java.math.BigDecimal;
import java.time.LocalDate;

@ApplicationScoped
public class PerformExchangeUseCase {

    private static final Logger LOG = Logger.getLogger(PerformExchangeUseCase.class);

    @Inject
    ExchangeRateRepository exchangeRateRepository;

    @Inject
    ExchangeRateCalculateRepository exchangeRateCalculateRepository;

    @Inject
    ExchangeRateOperationRepository exchangeRateOperationRepository;

    @Inject
    ExchangeRateCache exchangeRateCache;

    public Uni<ExchangeRateResultDTO> execute(BigDecimal amount,
                                              String currencySource,
                                              String currencyDestination,
                                              LocalDate date) {

        return exchangeRateCache.get(currencySource, currencyDestination, date)
                .onItem().transformToUni(optional -> {
                    if (optional.isPresent()) {
                        LOG.debugf("GET_FROM_REDIS: %s -> %s (%s)", currencySource, currencyDestination, date);
                        return Panache.withTransaction(() -> calculateAndPersist(amount, optional.get()));
                    }
                    LOG.debugf("GET_FROM_DATABASE: %s -> %s (%s)", currencySource, currencyDestination, date);
                    return Panache.withTransaction(() ->
                            exchangeRateRepository
                                    .findBySourceAndDestinationAndDate(currencySource, currencyDestination, date)
                                    .onItem().ifNull().failWith(() -> {
                                                LOG.errorf("ERROR_EXCHANGE_RATE_NO_FOUND %s %s", currencySource, currencyDestination);
                                                return new ExchangeRateNotFoundException("EXCHANGE_RATE_NOT_FOUND",
                                                        "Not found exchange rate " + currencySource + " a " + currencyDestination + " en " + date);
                                        }
                                    )
                                    .onItem().transformToUni(exchangeRate ->
                                            exchangeRateCache.put(currencySource, currencyDestination, date, exchangeRate)
                                                    .replaceWith(exchangeRate)
                                    )
                                    .onItem().transformToUni(exchangeRate -> calculateAndPersist(amount, exchangeRate))
                    );
                });
    }


    private Uni<ExchangeRateResultDTO> calculateAndPersist(BigDecimal amount, ExchangeRate exchangeRate) {
        return exchangeRateCalculateRepository
                .calculate(amount, BigDecimal.valueOf(exchangeRate.getRate()))
                .onItem().transformToUni(result ->
                        {
                            ExchangeRateOperation operation = new ExchangeRateOperation(
                                    null,
                                    amount,
                                    LocalDate.now().atStartOfDay(),
                                    exchangeRate.getCurrencySource(),
                                    exchangeRate.getCurrencyDestination()
                            );

                            return exchangeRateOperationRepository
                                    .save(operation)
                                    .replaceWith(new ExchangeRateResultDTO(
                                            amount,
                                            result,
                                            exchangeRate.getCurrencySource(),
                                            exchangeRate.getCurrencyDestination(),
                                            exchangeRate.getRate()
                                    ));
                        }
                );
    }
}
