package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CardExceptCommentResponseDtoTest {

    @Test
    @DisplayName("카드의 정보 중 comment의 내용은 빼고 전달해 주는 responseDto의 기능 테스트")
    void CardExceptCommentResponseDtoConstructorTest(){
        Card card = new Card();
        User user = new User();
        card.setUser(user);

        CardExceptCommentResponseDto responseDto = new CardExceptCommentResponseDto(card);

        assertThat(card.getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(card.getContent()).isEqualTo(responseDto.getContent());
    }
}