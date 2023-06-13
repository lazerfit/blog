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
  - 로그인
  - Card 페이징 [V] / [230609]
  - Tag
  - Search   
***

## *개발 일지*   
- ### [230613] &lt;Thymeleaf&gt; 게시글 수정 기능 구현   
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
