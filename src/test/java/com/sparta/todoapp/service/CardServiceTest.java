package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CardExceptCommentResponseDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    CardService cardService;

    @Mock
    CardRepository cardRepository;


    @Test
    void testCreateCard() {
        // given
        User user = new User();
        CardRequestDto requestDto = new CardRequestDto("할일 제목", "할일 내용", true, false);
        Card card = new Card(requestDto, user);
        System.out.println(user.getUsername());

        given(cardRepository.save(any())).willReturn(card);

        // when
        CardExceptCommentResponseDto createCard = cardService.createCard(requestDto, user);

        // then
        assertThat(requestDto.getTitle()).isEqualTo(createCard.getTitle());
    }

    @Test
    void getCards() {
        // given
        // 주어진 입력들
        CardRequestDto requestDto = new CardRequestDto("찾은 카드 제목", "찾은 카드 내용",false, false);

        User findUser = new User();
        findUser.setId(100L);

        //Card card = Mockito.spy(new Card(requestDto, findUser));

        Card card = new Card(requestDto, findUser);
        card.setId(1L);

        // 카드의 아이디는 1L 이다.
//        Mockito.when(card.getId()).thenReturn(1L);
        // 1L 인 카드를 조회하면 등록된 카드를 반환한다.
        given(cardRepository.findById(1L)).willReturn(Optional.of(card));
        given(cardRepository.findByUserAndId(findUser, 1L)).willReturn(Optional.of(card));
        // when
        CardResponseDto responseDto = cardService.getCards(1L, findUser);
        // then
        assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    void getCardsfail(){  // 내가 작성한 카드가 아닌데 << visible -> false 그러면 안보여야되거든요?
        // given
        CardRequestDto requestDto = new CardRequestDto("찾은 카드 제목", "찾은 카드 내용", false, false);
        User createUser = new User();
        createUser.setId(100L);
        Card card = new Card(requestDto, createUser);
        card.setId(1L);

        User findUser = new User();
        findUser.setId(200L);

        given(cardRepository.findById(1L)).willReturn(Optional.of(card));
        given(cardRepository.findByUserAndId(createUser, 1L)).willReturn(Optional.of(card));
        // when then
//        System.out.println(cardRepository.findByUserAndId(findUser, 1L).isEmpty());
        Exception exception = assertThrows(NullPointerException.class, () -> cardService.getCards(1L, findUser));
        assertThat("해당하는 카드가 없습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    void getAllCards() {
    }

    @Test
    void getTitleCards() {
    }

    @Test
    void cardModify() {
    }

    @Test
    void completeTodo() {
    }

    @Test
    void deleteCard() {
    }

    @Test
    void checkCard() {
    }
}