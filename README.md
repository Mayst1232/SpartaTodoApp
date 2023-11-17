# SpartaTodoApp
## 설계해야 하는 것
- [ ]  **🆕 회원 가입 API**
    - **API 가 사용될 화면 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/80111660-613a-4ce7-bdd6-49975561e40a/Untitled.png)
        
    - username, password를 Client에서 전달받기
    - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
    - DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    - 참고자료
        1. https://mangkyu.tistory.com/174
        2. [https://ko.wikipedia.org/wiki/정규_표현식](https://ko.wikipedia.org/wiki/%EC%A0%95%EA%B7%9C_%ED%91%9C%ED%98%84%EC%8B%9D)
        3. https://bamdule.tistory.com/35
            
            
- [ ]  **🆕 로그인 API**
    - **API 가 사용될 화면 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/5f20f99c-5203-4afb-87c3-d9cb96215840/Untitled.png)
        
    - username, password를 Client에서 전달받기
    - DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
    - 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고, 
    발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기
- [ ]  ~~**게시글~~ 할일카드 작성 기능 API**
    - **API 가 사용될 화면 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/d163fef3-2962-4762-9a76-bcd247b4f852/Untitled.png)
        
    - 토큰을 검사하여, 유효한 토큰일 경우에만 할일 작성 가능
    - `할일 제목`,`할일 내용`, `작성일`을 저장할 수 있습니다. (~~작성자명, 비밀번호)~~
    - 할일 제목, 할일 내용을 저장하고
    - 저장된 할일을 Client 로 반환하기(username은 로그인 된 사용자)
- [ ]  **선택한 ~~게시글~~ 할일카드  조회 기능 API**
    - **API 가 사용될 화면 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/9f9ae771-6751-402a-add3-3249e998c704/Untitled.png)
        
    - 선택한 ~~게시글~~ 할일 의 정보를 조회할 수 있습니다.
        - 반환 받은 할일 정보에는 `할일 제목`,`할일 내용`, `작성자` , `작성일`정보가 들어있습니다.
        - ~~반환 받은 게시글의 정보에 비밀번호는 제외 되어있습니다.~~
- [ ]  ~~**게시글~~ 할일카드 목록 조회 기능 API**
    - **API 가 사용될 화면 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/dd72b3c7-39d4-4018-b04e-a9fb403028f7/Untitled.png)
        
    - 등록된 할일 전체를 조회할 수 있습니다.
        - 회원별로 각각 나누어서 할일 목록이 조회됩니다.
        - 반환 받은 할일 정보에는 `할일 제목`, `작성자` , `작성일`, `완료 여부`정보가 들어있습니다.
        - ~~반환 받은 할일 정보에 비밀번호는 제외 되어있습니다.~~
    - 조회된 할일 목록은 `작성일` 기준 내림차순으로 정렬 되어있습니다.
- [ ]  **선택한 ~~게시글~~ 할일카드 수정 기능 API**
    - **API 가 사용될 화면 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/08e97bc0-2e31-41cb-a4b2-8baf809c4c47/Untitled.png)
        
    - 선택한 ~~게시글~~ 할일카드의 `제목`, `작성 내용`을 수정할 수 있습니다. (~~작성자명~~)
        - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
        - 할일 제목, 할일 내용을 수정하고 수정된 할일 정보는 Client 로 반환됩니다.
        - ~~서버에 게시글 수정을 요청할 때 비밀번호를 함께 전달합니다.~~
        - ~~선택한 게시글의 비밀번호와 요청할 때 함께 보낸 비밀번호가 일치할 경우에만 수정이 가능합니다.~~
    - 수정된 ~~게시글~~ 할일의 정보를 반환 받아 확인할 수 있습니다.
        - 반환 받은 할일 정보에는 `할일 제목`,`할일 내용`, `작성자` , `작성일`정보가 들어있습니다.
        - ~~반환 받은 게시글의 정보에 비밀번호는 제외 되어있습니다.~~
- [ ]  **🆕 할일카드 완료 기능 API**
    - **API 가 사용될 화면 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/197c518e-48c2-4a70-8fed-67fcfb983af8/Untitled.png)
        
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 할일카드 만 완료 가능
    - 완료처리 한 할일카드는 목록조회시 `완료 여부`필드가 TRUE 로 내려갑니다.
    - `완료 여부` 기본값은 FALSE
- [ ]  **🆕 댓글 작성 API**
    - **API 가 사용될 화면 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/9b4f8b70-ceb6-497f-b95b-58835c9af662/Untitled.png)
        
    - 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
    - 선택한 할일의 DB 저장 유무를 확인하기
    - 선택한 할일이 있다면 댓글을 등록하고 등록된 댓글 반환하기
- [ ]  **🆕 댓글 수정 API**
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
- [ ]  **🆕 댓글 삭제 API**
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
- [ ]  **🆕 예외 처리 (ResponseEntity 사용)**
    - **API 예외응답 예시 보기**
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/d1f62f78-479b-488e-b19b-5ea0a71ad504/Untitled.png)
        
    - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - DB에 이미 존재하는 username으로 회원가입을 요청한 경우 "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 Client에 반환하기

## ERD 설계
![SpartaTodoERD](https://github.com/Mayst1232/SpartaTodoApp/assets/48579987/26c79873-9d48-4a97-9d0b-4fd4609accb5)

## API 명세서 설계
![SpartaTodoApi](https://github.com/Mayst1232/SpartaTodoApp/assets/48579987/84fb954d-35c8-4d53-a337-c28a07e56812)

## PostMan API 설계
https://documenter.getpostman.com/view/30857682/2s9YXpWJpo#869d62ab-cec4-41bf-bb31-7e6b6aa796e3

## 사용한 기술
1. JWT 인증 / 인가
2. Spring Security를 이용한 보안
3. @Valid를 이용한 유효성 판단
4. HttpResponse를 이용한 헤더로 원하는 정보 전달
5. JPA를 이용한 Server와 DB 연결
6. Postman을 이용한 JWT 토큰 헤더로 주고 받기
7. ResponseEntity를 이용한 Status와 Message 전달하기

## 어려웠던 점
1. Spring Security 자체가 굉장히 복잡하고 어려웠던 것 같다.
2. JWT에 대한 이해도가 많이 떨어지는 것 같다.
3. @Valid에 대한 이용방법이 미숙했다.
4. ResponseEntity에 대한 이용방법이 미숙했다.

### 해결방법
1. Spring Security에 대한 이해도를 높여야할 필요성이있습니다. 좀 더 자세하게 뜯어보고 공부를 많이 해봐야되겠다는 생각을 갖게 되었습니다.
2. JWT 또한 마찬가지로 좀 더 깊게 이해할 필요성을 느꼈습니다. 강의를 좀 더 보고 갖고있는 책에 JWT에 관한 내용을 찾아봐야겠습니다.
3. @Valid의 Min과 Max에 대한 이해가 문제였습니다. 이 두 가지 Annotation이 숫자에만 해당하는 것을 몰랐습니다.. 좀 더 찾아보고 사용하는 습관을 들여야될 것 같습니다.
4. ResponseEntity를 이용한 예외처리를 통해 Status와 message를 전달하는 방법을 이번 프로젝트를 통해 연습을 하였습니다. 좀 더 많은 코드를 보고 더 많은 경험이 필요할 것 같습니다.
