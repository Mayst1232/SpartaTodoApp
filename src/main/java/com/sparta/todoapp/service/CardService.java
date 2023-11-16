package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CardCompleteRequestDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    public CardResponseDto createCard(CardRequestDto requestDto) {
        Card card = cardRepository.save(new Card(requestDto));
        return new CardResponseDto(card);
    }

    public CardResponseDto completeTodo(Long id, CardCompleteRequestDto requestDto) {
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당하는 카드가 없습니다.")
        );

        card.completeUpdate(requestDto);

        return new CardResponseDto(card);
    }

    public List<CardResponseDto> getAllCards() {
        List<Card> cardList = cardRepository.findAll();
        List<CardResponseDto> responseDtoList = new ArrayList<>();

        for (Card card : cardList) {
            responseDtoList.add(new CardResponseDto(card));
        }

        return responseDtoList;
    }
}
