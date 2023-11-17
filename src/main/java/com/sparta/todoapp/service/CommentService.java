package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;


    public CommentResponseDto createComments(Long cardId, CommentRequestDto requestDto, User user) {
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new NullPointerException("해당 카드는 존재하지 않습니다."));
        Comment comment = commentRepository.save(new Comment(requestDto.getContent(),user.getUsername(), user, card));
        //addComment(cardId, comment.getId());
        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getComments(Long id) {
        List<Comment> commentList = commentRepository.findAllByCard_Id(id);
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            responseDtoList.add(new CommentResponseDto(comment));
        }

        return responseDtoList;
    }
}
