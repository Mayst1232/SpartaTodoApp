package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.dto.StatusResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/cards/{cardId}/comments")
    public CommentResponseDto createComments(@PathVariable Long cardId,
                                             @RequestBody CommentRequestDto requestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComments(cardId, requestDto, userDetails.getUser());
    }

    @GetMapping("/cards/{cardId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long cardId){
        return commentService.getComments(cardId);
    }


    @PatchMapping("/cards/comments/{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable Long commentId,
                                           @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            CommentResponseDto commentResponseDto = commentService.modifyComment(commentId,requestDto, userDetails.getUser());
            return ResponseEntity.ok(commentResponseDto);
        } catch (NullPointerException | IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(new StatusResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

    }

    @DeleteMapping("/cards/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            commentService.deleteComment(commentId, userDetails.getUser());
            String message = "삭제 성공!";
            return ResponseEntity.ok().body(new StatusResponseDto(message, HttpStatus.OK.value()));
        } catch (NullPointerException | IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(new StatusResponseDto(ex.getMessage(),HttpStatus.BAD_REQUEST.value()));
        }
    }
}
