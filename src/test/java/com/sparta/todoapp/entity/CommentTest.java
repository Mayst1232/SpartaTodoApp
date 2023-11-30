package com.sparta.todoapp.entity;


import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CommentRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;

class CommentTest {

    @Test
    @DisplayName("Comment에 전달된 content, writer, user, card가 잘 전달 되어야 합니다. ")
    void testCommentConstructor(){
        // given
        Card card = new Card();
        User user = new User();

        card.setId(1L);
        user.setUsername("Mayst");
        user.setId(1L);

        String content = "댓글내용";

        // when
        Comment comment = Mockito.spy(Comment.builder()
                .content(content)
                .writer(user.getUsername())
                .user(user)
                .card(card).build());

        // then
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getWriter()).isEqualTo(user.getUsername());
        assertThat(comment.getUser()).isEqualTo(user);
        assertThat(comment.getCard()).isEqualTo(card);
    }

    @Test
    @DisplayName("Comment의 내용을 바꾸는 기능 테스트")
    void updateComment() {
        // given
        CommentRequestDto requestDto = new CommentRequestDto("변경 댓글 내용입니다.");

        Comment comment = new Comment();

        // when
        comment.update(requestDto);

        // then
        assertThat(comment.getContent()).isEqualTo(requestDto.getContent());
    }
}