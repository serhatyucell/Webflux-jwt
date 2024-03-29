package com.devz.ventlon.Exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


//@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON));
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        GlobalExceptionResponse exceptionResponse = GlobalExceptionResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        if (ex instanceof CustomException customException) {
            response.setStatusCode(customException.getStatus());
            exceptionResponse.setStatus(customException.getStatus().value());
            exceptionResponse.setErrorMessage(customException.getErrorMessage());
        }

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(exceptionResponse));
            } catch (Exception e) {
                e.printStackTrace();
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}