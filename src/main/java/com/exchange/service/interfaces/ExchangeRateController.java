package com.exchange.service.interfaces;

import com.exchange.service.application.usecase.PerformExchangeUseCase;
import com.exchange.service.interfaces.dto.ExchangeRateRequest;
import com.exchange.service.interfaces.dto.ExchangeRateResponse;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;

/**
 * REST controller that expone el endpoint para realizar la conversión de tipo de cambio.
 *
 * Endpoint principal: /api/exchange-rate
 *
 * Este controlador recibe una solicitud con los parámetros necesarios para convertir una
 * cantidad de una moneda a otra, y retorna el resultado del tipo de cambio aplicado.
 */
@Path("/api/exchange-rate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExchangeRateController {

    /**
     * Caso de uso encargado de realizar la operación de conversión y registro.
     */
    @Inject
    PerformExchangeUseCase performExchangeUseCase;

    /**
     * Endpoint que realiza la conversión de divisas.
     *
     * @param request Objeto {@link ExchangeRateRequest} que contiene:
     *                - amount: Monto a convertir
     *                - currencySource: Moneda de origen
     *                - currencyDestination: Moneda de destino
     * @return {@link Response} con un cuerpo {@link ExchangeRateResponse} que incluye:
     *         - amount: monto original
     *         - exchangedAmount: monto convertido
     *         - currencySource: moneda origen
     *         - currencyDestination: moneda destino
     *         - rate: tipo de cambio aplicado
     */
    @POST
    public Uni<Response> convertCurrency(ExchangeRateRequest request) {
        return performExchangeUseCase.execute(
                request.getAmount(),
                request.getCurrencySource(),
                request.getCurrencyDestination(),
                LocalDate.now()
        ).map(result -> Response.ok(
                ExchangeRateResponse.builder()
                        .amount(result.getAmount())
                        .exchangedAmount(result.getExchangedAmount())
                        .currencySource(result.getCurrencySource())
                        .currencyDestination(result.getCurrencyDestination())
                        .rate(result.getRate())
                        .build()
        ).build());
    }
}
