package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CardExceptCommentResponseDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.dto.CardTitleRequestDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
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
import static org.mockito.BDDMockito.given;


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
        CardRequestDto requestDto = new CardRequestDto("찾은 카드 제목", "찾은 카드 내용",false, false);

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
    void getCardsFailTest(){
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
        List<Card> cardList = new ArrayList<>();
        User user = new User("Hwang", "1234");

        for(int i = 0; i < 4; i++){
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 " + i, "할일 카드 내용 " +i, i % 2 == 0, false);
            Card card = new Card(requestDto, user);
            if(card.isVisible()){
                cardList.add(card);
            }
        }

        given(cardRepository.findAllByVisible(true)).willReturn(cardList);

        List<CardExceptCommentResponseDto> result = cardService.getAllCards();

        assertThat(result).hasSize(2);
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