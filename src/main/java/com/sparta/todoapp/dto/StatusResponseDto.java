package com.sparta.todoapp.dto;

import lombok.Data;

@Data
public class StatusResponseDto {
    private final String message;
    private final int status;

    public StatusResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}

//public record ErrorResponseDto(
//        String message,
//        int status
//){
//}