package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.CardRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class StartTest {

    User user;

    @BeforeEach
    void setUp() {
        user = Mockito.mock(User.class);
        Mockito.when(user.getUsername()).thenReturn("Mayst");
        Mockito.when(user.getPassword()).thenReturn("1234");
    }

    @DisplayName("spy & mock의 차이점은 mock은 정한 객체의 메소드 안이 비어있지만 spy는 제대로 구성한다.")
    @Test
    void test2(){
        // given
        Card card = new Card();
        card.setTitle("그냥 테스트");

        Card mock = Mockito.mock(Card.class);
        Card spy = Mockito.spy(card);

        // when
        String result = card.getTitle();
        mock.setTitle("mock의 메소드는 비어있기에 작동하지 않습니다.");
        spy.setTitle("spy의 메소드는 구성되어 작동합니다.");

        // then
        System.out.println(card.getTitle());
        System.out.println(spy.getTitle());
        System.out.println(mock.getTitle());
        assertThat(result).isEqualTo("그냥 테스트");
    }


    @DisplayName("when - thenReturn 테스트")
    @Test
    void test3(){
        // given
        CardRequestDto requestDto = Mockito.mock(CardRequestDto.class);
        Mockito.when(requestDto.getTitle()).thenReturn("테스트 제목");
        Mockito.when(requestDto.getContent()).thenReturn("테스트 내용");
        Mockito.when(requestDto.isVisible()).thenReturn(true);
        Mockito.when(requestDto.isComplete()).thenReturn(false);

        // when
        Card card = new Card(requestDto, user);     // 위에 정해준 return 값들이 각각 card에 들어갑니다.

        // then
        System.out.println(card.getTitle());        // 정해준 Title의 값인 "테스트 제목"이 출력됩니다.
    }
}
