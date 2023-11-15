package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    @PostMapping("/cards")
    public CardResponseDto createCard(@RequestBody CardRequestDto requestDto){
        return cardService.createCard(requestDto);
    }
}
