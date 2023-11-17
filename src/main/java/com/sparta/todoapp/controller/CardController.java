package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.*;
import com.sparta.todoapp.entity.User;
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
    public CardExceptCommentResponseDto createCard(@RequestBody CardRequestDto requestDto,
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

    @PatchMapping("/cards/{id}")
    public CardExceptCommentResponseDto cardModify(@PathVariable Long id,
                                            @RequestBody CardModifyRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return cardService.cardModify(id, requestDto, userDetails.getUser());
    }

    @PatchMapping("/cards/complete/{id}")
    public CardResponseDto completeTodo(@PathVariable Long id,
                                        @RequestBody CardCompleteRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return cardService.completeTodo(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/cards/{id}")
    public void deleteCard(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        cardService.deleteCard(id, userDetails.getUser());
    }
}
