package com.sparta.todoapp.repository;

import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CardRepositoryTest extends RepositoryTest {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("Hwang")
                .password("1234")
                .build();
        user.setId(1L);
        userRepository.save(user);
    }

    @Test
    void saveCard(){
        CardRequestDto requestDto = new CardRequestDto("카드 제목", "카드 내용", true, false);

        Card card = Card.builder()
                .requestDto(requestDto)
                .user(user)
                .build();

        Card saveCard = cardRepository.save(card);

        assertThat(saveCard.getTitle()).isEqualTo(card.getTitle());
        assertThat(saveCard.getContent()).isEqualTo(card.getContent());
        assertThat(saveCard.isVisible()).isEqualTo(card.isVisible());
        assertThat(saveCard.isComplete()).isEqualTo(card.isComplete());
    }

    void saveSampleCard(Long id,CardRequestDto requestDto, User user){
        Card card = Card.builder()
                .requestDto(requestDto)
                .user(user)
                .build();

        card.setId(id);

        cardRepository.save(card);
    }

    @Test
    void findByUserAndIdSuccessTest() {
        CardRequestDto requestDto = new CardRequestDto("할일 카드 제목", "할일 카드 내용", true, false);
        saveSampleCard(1L, requestDto, user);

        Card card = cardRepository.findByUserAndId(user,1L).orElseThrow(
                () -> new NullPointerException("해당하는 할일 카드가 없습니다.")
        );

        assertThat(card.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(card.getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    void findByUserAndIdFailTest() {

    }

    @Test
    void findByUserAndTitleTest() {

    }

    @Test
    void findAllByUserTest() {

    }

    @Test
    void findAllByVisibleTest() {

    }

}