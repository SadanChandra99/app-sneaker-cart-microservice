package com.app.sneakers.cart.exception.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
    private String reason;
    private String path;
    private int status;
    private LocalDateTime timestamp;
}