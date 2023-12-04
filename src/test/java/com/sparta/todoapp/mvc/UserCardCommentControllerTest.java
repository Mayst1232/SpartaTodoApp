package com.sparta.todoapp.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.config.WebSecurityConfig;
import com.sparta.todoapp.controller.CardController;
import com.sparta.todoapp.controller.CommentController;
import com.sparta.todoapp.controller.UserController;
import com.sparta.todoapp.dto.*;
import com.sparta.todoapp.entity.Comment;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        // given
        SignupRequestDto requestDto = new SignupRequestDto("hwang1234","qwer1234");

        String json = objectMapper.writeValueAsString(requestDto);

        System.out.println(json);

        // when - then
        mvc.perform(post("/api/user/signup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("회원가입 요청 시 username 혹은 password 필드가 비어있을 때 예외처리 테스트")
    void signupFailTestFieldIsEmpty() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto("hwang1234","");
        String expectByMessage = "$.[?(@.message == '%s')]";
        String json = objectMapper.writeValueAsString(requestDto);

        // when - then
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
        // givne
        SignupRequestDto requestDto = new SignupRequestDto("hwang1234","qwer1234");
        String expectByStatus = "$.[?(@.status == '%d')]";
        String json = objectMapper.writeValueAsString(requestDto);

        mvc.perform(post("/api/user/signup")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        doThrow(IllegalArgumentException.class).when(userService).signup(any());

        // when - then
        mvc.perform(post("/api/user/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath(expectByStatus, 409).exists());
    }

    @Test
    @DisplayName("회원가입 요청 시 유효하지 않은 username / password로 가입 시도할 때 예외처리 테스트")
    void signupFailTestNotValidData() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto("Lee", "qwer1234");
        String json = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/api/user/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        // given
        requestDto = new SignupRequestDto("hwang", "qwer");
        json = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/api/user/signup")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("새로운 카드 생성하는 기능 테스트")
    void createCardSuccessTest() throws Exception {
        // givne
        this.mockUserSetup();
        String title = "카드 제목";
        String content = "카드 내용";
        boolean visible = true;
        boolean complete = false;
        CardRequestDto requestDto = new CardRequestDto(title, content, visible, complete);

        String cardInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/api/cards")
                .content(cardInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("카드 단건 조회 기능 성공 테스트")
    void getCardsSuccessTest() throws Exception {
        // given
        this.mockUserSetup();

        // when - then
        mvc.perform(get("/api/cards/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ) .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카드 단건 조회 기능 실패 테스트")
    void getCardsFailTestCardIsNotExist() throws Exception {
        // given
        this.mockUserSetup();

        given(cardService.getCards(any(),any())).willThrow(NullPointerException.class);

        // when - then
        mvc.perform(get("/api/cards/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ) .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("모든 카드 조회 기능 테스트")
    void getAllCardsTest() throws Exception {
        // when - then
        mvc.perform(get("/api/cards"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("제목에 따라 해당하는 카드를 조회해오는 기능 테스트")
    void getTitleCardsSuccessTest() throws Exception {
        // given
        this.mockUserSetup();
        CardTitleRequestDto requestDto = new CardTitleRequestDto("찾는 제목");

        String json = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(get("/api/cards/title")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("제목에 따라 해당하는 카드를 조회해오는 기능 실패 테스트")
    void getTitleCardsFailTestCardIsNotExist() throws Exception {
        // given
        this.mockUserSetup();
        CardTitleRequestDto requestDto = new CardTitleRequestDto("찾는 제목");

        String json = objectMapper.writeValueAsString(requestDto);

        given(cardService.getTitleCards(any(),any())).willThrow(NullPointerException.class);

        // when - then
        mvc.perform(get("/api/cards/title")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("게시물 수정하는 기능 성공 테스트")
    void cardModifySuccessTest() throws Exception {
        // given
        this.mockUserSetup();
        CardModifyRequestDto requestDto = new CardModifyRequestDto("수정 제목", "수정 내용");

        String json = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(patch("/api/cards/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 수정하는 기능 카드가 존재하지 않아 실패 테스트")
    void cardModifyFailTestCardIsNotExist() throws Exception {
        // given
        this.mockUserSetup();
        CardModifyRequestDto requestDto = new CardModifyRequestDto("수정 제목", "수정 내용");
        String json = objectMapper.writeValueAsString(requestDto);

        given(cardService.cardModify(any(),any(),any())).willThrow(NullPointerException.class);

        // when - then
        mvc.perform(patch("/api/cards/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시물 수정하는 기능 유저가 존재하지 않는 실패 테스트")
    void cardModifyFailTestCardIsNotMine() throws Exception {
        // given
        this.mockUserSetup();
        CardModifyRequestDto requestDto = new CardModifyRequestDto("수정 제목", "수정 내용");
        String json = objectMapper.writeValueAsString(requestDto);

        given(cardService.cardModify(any(),any(),any())).willThrow(IllegalArgumentException.class);

        // when - then
        mvc.perform(patch("/api/cards/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("할일 카드를 완료 했을 때 완료시키는 기능 성공 테스트")
    void completeTodoSuccessTest() throws Exception {
        // given
        this.mockUserSetup();
        CardCompleteRequestDto requestDto = new CardCompleteRequestDto(true);

        String json = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(patch("/api/cards/complete/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 카드의 완료 여부를 수정할 경우 기능 실패 테스트")
    void completeTodoFailTestCardIsNotExsit() throws Exception {
        // given
        this.mockUserSetup();
        CardCompleteRequestDto requestDto = new CardCompleteRequestDto(true);

        String json = objectMapper.writeValueAsString(requestDto);

        given(cardService.completeTodo(any(),any(),any())).willThrow(NullPointerException.class);

        // when - then
        mvc.perform(patch("/api/cards/complete/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 카드의 완료 여부를 수정할 경우 기능 실패 테스트")
    void completeTodoFailTestCardIsNotMine() throws Exception {
        // given
        this.mockUserSetup();
        CardCompleteRequestDto requestDto = new CardCompleteRequestDto(true);

        String json = objectMapper.writeValueAsString(requestDto);

        given(cardService.completeTodo(any(),any(),any())).willThrow(IllegalArgumentException.class);

        // when - then
        mvc.perform(patch("/api/cards/complete/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("할일 카드를 삭제하는 기능 테스트 성공")
    void deleteCardTestSuccess() throws Exception {
        // given
        this.mockUserSetup();

        // when - then
        mvc.perform(delete("/api/cards/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 할일 카드 삭제 시도 시 테스트 실패")
    void deleteCardTestFailCardIsNotExist() throws Exception {
        // given
        this.mockUserSetup();

        doThrow(NullPointerException.class).when(cardService).deleteCard(any(), any());

        // when - then
        mvc.perform(delete("/api/cards/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("자신의 카드가 아닌 카드를 삭제 할 때 삭제 기능 테스트 실패")
    void deleteCardTestFailCardIsNotMine() throws Exception {
        // given
        this.mockUserSetup();

        doThrow(IllegalArgumentException.class).when(cardService).deleteCard(any(), any());

        // when - then
        mvc.perform(delete("/api/cards/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("조회하고 있는 카드에 댓글 작성 기능 테스트")
    void createCommentTestSuccess() throws Exception {
        // givne
        this.mockUserSetup();
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용");

        String commentInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/api/cards/1/comments")
                        .content(commentInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("조회하고 있는 카드에 댓글 조회 기능 테스트")
    void getCommnentsTestSuccess() throws Exception {
        mvc.perform(get("/api/cards/1/comments"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("조회하고 있는 카드 댓글 수정 기능 성공 테스트")
    void modifyCommentTestSuccess() throws Exception {
        this.mockUserSetup();

        CommentRequestDto requestDto = new CommentRequestDto("변경 댓글 내용");
        String commentInfo = objectMapper.writeValueAsString(requestDto);

        mvc.perform(patch("/api/cards/comments/1")
                .content(commentInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저가 작성한 댓글이 존재하지 않을 때 수정 기능 실패 테스트 / 수정하려는 댓글이 존재하지 않을 때 수정  기능 실패 테스트")
    void modifyCommentTestFailCommentIsNotExist() throws Exception {
        this.mockUserSetup();

        CommentRequestDto requestDto = new CommentRequestDto("변경 댓글 내용");
        String commentInfo = objectMapper.writeValueAsString(requestDto);

        given(commentService.modifyComment(any(),any(),any())).willThrow(NullPointerException.class);

        mvc.perform(patch("/api/cards/comments/1")
                .content(commentInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("수정하려고 하는 댓글이 유저의 것이 아닐 때 수정 기능 실패 테스트")
    void modifyCommentTestFailModifyAndDeleteOnlyMine() throws Exception {
        this.mockUserSetup();

        CommentRequestDto requestDto = new CommentRequestDto("변경 댓글 내용");
        String commentInfo = objectMapper.writeValueAsString(requestDto);

        given(commentService.modifyComment(any(),any(),any())).willThrow(IllegalArgumentException.class);

        mvc.perform(patch("/api/cards/comments/1")
                .content(commentInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("댓글 삭제 기능 성공 테스트")
    void deleteCommentTestSuccess() throws Exception {
        this.mockUserSetup();
        String expectByMessage = "$.[?(@.message == '%s')]";
        mvc.perform(delete("/api/cards/comments/1")
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isOk())
                .andExpect(jsonPath(expectByMessage, "삭제 성공!").exists());
    }
}