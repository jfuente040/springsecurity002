package com.jfuente040.springsecurity002.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"message", "backendMessage", "method", "url", "timestamp"})
public record ErrorResponse(String message,
                           String backendMessage,
                           String method,
                           String url,
                           String timestamp) {
}
