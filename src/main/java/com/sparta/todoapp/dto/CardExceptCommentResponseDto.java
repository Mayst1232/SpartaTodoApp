package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CardExceptCommentResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private boolean complete;

    public CardExceptCommentResponseDto(Card card) {
        this.id = card.getId();
        this.username = card.getUser().getUsername();
        this.title = card.getTitle();
        this.content = card.getContent();
        this.complete = card.isComplete();
    }
}
