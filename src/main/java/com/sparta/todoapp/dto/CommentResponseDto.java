package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String writer;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime modifyDate;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.writer = comment.getWriter();
        this.content = comment.getContent();
        this.writeDate = comment.getWriteDate();
        this.modifyDate = comment.getModifyDate();
    }
}
