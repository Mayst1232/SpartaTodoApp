package com.sparta.todoapp.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StatusResponseDtoTest {

    @Test
    @DisplayName("서버에서 클라이언트로 서버의 상태를 전달해주는 기능 테스트")
    void statusResponseDtoConstructorTest(){
        StatusResponseDto responseDto = new StatusResponseDto("서버가 정상입니다.", HttpStatus.OK.value());

        assertThat(responseDto.getMessage()).isEqualTo("서버가 정상입니다.");
        assertThat(responseDto.getStatus()).isEqualTo(200);
    }
}