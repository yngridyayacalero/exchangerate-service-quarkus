package com.exchange.service.application.usecase;

import com.exchange.service.application.dto.ExchangeRateResultDTO;
import com.exchange.service.application.exception.ExchangeRateNotFoundException;
import com.exchange.service.domain.model.ExchangeRate;
import com.exchange.service.domain.model.ExchangeRateOperation;
import com.exchange.service.domain.port.out.ExchangeRateCache;
import com.exchange.service.domain.port.out.ExchangeRateCalculateRepository;
import com.exchange.service.domain.port.out.ExchangeRateOperationRepository;
import com.exchange.service.domain.port.out.ExchangeRateRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class PerformExchangeUseCaseTest {

    @Inject
    PerformExchangeUseCase useCase;

    @InjectMock
    ExchangeRateRepository exchangeRateRepository;

    @InjectMock
    ExchangeRateCalculateRepository exchangeRateCalculateRepository;

    @InjectMock
    ExchangeRateOperationRepository exchangeRateOperationRepository;

    @InjectMock
    ExchangeRateCache exchangeRateCache;


    @InjectSpy
    PerformExchangeUseCase useCaseSpy;

    @BeforeEach
    void setup() {
        // Este metodo se ejecuta antes de cada test
        // Usamos doAnswer para interceptar cualquier llamada al metodo withTransaction del useCaseSpy
        // Esto es necesario porque Panache.withTransaction requiere un contexto Vert.x real,
        // y los tests unitarios no lo proveen, lo que causaría una IllegalStateException
        // Por eso, evitamos ejecutar la transacción real y simplemente ejecutamos el Supplier directo
        doAnswer(invocation -> {
            Supplier<Uni<Object>> supplier = invocation.getArgument(0);
            return supplier.get();
        }).when(useCaseSpy).withTransaction(any());
    }


    @Test
    void testExecute_WithRedisHit() {
        // Arrange
        // Simulamos un escenario en el que el tipo de cambio se encuentra en redis
        BigDecimal amount = BigDecimal.valueOf(100);
        String currencySource = "USD";
        String currencyDestination = "PEN";
        LocalDate date = LocalDate.now();
        double rate = 3.75;
        BigDecimal convertAmount = BigDecimal.valueOf(375);

        // Creamos un tipo de cambio simulado
        ExchangeRate exchangeRate = new ExchangeRate(1L, rate, date, currencySource, currencyDestination);

        // Simulamos que Redis contiene el tipo de cambio
        when(exchangeRateCache.get(currencySource, currencyDestination, date))
                .thenReturn(Uni.createFrom().item(Optional.of(exchangeRate)));

        // Simulamos que el calculo del monto convertido es exitoso
        when(exchangeRateCalculateRepository.calculate(amount, BigDecimal.valueOf(rate)))
                .thenReturn(Uni.createFrom().item(convertAmount));

        // Simulamos que la operacion de cambio se guarda correctamente.
        when(exchangeRateOperationRepository.save(any()))
                .thenReturn(Uni.createFrom().voidItem());


        // Ejecutamos el caso de uso y validamos los resultados
        useCaseSpy.execute(amount, currencySource, currencyDestination, date)
                .subscribe().with(result -> {
                    // Validamos que el resultado no sea nulo y contenga la información esperada.
                    assertNotNull(result);
                    assertEquals(amount, result.getAmount());
                    assertEquals(convertAmount, result.getExchangedAmount());
                    assertEquals(currencySource, result.getCurrencySource());
                    assertEquals(currencyDestination, result.getCurrencyDestination());
                    assertEquals(rate, result.getRate());
                }, Throwable::printStackTrace);
    }


    @Test
    void testExecute_WithDatabaseFallback() {
        // Arrange
        // Este test simula cuando Redis no contiene el tipo de cambio y se debe consultar la base de datos.
        BigDecimal amount = BigDecimal.valueOf(100);
        String source = "USD";
        String destination = "PEN";
        LocalDate date = LocalDate.now();
        double rate = 0.26;
        BigDecimal convertAmount = BigDecimal.valueOf(26);

        // Simulamos que redis devuelve vacio
        when(exchangeRateCache.get(source, destination, date))
                .thenReturn(Uni.createFrom().item(Optional.empty()));

        // Simulamos que la base de datos contiene el tipo de cambio
        ExchangeRate exchangeRate = new ExchangeRate(2L, rate, date, source, destination);
        when(exchangeRateRepository.findBySourceAndDestinationAndDate(source, destination, date))
                .thenReturn(Uni.createFrom().item(exchangeRate));

        // Simulamos que el tipo de cambio se guarda en cache correctamente
        when(exchangeRateCache.put(source, destination, date, exchangeRate))
                .thenReturn(Uni.createFrom().voidItem());

        // Simulamos que se realiza el calculo correctamente
        when(exchangeRateCalculateRepository.calculate(amount, BigDecimal.valueOf(rate)))
                .thenReturn(Uni.createFrom().item(convertAmount));

        // Simulamos que la operacion de cambio se guarda correctamente
        when(exchangeRateOperationRepository.save(any(ExchangeRateOperation.class)))
                .thenReturn(Uni.createFrom().voidItem());

        // Ejecutamos el caso de uso y validamos los resultados
        useCaseSpy.execute(amount, source, destination, date)
                .subscribe().with(result -> {
                    // Assert
                    assertNotNull(result);
                    assertEquals(amount, result.getAmount());
                    assertEquals(convertAmount, result.getExchangedAmount());
                    assertEquals(source, result.getCurrencySource());
                    assertEquals(destination, result.getCurrencyDestination());
                    assertEquals(rate, result.getRate());
                }, Throwable::printStackTrace);
    }

    @Test
    void execute_ShouldThrowExchangeRateNotFoundException_WhenExchangeRateNotFound() {
        // Simulamos que ni Redis ni la base de datos contienen el tipo de cambio solicitado
        BigDecimal amount = BigDecimal.valueOf(100);
        String source = "EUR"; // usamos un currency diferente para dejar claro que no existe
        String destination = "PEN";
        LocalDate date = LocalDate.now();

        // Redis no tiene el tipo de cambio
        when(exchangeRateCache.get(source, destination, date))
                .thenReturn(Uni.createFrom().item(Optional.empty()));

        // La base de datos tampoco lo tiene (null item)
        when(exchangeRateRepository.findBySourceAndDestinationAndDate(source, destination, date))
                .thenReturn(Uni.createFrom().nullItem());

        // Act & Assert
        // Esperamos que se lance la excepcion específica de tipo de cambio no encontrado
        ExchangeRateNotFoundException exception = assertThrows(
                ExchangeRateNotFoundException.class,
                () -> useCaseSpy.execute(amount, source, destination, date).await().indefinitely()
        );

        // Verificamos los detalles de la excepción
        assertEquals("EXCHANGE_RATE_NOT_FOUND", exception.getCode());
        assertTrue(exception.getMessage().contains("Not found exchange rate"));
        assertTrue(exception.getMessage().contains(source));
        assertTrue(exception.getMessage().contains(destination));
        assertTrue(exception.getMessage().contains(date.toString()));

        // Verificamos que se consulto Redis y la base de datos
        verify(exchangeRateCache).get(source, destination, date);
        verify(exchangeRateRepository).findBySourceAndDestinationAndDate(source, destination, date);

        // Confirmamos que no se intentó guardar en Redis porque no hubo resultado
        verify(exchangeRateCache, never()).put(any(), any(), any(), any());
    }



}
