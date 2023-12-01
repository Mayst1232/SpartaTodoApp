package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentRepositoryTest extends RepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository; // comment 테이블이 user와 card를 fk로 가지고 있기 때문에 저장해주기 위한 Repository들 추가

    User user = new User("hwang","1234");
    Card card = Mockito.spy(new Card());

    @BeforeEach
    @DisplayName("코멘트 조회를 위하여 사전에 미리 데이터를 넣어주는 작업 및 commentRepository.save 동작 테스트")
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
                    .writer(user.getUsername() + i)
                    .user(user)
                    .card(card)
                    .build();

            Comment saveComment = commentRepository.save(comment);    // commentRepository.save 동작

            assertThat(saveComment.getContent()).isEqualTo(comment.getContent()); // save 동작 테스트
        }
    }

    @Test
    @DisplayName("하나의 할일 카드 안에 들어있는 모든 댓글 조회하는 기능 테스트 (없어도 빈칸으로 조회할 수 있다.)")
    void findAllByCard_IdTest(){
        // when
        List<Comment> commentList = commentRepository.findAllByCard_Id(1L); // commentRepository의 findAllByCard_Id(Long id)를 이용하여 전체 댓글 조회

        // then
        assertThat(commentList).hasSize(10);
        assertThat(commentList).map(Comment::getWriter).contains(user.getUsername()+1, user.getUsername()+2);
    }

    @Test
    @DisplayName("유저가 작성한 모든 댓글 조회하는 기능 테스트 (없어도 빈칸으로 조회할 수 있다.)")
    void findAllByUserTest(){
        // when
        List<Comment> commentList = commentRepository.findAllByUser(user); // commentRepository의 findAllByUser(user)를 이용하여 전체 댓글 조회

        // then
        assertThat(commentList).hasSize(10);    // commentList가 10개의 데이터를 가지고 있는지 체크
        assertThat(commentList).map(Comment::getWriter).contains(user.getUsername()+1, user.getUsername()+2, user.getUsername()+3);
        // 이 commentList가 작성자로 user.getUsername()+1, user.getUsername()+2, user.getUsername()+3을 가지고 있는지 체크
    }
}