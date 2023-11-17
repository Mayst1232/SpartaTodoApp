package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.*;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    public CardExceptCommentResponseDto createCard(CardRequestDto requestDto, User user) {
        Card card = cardRepository.save(new Card(requestDto, user));
        return new CardExceptCommentResponseDto(card);
    }


    public CardResponseDto getCards(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("존재하지 않는 카드입니다."));

        return new CardResponseDto(card);

//        List<Card> cardList = cardRepository.findAllByUser(user);
//        List<CardResponseDto> responseDtoList = new ArrayList<>();
//
//        for (Card card : cardList) {
//            responseDtoList.add(new CardResponseDto(card));
//        }
//        return responseDtoList;
    }

    public List<CardExceptCommentResponseDto> getAllCards() {

        List<Card> cardList = cardRepository.findAll();
        List<CardExceptCommentResponseDto> responseDtoList = new ArrayList<>();

        for (Card card : cardList) {
            responseDtoList.add(new CardExceptCommentResponseDto(card));
        }

        return responseDtoList;
    }

    @Transactional
    public CardExceptCommentResponseDto cardModify(Long id, CardModifyRequestDto requestDto, User user) {
        Card card = cardRepository.findByUserAndId(user, id).orElseThrow(
                () -> new NullPointerException("해당하는 카드가 없습니다.")
        );

        card.updateCard(requestDto);

        return new CardExceptCommentResponseDto(card);
    }

    @Transactional
    public CardResponseDto completeTodo(Long id, CardCompleteRequestDto requestDto, User user) {

        Card card = cardRepository.findByUserAndId(user, id).orElseThrow(
                () -> new NullPointerException("해당하는 카드가 없습니다.")
        );

        card.completeUpdate(requestDto);
        return new CardResponseDto(card);
    }

    public void deleteCard(Long id, User user) {

        Card card = cardRepository.findByUserAndId(user, id).orElseThrow(
                () -> new NullPointerException("해당하는 카드가 없습니다.")
        );

        cardRepository.delete(card);
    }

//    public CardResponseDto completeTodo(Long id, CardCompleteRequestDto requestDto, User user) {
//
//        Card card = cardRepository.findById(id).orElseThrow(
//                () -> new NullPointerException("해당하는 카드가 없습니다.")
//        );
//
//        card.completeUpdate(requestDto);
//        return new CardResponseDto(card);
//    }
}
