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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void createCommentTestSuccess() {
        // given
        User user = new User();
        CardRequestDto requestDto = new CardRequestDto("카드 제목", "카드 내용", true, false);
        Card card = new Card(requestDto, user);

        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 내용");
        Comment comment = new Comment(commentRequestDto.getContent(), user.getUsername(), user, card);

        given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));

        given(commentRepository.save(any())).willReturn(comment);

        // when
        CommentResponseDto responseDto = commentService.createComments(card.getId(), commentRequestDto, user);

        // then
        assertThat("댓글 내용").isEqualTo(responseDto.getContent());
    }

    @Test
    @DisplayName("댓글 작성 기능 실패 테스트")
    void createCommentTestFailCardIsNotExist() {
        // given
        User user = new User();
        Card card = new Card();
        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 내용");

        given(cardRepository.findById(card.getId())).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(NullPointerException.class,
                () -> commentService.createComments(card.getId(), commentRequestDto, user)
        );

        // then
        assertThat("해당 카드는 존재하지 않습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("선택한 카드 안에 있는 전체 댓글 조회 기능 테스트")
    void getCommentsTest() {
        // given
        Card card = new Card();
        User user = new User();
        user.setUsername("hwang");
        List<Comment> commentList = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            CommentRequestDto requestDto = new CommentRequestDto("댓글 내용 " + i);

            Comment comment = new Comment(requestDto.getContent(), user.getUsername(), user, card);
            commentList.add(comment);
        }

        given(commentRepository.findAllByCardId(any())).willReturn(commentList);

        // when
        List<CommentResponseDto> commentResponseDtoList = commentService.getComments(card.getId());

        // then
        assertThat(commentResponseDtoList).hasSize(5);
        assertThat(commentResponseDtoList.get(0).getContent()).isEqualTo("댓글 내용 0");
    }

    @Test
    @DisplayName("선택한 댓글 수정 기능 성공 테스트")
    void modifyCommentTestSuccess() {
        // given
        List<Comment> commentList = new ArrayList<>();
        User user = new User();
        user.setUsername("hwang");
        Card card = new Card();
        card.setId(1L);
        createComment(commentList, user, card);
        checkComment(commentList, user);
        CommentRequestDto requestDto = new CommentRequestDto("변경할 댓글 내용");
        Comment comment = commentList.get(0);

        // when
        CommentResponseDto responseDto = commentService.modifyComment(comment.getId(), requestDto, user);

        // then
        assertThat("변경할 댓글 내용").isEqualTo(responseDto.getContent());
    }

    @Test
    @DisplayName("선택한 댓글 삭제 기능 성공 테스트")
    void deleteCommentTestSuccess() {
        // given
        List<Comment> commentList = new ArrayList<>();
        User user = new User();
        user.setUsername("hwang");
        Card card = new Card();
        card.setId(1L);

        createComment(commentList, user, card);
        checkComment(commentList, user);

        Comment deleteComment = commentList.get(0);

        // when
        commentService.deleteComment(deleteComment.getId(), user);

        // then
        verify(commentRepository, times(1)).delete(any());
    }

    void createComment(List<Comment> commentList, User user, Card card) {
        for(int i = 0; i < 5; i++) {
            CommentRequestDto requestDto = new CommentRequestDto("댓글 내용 " + i);
            Comment comment = new Comment(requestDto.getContent(), user.getUsername(), user, card);
            comment.setId((long) i+1);
            commentList.add(comment);
        }
    }

    void checkComment(List<Comment> commentList, User user) {
        Comment checkComment = commentList.get(0);
        given(commentRepository.findAllByUser(user)).willReturn(commentList);
        given(commentRepository.findById(checkComment.getId())).willReturn(Optional.of(checkComment));

    }

    @Test
    @DisplayName("유저의 코멘트만 수정 / 삭제 할 수 있기 때문에 자신의 코멘트를 찾는 기능 성공 테스트")
    void checkCommentTestSuccess() {
        // given
        Comment myComment;
        List<Comment> commentList = new ArrayList<>();
        Card card = new Card();
        card.setId(1L);
        User user = new User();
        user.setUsername("hwang");

        for(int i = 0; i < 5; i++){
            CommentRequestDto requestDto = new CommentRequestDto("댓글 내용 " + i);

            Comment comment = new Comment(requestDto.getContent(), user.getUsername(), user, card);
            comment.setId((long) i+1);
            commentList.add(comment);
        }

        Comment checkComment = commentList.get(0);

        given(commentRepository.findAllByUser(user)).willReturn(commentList);
        given(commentRepository.findById(checkComment.getId())).willReturn(Optional.of(checkComment));

        // when
        myComment = commentService.checkComment(checkComment.getId(), user);

        // then
        assertThat("댓글 내용 0").isEqualTo(myComment.getContent());
    }

    @Test
    @DisplayName("user가 작성한 댓글이 한 개도 없을 경우 테스트 실패 예외처리")
    void checkCommentFailTestUserNotWriteComment() {
        // given
        Comment myComment;
        List<Comment> commentList = new ArrayList<>();
        Card card = new Card();
        card.setId(1L);
        User user = new User();
        user.setUsername("hwang");

        for(int i = 0; i < 5; i++){
            CommentRequestDto requestDto = new CommentRequestDto("댓글 내용 " + i);

            Comment comment = new Comment(requestDto.getContent(), user.getUsername(), user, card);
            comment.setId((long) i+1);
            commentList.add(comment);
        }
        Comment findComment = commentList.get(0);

        User findUser = new User();
        user.setUsername("finder");

        given(commentRepository.findAllByUser(findUser)).willReturn(new ArrayList<>());

        Exception exception = assertThrows(NullPointerException.class,
                () -> commentService.checkComment(findComment.getId(), findUser)
        );

        assertThat("작성한 댓글이 존재하지 않습니다.").isEqualTo(exception.getMessage());
    }
}