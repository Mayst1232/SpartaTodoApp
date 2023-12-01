package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CardResponseDtoTest {

    @Test
    @DisplayName("CardResponseDto의 생성자의 기능 테스트")
    void CardResponseDtoConstructorTest(){
        Card card = new Card();
        User user = new User();
        card.setTitle("CardResponseDto 생성자 테스트");
        card.setContent("CardResponseDto 생성자 테스트");
        card.setUser(user);

        CardResponseDto responseDto = new CardResponseDto(card);

        assertThat(card.getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(card.getContent()).isEqualTo(responseDto.getContent());
    }
}