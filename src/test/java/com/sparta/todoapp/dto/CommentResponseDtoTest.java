package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentResponseDtoTest {

    @Test
    @DisplayName("CommentResponseDto의 생성자의 기능 테스트")
    void CommentResponseDtoConstructorTest(){
        Card card = new Card();
        User user = new User();
        Comment comment = new Comment("내용",user.getUsername(),user,card);

        CommentResponseDto responseDto = new CommentResponseDto(comment);

        assertThat(responseDto.getContent()).isEqualTo("내용");
    }
}