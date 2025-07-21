package com.exchange.service.infrastructure.rest.mapper;


import com.exchange.service.application.exception.ExchangeRateNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExchangeRateExceptionMapper implements ExceptionMapper<ExchangeRateNotFoundException> {

    @Override
    public Response toResponse(ExchangeRateNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorDTO(ex.getCode(), ex.getMessage()))
                .build();
    }

    public static class ErrorDTO {
        public String code;
        public String message;

        public ErrorDTO(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}

