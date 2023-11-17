package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CommentService;
import lombok.RequiredArgsConstructor;
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

//    @PatchMapping("/cards/{cardId}/comments")
//    public CommentResponseDto modifyComment(@PathVariable Long cardId,
//                                            @RequestBody CommentRequestDto requestDto,
//                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return commentService.modifyComment(cardId, requestDto, userDetails.getUser());
//    }
}
