package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CardCompleteRequestDto;
import com.sparta.todoapp.dto.CardExceptCommentResponseDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    @PostMapping("/cards")
    public CardResponseDto createCard(@RequestBody CardRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return cardService.createCard(requestDto, userDetails.getUser());
    }

    @GetMapping("/cards/{id}")
    public CardResponseDto getCards(@PathVariable Long id){
        return cardService.getCards(id);
//        return cardService.getCards(id, userDetails.getUser());
    }

    @GetMapping("/cards")
    public List<CardExceptCommentResponseDto> getAllCards(){
        return cardService.getAllCards();
    }



    @PutMapping("/cards/complete/{id}")
    public CardResponseDto completeTodo(@PathVariable Long id,
                                        @RequestBody CardCompleteRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return cardService.completeTodo(id, requestDto, userDetails.getUser());
    }
}
