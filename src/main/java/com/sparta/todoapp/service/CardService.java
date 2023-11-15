package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    public CardResponseDto createCard(CardRequestDto requestDto) {
        Card card = cardRepository.save(new Card(requestDto));
        return new CardResponseDto(card);
    }
}
