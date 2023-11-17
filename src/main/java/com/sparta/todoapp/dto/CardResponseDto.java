package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CardResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private boolean complete;

    private List<CommentResponseDto> commentList = new ArrayList<>();

    public CardResponseDto(Card card){
        this.id = card.getId();
        this.username = card.getUser().getUsername();
        this.title = card.getTitle();
        this.content = card.getContent();
        this.complete = card.isComplete();
        for (Comment comment : card.getCommentList()) {
            commentList.add(new CommentResponseDto(comment));
        }
    }
}
