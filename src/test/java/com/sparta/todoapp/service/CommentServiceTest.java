package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    CardRepository cardRepository;

    @Test
    @DisplayName("댓글 작성 기능 성공 테스트")
    void createCommentTestSuccess(){
        User user = new User();
        CardRequestDto requestDto = new CardRequestDto("카드 제목", "카드 내용", true, false);
        Card card = new Card(requestDto, user);

        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 내용");
        Comment comment = new Comment(commentRequestDto.getContent(), user.getUsername(), user, card);

        given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));

        given(commentRepository.save(any())).willReturn(comment);

        CommentResponseDto responseDto = commentService.createComments(card.getId(), commentRequestDto, user);

        assertThat("댓글 내용").isEqualTo(responseDto.getContent());
    }
}