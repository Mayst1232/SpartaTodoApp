package com.sparta.todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {
    private String title;
    private String content;
    private boolean visible;
    private boolean complete = false;
}
