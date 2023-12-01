package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.*;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
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


    public CardResponseDto getCards(Long id, User user) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("존재하지 않는 카드입니다."));

        if(card.isVisible()){
            return new CardResponseDto(card);
        } else {

            var op = cardRepository.findByUserAndId(user, id);
            card = op.orElseThrow(
                    () -> new NullPointerException("해당하는 카드가 없습니다.")
            );
            return new CardResponseDto(card);
        }
    }

    public List<CardExceptCommentResponseDto> getAllCards() {

        List<Card> cardList = cardRepository.findAllByVisible(true);
        List<CardExceptCommentResponseDto> responseDtoList = new ArrayList<>();

        for (Card card : cardList) {
            responseDtoList.add(new CardExceptCommentResponseDto(card));
        }

        return responseDtoList;
    }

    public List<CardExceptCommentResponseDto> getTitleCards(CardTitleRequestDto requestDto, User user) {
        List<Card> cardList = cardRepository.findAllByTitle(requestDto.getTitle());
        List<CardExceptCommentResponseDto> responseDtoList = new ArrayList<>();

        if(cardList.isEmpty()){
            throw new NullPointerException("해당하는 제목의 게시물이 존재하지 않습니다.");
        }

        for (Card card : cardList) {
            if(card.isVisible()){
                responseDtoList.add(new CardExceptCommentResponseDto(card));
            } else {
                card = cardRepository.findByUserAndTitle(user, requestDto.getTitle());
                responseDtoList.add(new CardExceptCommentResponseDto(card));
            }
        }

        return responseDtoList;
    }

    @Transactional
    public CardExceptCommentResponseDto cardModify(Long id, CardModifyRequestDto requestDto, User user) {
        Card myCard = checkCard(id, user);
        myCard.updateCard(requestDto);
        return new CardExceptCommentResponseDto(myCard);
    }

    @Transactional
    public CardExceptCommentResponseDto completeTodo(Long id, CardCompleteRequestDto requestDto, User user) {
        Card myCard = checkCard(id, user);
        myCard.completeUpdate(requestDto);
        return new CardExceptCommentResponseDto(myCard);
    }

    public void deleteCard(Long id, User user) {
        Card myCard = checkCard(id, user);

        cardRepository.delete(myCard);
    }

    public Card checkCard(Long id, User user){

        Card myCard = new Card();

        List<Card> cardList = cardRepository.findAllByUser(user);
        if(cardList.isEmpty()){
            throw new NullPointerException("작성한 카드가 존재하지 않습니다.");
        }

        Card checkCard = cardRepository.findById(id).orElseThrow(()-> new NullPointerException("해당 id의 카드가 존재하지 않습니다."));

        for (Card card : cardList) {
            if (card.getId().equals(id)) {
                myCard = card;
                break;
            }
        }
        if(myCard.getId() == null){
            throw new IllegalArgumentException("작성자만이 카드를 수정/삭제 할 수 있습니다.");
        }

        return myCard;
    }
}
