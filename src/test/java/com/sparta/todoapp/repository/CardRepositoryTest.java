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

    void saveSampleCard(CardRequestDto requestDto, User user){
        Card card = Card.builder()
                .requestDto(requestDto)
                .user(user)
                .build();

        cardRepository.save(card);
    }


}