# lazerfit's blog   

lazerfit 의 개인 blog 프로젝트입니다.   
개발/블록체인/음식점 리뷰 등을 Post 합니다.

## *Development Environment*   

- IntelliJ IDEA
- Postman
- Git/Github
- SourceTree
- MySQL

## *Tech Stack*   

### Backend  

#### Framework/Library   

- Java 17
- SpringBoot 3.1.0
- SpringBoot Security
- Spring Data JPA
- Querydsl

#### Build Tool   

- Gradle


#### Database   

- MySQL
***


## *Blog 개발 목적*   

- 앞서 학습한 웹 개발 기술들을 내 것으로 체화
- 웹 애플리케이션의 동작 과정들을 숙지
- 나만의 공간을 개발함으로써 보다 높은 내적 동기 생성   
***
- ### TODO:
  - 글쓰기 [V] / [230607]
  - 로그인 [V] / [230619]
  - Card 페이징 [V] / [230609]
  - Tag
  - Search [V] / [230628]   
***

## *개발 일지*
- ### [230705] 카테고리 관리 / 카테고리 수정 / 카테고리 삭제 기능 추가
  - 카테고리 수정 Button click -> 카테고리 Table Modal Popup -> 삭제 Button click -> Category delete 처리
- ### [230704] 게시글 수정 내 카테고리 수정 추가
  - Home 화면에 보여지는 카테고리 순서 수정 기능 필요
    - 현재 Category Table / list_order 컬럼으로 순서 정렬
    - ~~해결 방법 생각중임~~
      - Modal Popup -> editable Table 에서 수정 후 Update Query 나가도록 할 예정
- ### [230704] Add category on Posts
  - Category 별 게시글 조회
- ### [230630] Add Category
  - Category 저장
  - list-group 에서 지정한 listOrder 순서대로 정렬되도록 쿼리 수정 필요 / 현재는 findAll() 사용
    - findAll(Sort) 로 해결
- ### [230628] Add sns sharing
  - add copy url
  - [230629] add Twitter sharing
  - local 에서 facebook 공유 불가
- ### [230628] Add Searching Posts by title keyword with Querydsl
  - 검색 결과 반환 시 페이징도 같이 처리
  - Pagination 에서 다음페이지 클릭 시 검색 결과 반환 Paging 풀림(전체 Posts 데이터 관한 Pagination 작동)
    - Controller 에서 Model 로 Keyword 넘김
    - thymeleaf if 로 만약 keyword 가 넘어왔을시 검색된 Posts Pagination 작동
- ### [230627] Add social-media-icon on Footer
  - Github / LinkedIn / ScrollToTop
  - TODO: 
    - Make Spring Security Test code
- ### [230622] Login 버튼 보임 -> 로그인시 프로필 사진 보이도록 개선
  - [230623] 프로필 사진 클릭 시 [관리/글쓰기/로그아웃] 토글 창 나오도록 개선
  - [230627] 토글창 이외 클릭 -> 드랍다운 메뉴 닫힘
- ### [230620] 시큐리티 핸들러 처리
  - ~~AccessDinedException 오류 Page 추가~~
  - ~~authenticationEntryPoint 오류 Page 추가~~
    - 401.html, 403.html 'static/error' 경로에 추가 -> 자동으로 작동
- ### [230619] 로그인 기능 구현
  - Spring security 6 
  - 로그인 시 ROLE_ADMIN 부여
  - TODO : 
    1. ~~ADMIN 계정에만 게시글 수정 / 삭제 보이도록 하기~~
    2. ~~로그아웃 구현~~
    3. DB 에 아이디는 한 개만 저장 -> SignUp 막기
    4. ~~시큐리티 핸들러 처리~~
- ### [230615] &lt;Spring Security&gt; 로그인 기능 구현 시작   
- ### [230614] &lt;TroubleShooting&gt; 게시글 삭제 후 홈 화면 리다이렉션 시 기존 글이 남아있는 문제 발생
  - 기존 Javascript location.replace or location.href 통해 Home 화면 진입 시 기존 게시글이 남아 있음
  - form submit 이용하여 Controller 에서 redirect 하여 문제 해결
  - 그러나, redirect 후 뒤로 가기 누르면 삭제한 Post 보임   

- ### [230613] &lt;Thymeleaf&gt; 게시글 수정/삭제 구현   
  - 뒤로가기 / 새로고침 시 경고 알람 발생
  - submit 시 경고 알람 발생 X

- ### [230613] Home 화면 최근 게시글 표시 개선
  - Home 화면 좌상단에 전체 게시글 개수 표시   
  - 제목 클릭 시 상세 페이지로 이동   
  - text truncate 적용
   
- ### [230609] 페이징 기능 개선
  - Pageable 활용
  - @QueryProjection 활용하여 Page&lt;Entity&gt; 가 아닌 Page&lt;Dto&gt; 로 데이터 전송   
  - Pagination 구현
   
- ### [230608] Home 화면에 최근 게시글 게시 
  - ~~"/" 로 진입시 "/posts" 로 redirect 하면서 page 와 size parameter 넘김~~   
    - Pageable 글로벌 세팅 통해 첫 화면 url query parameter 넘기지 않아도 페이징 가능   
  - ~~첫 화면의 url 은 깔끔하게 유지하고 싶었으나 parameter 정보가 붙음~~
   
- ### [230607] 게시글 DB 저장 구현
   
- ### [230605] Integrate Summernote(WYSIWYG)
  - Toolbar 커스터마이징 편리   
  - 간단한 설정만으로 사용 가능   
  - 부트스트랩 사용   
   
- ### [230602] 기본적인 layout 생성   
  - navbar, leftcolumn, rightcolumn, footer 로 layout 나눔   
  - Thymeleaf layout dialect 사용
    - layout.html 생성 (navbar, rightcolumn, footer 는 여기서 구현)   
    - 새로운 html 생성 시 leftcolumn(메인) 만 구현
   
- ### [230530] Thymeleaf 활용한 SSR 설계 시작
- Thymeleaf   
  - Natural Template   
    - 순수 HTML 지원 + View template 활용
  - Spring 통합 지원   
 - Bootstrap   
   - Template 제공으로 빠르고 쉽게 웹페이지 생성 가능   
   
- ### [230530] 페이징 기능 추가
  - Page 는 1부터 시작
  - Default size 는 6
   
- ### [230525] 백단에서 기본적인 CRUD 기능 생성
  - Service 단에서 CRUD test 완료
  - MockMVC 활용하여 Controller test 완료
