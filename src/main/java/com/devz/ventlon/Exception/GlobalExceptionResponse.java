package com.devz.ventlon.Exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class GlobalExceptionResponse {
    Integer status;
    String errorMessage;
}