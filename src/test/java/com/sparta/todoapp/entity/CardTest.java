package com.sparta.todoapp.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.dto.CardCompleteRequestDto;
import com.sparta.todoapp.dto.CardModifyRequestDto;
import com.sparta.todoapp.dto.CardRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;

class CardTest {

    User user;

    @BeforeEach
    void setUp() {
        user = Mockito.mock(User.class);
        Mockito.when(user.getUsername()).thenReturn("Mayst");
        Mockito.when(user.getPassword()).thenReturn("1234");
    }

    @Test
    @DisplayName("Card에 전달된 CardRequestDto와 User에서 전달된 값과 일치해야 합니다 ")
    void testCardConstructor() {

        // given
        final String title = "제목";
        final String content = "내용";
        final boolean visible = true;
        final boolean complete = false;

        final CardRequestDto requestDto = new CardRequestDto(title, content, visible, complete);

        // when
        Card card = new Card(requestDto, user);

        // then
        assertThat(card.getTitle()).isEqualTo(title);
        assertThat(card.getContent()).isEqualTo(content);
        assertThat(card.isComplete()).isEqualTo(complete);
        assertThat(card.isVisible()).isEqualTo(visible);
        assertThat(card.getUser().getUsername()).isEqualTo("Mayst");
        assertThat(card.getUser().getPassword()).isEqualTo("1234");
    }

    @DisplayName("카드에 적힌 할일이 끝나고 완료 버튼을 누르면 완료됨을 알려줍니다.")
    @Test
    void completeUpdateTest(){
        // given
        Card card = new Card();
        card.setComplete(false);
        CardCompleteRequestDto request = new CardCompleteRequestDto(true);

        // when
        card.completeUpdate(request);

        // then
        assertThat(card.isComplete()).isTrue();
    }

    @DisplayName("카드 내의 정보를 바꿀 때 제목만 바꾸고 싶을 경우를 확인합니다.")
    @Test
    void updateCardTitleTest(){
        // given
        CardModifyRequestDto modifyRequestDto = new CardModifyRequestDto("변경된 제목 1" , null);

        Card card = new Card();
        card.setTitle("기존 제목");
        card.setContent("기존 내용");

        // when
        card.updateCard(modifyRequestDto);

        // then
        assertThat(card.getTitle()).isEqualTo(modifyRequestDto.getTitle());
        assertThat(card.getContent()).isNotEqualTo(modifyRequestDto.getContent());
    }

    @DisplayName("카드 내의 정보를 바꿀 때 내용만 바꾸고 싶을 경우를 확인합니다.")
    @Test
    void updateCardContentTest(){
        // given
        CardModifyRequestDto modifyRequestDto = new CardModifyRequestDto(null, "변경된 내용 1");

        Card card = new Card();
        card.setTitle("기존 제목");
        card.setContent("기존 내용");

        // when
        card.updateCard(modifyRequestDto);

        // then
        assertThat(card.getTitle()).isEqualTo(modifyRequestDto.getContent());
        assertThat(card.getContent()).isNotEqualTo(modifyRequestDto.getTitle());
    }

    @DisplayName("카드 내의 정보를 바꿀 때 제목과 내용 둘 다 바꾸고 싶을 경우를 확인합니다.")
    @Test
    void updateCardBothTest(){
        // given
        CardModifyRequestDto modifyRequestDto = new CardModifyRequestDto("변경된 제목 2", "변경된 내용 2");

        Card card = new Card();
        card.setTitle("기존 제목");
        card.setContent("기존 내용");

        // when - then
        card.updateCard(modifyRequestDto);
        assertThat(card.getTitle()).isEqualTo(modifyRequestDto.getTitle());
        assertThat(card.getContent()).isEqualTo(modifyRequestDto.getContent());
    }
}
