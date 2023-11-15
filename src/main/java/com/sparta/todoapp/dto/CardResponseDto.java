package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String complete;

    public CardResponseDto(Card card){
        this.id = card.getId();
        this.title = card.getTitle();
        this.content = card.getContent();
        this.complete = card.getComplete();
    }
}
