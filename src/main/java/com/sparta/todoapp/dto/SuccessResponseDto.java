package com.sparta.todoapp.dto;

import lombok.Data;

@Data
public class SuccessResponseDto {
    private final String message;
    private final int value;

    public SuccessResponseDto(String message, int value) {
        this.message = message;
        this.value = value;
    }
}
