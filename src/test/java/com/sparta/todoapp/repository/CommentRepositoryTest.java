package com.sparta.todoapp.repository;

import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CommentRepositoryTest extends RepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository;

    User user = new User("hwang","1234");
    Card card = Mockito.spy(new Card());

    @BeforeEach
    void setUp() {
        // given
        user.setId(1L);
        userRepository.save(user);

        card.setId(1L);
        card.setTitle("제목");
        card.setContent("내용");
        card.setComplete(false);
        card.setVisible(true);
        card.setUser(user);
        cardRepository.save(card);

        for(int i = 0; i < 10; i++){
            Comment comment = Comment.builder()
                    .content("댓글 내용" + i)
                    .writer(user.getUsername())
                    .user(user)
                    .card(card)
                    .build();

            commentRepository.save(comment);
        }
    }

    @Test
    void findAllByCard_IdTest(){
        // when
        List<Comment> commentList = commentRepository.findAllByCard_Id(1L);

        // then
        assertThat(commentList).hasSize(10);
        assertThat(commentList).map(Comment::getWriter).contains(user.getUsername());
    }
}