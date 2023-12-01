package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.*;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    CardService cardService;

    @Mock
    CardRepository cardRepository;


    @Test
    @DisplayName("CardService의 createCard의 메소드 동작 테스트")
    void createCardTest() {
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
    @DisplayName("할일 카드의 단건 조회하는 기능 중 작성자가 visible을 false로 설정하여도 자기 자신은 할일 카드를 확인할 수 있습니다.")
    void getCardsSuccessTest() {
        // given
        CardRequestDto requestDto = new CardRequestDto("찾은 카드 제목", "찾은 카드 내용", false, false);

        User findUser = new User();
        findUser.setId(100L);
        Card card = new Card(requestDto, findUser);
        card.setId(1L);

        given(cardRepository.findById(1L)).willReturn(Optional.of(card));
        given(cardRepository.findByUserAndId(findUser, 1L)).willReturn(Optional.of(card));

        // when
        CardResponseDto responseDto = cardService.getCards(1L, findUser);

        // then
        assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    @DisplayName("할일 카드의 단건 조회 중 작성자가 visible을 false로 설정해놓으면 다른 사람은 그 카드를 확인할 수 없습니다.")
    void getCardsFailTest() {
        // given
        CardRequestDto requestDto = new CardRequestDto("찾은 카드 제목", "찾은 카드 내용", false, false);
        User createUser = new User();
        createUser.setId(100L);
        Card card = new Card(requestDto, createUser);
        card.setId(1L);

        User findUser = new User();
        findUser.setId(200L);

        given(cardRepository.findById(1L)).willReturn(Optional.of(card));
        given(cardRepository.findByUserAndId(findUser, 1L)).willReturn(Optional.empty());

        // when - then
        Exception exception = assertThrows(NullPointerException.class, () -> cardService.getCards(1L, findUser));
        assertThat("해당하는 카드가 없습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("할일 카드의 전체 조회 중 visible이 true인 카드들만 조회하는 기능 테스트")
    void getAllCardsTest() {
        // given
        List<Card> cardList = new ArrayList<>();
        User user = new User("Hwang", "1234");

        for (int i = 0; i < 4; i++) {
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 " + i, "할일 카드 내용 " + i, i % 2 == 0, false);
            Card card = new Card(requestDto, user);
            if (card.isVisible()) {
                cardList.add(card);
            }
        }

        given(cardRepository.findAllByVisible(true)).willReturn(cardList);

        // when
        List<CardExceptCommentResponseDto> result = cardService.getAllCards();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("찾고 있는 제목의 카드가 모두 visible true일 때 찾고 있는 모든 카드를 찾아주는 기능 테스트")
    void getTitleCardsTest() {
        // given
        User user = new User();
        List<Card> cardList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 ", "할일 카드 내용 " + i, true, false);
            Card card = new Card(requestDto, user);
            cardList.add(card);
        }

        CardTitleRequestDto titleRequestDto = new CardTitleRequestDto("할일 카드 제목");

        given(cardRepository.findAllByTitle(titleRequestDto.getTitle())).willReturn(cardList);

        // when
        List<CardExceptCommentResponseDto> result = cardService.getTitleCards(titleRequestDto, user);

        // then
        assertThat(result).hasSize(4);
    }

    @Test
    @DisplayName("찾고 있는 제목의 카드가 존재하지 않아 예외를 발생시키는 기능 테스트")
    void getTitleCardsFailCardListIsEmptyTest() {
        // given
        User user = new User();
        List<Card> cardList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 ", "할일 카드 내용 " + i, true, false);
            Card card = new Card(requestDto, user);
            cardList.add(card);
        }

        CardTitleRequestDto titleRequestDto = new CardTitleRequestDto("없는 카드 제목");

        given(cardRepository.findAllByTitle(titleRequestDto.getTitle()))
                .willReturn(cardList = new ArrayList<>());

        //when
        Exception exception = assertThrows(NullPointerException.class, () -> cardService.getTitleCards(titleRequestDto, user));

        assertThat("해당하는 제목의 게시물이 존재하지 않습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("찾고 있는 제목의 할일 카드가 visible이 false이지만 작성자가 본인이면 찾을 수 있게 해주는 기능 테스트")
    void getTitleCardsVisibleIsFalseTest() {
        // given
        User user = new User();
        List<Card> cardList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 ", "할일 카드 내용 " + i, false, false);
            Card card = new Card(requestDto, user);
            cardList.add(card);
        }

        CardTitleRequestDto titleRequestDto = new CardTitleRequestDto("없는 카드 제목");

        given(cardRepository.findAllByTitle(titleRequestDto.getTitle()))
                .willReturn(cardList);

        for (Card card : cardList) {
            given(cardRepository.findByUserAndTitle(user, titleRequestDto.getTitle()))
                    .willReturn(card);
        }

        //when
        List<CardExceptCommentResponseDto> result = cardService.getTitleCards(titleRequestDto, user);

        // then
        assertThat(result).hasSize(4);
    }

    @Test
    @Disabled
    @DisplayName("자신의 할일 카드의 제목/내용을 수정할 때의 동작 테스트")
    void cardModifyTest() {
        // given
        User user = new User();
        Long id = 3L;

        checkCard(user, id);

        CardModifyRequestDto requestDto = new CardModifyRequestDto("바꿀 제목", "바꿀 내용");

        // when
        CardExceptCommentResponseDto responseDto = cardService.cardModify(3L, requestDto, user);

        // then
        assertThat("바꿀 제목").isEqualTo(responseDto.getTitle());
    }

    @Test
    @DisplayName("자신의 할일 카드의 완료 여부를 수정할 때의 동작 테스트")
    void completeTodo() {
        // given
        User user = new User();
        Long id = 3L;

        checkCard(user, id);

        CardCompleteRequestDto requestDto = new CardCompleteRequestDto(true);

        // when
        CardExceptCommentResponseDto responseDto = cardService.completeTodo(3L, requestDto, user);

        // then
        assertThat(true).isEqualTo(responseDto.isComplete());
    }

    @Test
    @DisplayName("자신의 카드를 삭제하는 기능 테스트")
    void deleteCardTest() {
        // given
        User user = new User();
        Long id = 3L;
        checkCard(user, id);

        // when
        cardService.deleteCard(id, user);

        // then
        verify(cardRepository, times(1)).delete(any());
    }

    void checkCard(User user, Long id) {
        // given
        List<Card> cardList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 " + i, "할일 카드 내용 " + i, true, false);
            Card card = new Card(requestDto, user);
            card.setId((long) i + 1);
            cardList.add(card);
        }

        given(cardRepository.findAllByUser(user)).willReturn(cardList);

        given(cardRepository.findById(anyLong())).willReturn(Optional.of(cardList.get(2)));

        // when
        Card myCard = cardService.checkCard(id, user);
    }

    @Test
    @DisplayName("수정/삭제 하려는 카드가 자신의 할일 카드일때 성공 테스트")
    void checkCardTestSuccess() {
        // given
        User user = new User();
        Long id = 3L;
        List<Card> cardList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 " + i, "할일 카드 내용 " + i, true, false);
            Card card = new Card(requestDto, user);
            card.setId((long) i + 1);
            cardList.add(card);
        }

        given(cardRepository.findAllByUser(user)).willReturn(cardList);

        given(cardRepository.findById(id)).willReturn(Optional.of(cardList.get(2)));

        // when
        Card myCard = cardService.checkCard(id, user);
        // then
        assertThat("할일 카드 제목 2").isEqualTo(myCard.getTitle());
    }

    @Test
    @DisplayName("수정/삭제 하려는 카드가 존재하지 않을 때 실패 테스트 예외 처리")
    void checkCardTestFailCardListIsEmpty() {
        // given
        List<Card> cardList = new ArrayList<>();
        User user = new User();
        Long id = 3L;

        given(cardRepository.findAllByUser(user)).willReturn(cardList);

        // when
        Exception exception = assertThrows(NullPointerException.class,
                () -> cardService.checkCard(id, user));

        // then
        assertThat("작성한 카드가 존재하지 않습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("수정/삭제 하려는 카드가 자신의 것이 아닐 때 실패 테스트 예외 처리")
    void checkCardTestFailCardIsNotMine() {
        // given
        List<Card> cardList = new ArrayList<>();
        User cardOwner = new User();
        Long id = 1L;
        for (int i = 0; i < 2; i++) {
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 " + i, "할일 카드 내용 " + i, true, false);
            Card card = new Card(requestDto, cardOwner);
            card.setId((long) i + 1);
            cardList.add(card);
        }

        List<Card> myCardList = new ArrayList<>();
        User itsMe = new User();
        for (int i = 2; i < 4; i++) {
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 " + i, "할일 카드 내용 " + i, true, false);
            Card card = new Card(requestDto, itsMe);
            card.setId((long) i + 1);
            myCardList.add(card);
            cardList.add(card);
        }


        given(cardRepository.findAllByUser(itsMe)).willReturn(myCardList);

        given(cardRepository.findById(id)).willReturn(Optional.of(cardList.get(0)));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> cardService.checkCard(id, itsMe));

        // then
        assertThat("작성자만이 카드를 수정/삭제 할 수 있습니다.").isEqualTo(exception.getMessage());
    }
}