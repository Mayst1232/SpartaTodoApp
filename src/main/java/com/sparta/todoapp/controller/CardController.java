package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CardCompleteRequestDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    @PostMapping("/cards")
    public CardResponseDto createCard(@RequestBody CardRequestDto requestDto){
        return cardService.createCard(requestDto);
    }

    @PutMapping("/cards/{id}")
    public CardResponseDto completeTodo(@PathVariable Long id, @RequestBody CardCompleteRequestDto requestDto){
        return cardService.completeTodo(id, requestDto);
    }

    @GetMapping("/cards")
    public List<CardResponseDto> getAllCards(){
        return cardService.getAllCards();
    }
}
