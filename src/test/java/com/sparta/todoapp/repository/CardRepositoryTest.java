package com.sparta.todoapp.repository;

import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardRepositoryTest extends RepositoryTest {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    @DisplayName("모든 테스트가 시작 전에 할일 카드를 작성할 공통의 User를 갖고 있게 하기 위한 Setup")
    void setUp() {
        user = User.builder()
                .username("Hwang")
                .password("1234")
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("User와 requestDto를 전달받은 Card Entity를 DB에 저장하는 기능의 테스트")
    void saveCard(){
        // givne
        CardRequestDto requestDto = new CardRequestDto("카드 제목", "카드 내용", true, false);

        Card card = Card.builder()
                .requestDto(requestDto)
                .user(user)
                .build();

        // when
        Card saveCard = cardRepository.save(card);

        // then
        assertThat(saveCard.getTitle()).isEqualTo(card.getTitle());
        assertThat(saveCard.getContent()).isEqualTo(card.getContent());
        assertThat(saveCard.isVisible()).isEqualTo(card.isVisible());
        assertThat(saveCard.isComplete()).isEqualTo(card.isComplete());
    }


    @Test
    @DisplayName("할일 카드 중 user가 작성한 할일 카드를 찾아오는 기능 성공 테스트")
    void findByUserAndIdSuccessTest() {
        // given
        CardRequestDto requestDto = new CardRequestDto("할일 카드 제목", "할일 카드 내용", true, false);
        Card card = Card.builder()
                .requestDto(requestDto)
                .user(user)
                .build();

        cardRepository.save(card);

        card = cardRepository.findByUser(user).orElseThrow(
                () -> new NullPointerException("해당하는 할일 카드가 없습니다.")
        );

        // then
        assertThat(card.getUser().getId()).isEqualTo(user.getId());
        assertThat(card.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(card.getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    @DisplayName("할일 카드 중 user가 작성한 할일 카드를 찾아오는 기능 실패 테스트")
    void findByUserAndIdFailTest() {
        // given
        CardRequestDto requestDto = new CardRequestDto("할일 카드 제목", "할일 카드 내용", true, false);
        Card card = Card.builder()
                .requestDto(requestDto)
                .user(user)
                .build();

        cardRepository.save(card);

        User findUser = new User();
        findUser.setId(3L);

        // when
        Exception exception = assertThrows(NullPointerException.class,
                () -> cardRepository.findByUserAndId(findUser,1L).orElseThrow(
                        () -> new NullPointerException("해당하는 할일 카드가 없습니다.")
                )
        );

        //then
        assertThat("해당하는 할일 카드가 없습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("user가 작성한 할일 카드 중 찾는 제목과 일치하는 카드 찾아오는 기능 테스트")
    void findByUserAndTitleTest() {
        //given
        CardRequestDto requestDto = new CardRequestDto("할일 카드 제목", "할일 카드 내용", true, false);
        Card card = Card.builder()
                .requestDto(requestDto)
                .user(user)
                .build();

        cardRepository.save(card);

        // when
        card = cardRepository.findByUserAndTitle(user, requestDto.getTitle());

        // then
        assertThat(card.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(card.getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    @DisplayName("user가 작성한 모든 카드를 조회하는 기능 테스트")
    void findAllByUserTest() {
        // given
        for(int i = 1; i < 6; i++){
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 " + i, "할일 카드 내용" + i, true, false);
            Card card = Card.builder()
                    .requestDto(requestDto)
                    .user(user)
                    .build();

            cardRepository.save(card);
        }

        // when
        List<Card> cardList = cardRepository.findAllByUser(user);

        // then
        assertThat(cardList).hasSize(5);
        assertThat(cardList).map(Card::getTitle).contains("할일 카드 제목 1", "할일 카드 제목 2", "할일 카드 제목 3", "할일 카드 제목 4", "할일 카드 제목 5");
    }

    @Test
    @DisplayName("보여지는 기능이 작동하고 있는 모든 카드를 조회하는 기능 테스트")
    void findAllByVisibleTest() {
        // given
        for(int i = 1; i < 6; i++){
            CardRequestDto requestDto = new CardRequestDto("할일 카드 제목 " + i, "할일 카드 내용 " + i, i % 2 == 0, false);
            Card card = Card.builder()
                    .requestDto(requestDto)
                    .user(user)
                    .build();

            cardRepository.save(card);
        }

        // when
        List<Card> cardList = cardRepository.findAllByVisible(true);

        // then
        assertThat(cardList).hasSize(2);
        assertThat(cardList).map(Card::getTitle).contains("할일 카드 제목 2", "할일 카드 제목 4");
        // extract << 다음에 사용해 볼 예정
    }

}