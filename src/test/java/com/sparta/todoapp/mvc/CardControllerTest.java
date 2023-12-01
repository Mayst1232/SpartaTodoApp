package com.sparta.todoapp.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.config.WebSecurityConfig;
import com.sparta.todoapp.controller.CardController;
import com.sparta.todoapp.dto.CardExceptCommentResponseDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.service.CardService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {CardController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CardService cardService;

    @Test
    void createCard(){
    }

    @Test
    void getCards() {
    }

    @Test
    void getAllCards() {
    }

    @Test
    void getTitleCards() {
    }

    @Test
    void cardModify() {
    }

    @Test
    void completeTodo() {
    }

    @Test
    void deleteCard() {
    }
}