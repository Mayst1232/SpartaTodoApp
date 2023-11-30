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
}
