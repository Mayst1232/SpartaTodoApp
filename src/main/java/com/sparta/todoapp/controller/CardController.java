package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.*;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
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
    public ResponseEntity<?> cardModify(@PathVariable Long id,
                                        @RequestBody CardModifyRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            CardExceptCommentResponseDto cardExceptCommentResponseDto = cardService.cardModify(id, requestDto, userDetails.getUser());
            return ResponseEntity.ok(cardExceptCommentResponseDto);
        } catch (NullPointerException | IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(new StatusResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PatchMapping("/cards/complete/{id}")
    public ResponseEntity<?> completeTodo(@PathVariable Long id,
                                          @RequestBody CardCompleteRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            CardResponseDto cardResponseDto = cardService.completeTodo(id, requestDto, userDetails.getUser());
            return ResponseEntity.ok(cardResponseDto);
        } catch (NullPointerException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new StatusResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            cardService.deleteCard(id, userDetails.getUser());
            String message = "삭제 성공!";
            return ResponseEntity.ok().body(new StatusResponseDto(message, HttpStatus.OK.value()));
        } catch (NullPointerException | IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(new StatusResponseDto(ex.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
    }
}
