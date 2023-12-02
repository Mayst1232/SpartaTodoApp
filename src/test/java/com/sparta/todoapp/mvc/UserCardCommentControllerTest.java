package com.sparta.todoapp.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.config.WebSecurityConfig;
import com.sparta.todoapp.controller.CardController;
import com.sparta.todoapp.controller.CommentController;
import com.sparta.todoapp.controller.UserController;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CardService;
import com.sparta.todoapp.service.CommentService;
import com.sparta.todoapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {UserController.class, CardController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@ActiveProfiles("test")
class UserCardCommentControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    CardService cardService;

    @MockBean
    CommentService commentService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
    }

    private void mockUserSetup() {
        String username = "hwang1234";
        String password = "qwer1234";
        User testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails,null, null);
    }

    @Test
    @DisplayName("회원가입 요청 기능 테스트")
    void signupSuccessTest() throws Exception {
        SignupRequestDto requestDto = new SignupRequestDto("hwang1234","qwer1234");

        String json = objectMapper.writeValueAsString(requestDto);

        System.out.println(json);

        mvc.perform(post("/api/user/signup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("회원가입 요청 시 username 혹은 password 필드가 비어있을 때 예외처리 테스트")
    void signupFailTestFieldIsEmpty() throws Exception {
        SignupRequestDto requestDto = new SignupRequestDto("hwang1234","");
        String expectByMessage = "$.[?(@.message == '%s')]";
        String json = objectMapper.writeValueAsString(requestDto);

        mvc.perform(post("/api/user/signup")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(expectByMessage,"필드가 비었습니다.").exists());
    }

    @Test
    @DisplayName("회원가입 요청 시 중복된 username으로 가입 시도할 때 예외처리 테스트")
    void signupFailTestDuplicateUser() throws Exception {
        SignupRequestDto requestDto = new SignupRequestDto("hwang1234","qwer1234");
        String expectByStatus = "$.[?(@.status == '%d')]";
        String json = objectMapper.writeValueAsString(requestDto);

        mvc.perform(post("/api/user/signup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        doThrow(IllegalArgumentException.class).when(userService).signup(any());
        mvc.perform(post("/api/user/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(jsonPath(expectByStatus, 409).exists());
    }

    @Test
    @DisplayName("회원가입 요청 시 유효하지 않은 username / password로 가입 시도할 때 예외처리 테스트")
    void signupFailTestNotValidData() throws Exception {
        SignupRequestDto requestDto = new SignupRequestDto("Lee", "qwer1234");
        String json = objectMapper.writeValueAsString(requestDto);

        mvc.perform(post("/api/user/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        requestDto = new SignupRequestDto("hwang", "qwer");
        json = objectMapper.writeValueAsString(requestDto);

        mvc.perform(post("/api/user/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("새로운 카드 생성하는 기능 테스트")
    void createCardSuccessTest() throws Exception {
        this.mockUserSetup();
        String title = "카드 제목";
        String content = "카드 내용";
        boolean visible = true;
        boolean complete = false;
        CardRequestDto requestDto = new CardRequestDto(title, content, visible, complete);

        String cardInfo = objectMapper.writeValueAsString(requestDto);

        mvc.perform(post("/api/cards")
                .content(cardInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("카드 단건 조회 성공 테스트")
    void getCardsSuccessTest() throws Exception {
        this.mockUserSetup();

        mvc.perform(get("/api/cards/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ) .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카드 단건 조회 실패 테스트")
    void getCardsFailTestCardIsNotExist() throws Exception {
        this.mockUserSetup();

        given(cardService.getCards(any(),any())).willThrow(NullPointerException.class);
        mvc.perform(get("/api/cards/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ) .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("모든 카드 조회 메소드")
    void getAllCardsTest() throws Exception {
        mvc.perform(get("/api/cards"))
                .andExpect(status().isOk());
    }

}