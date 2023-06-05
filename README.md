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

## *개발 일지*   
- [230605] Integrate Summernote(WYSIWYG)   
***
- [230602] 기본적인 layout 생성   
  - navbar, leftcolumn, rightcolumn, footer 로 layout 나눔   
  - Thymeleaf layout dialect 사용
    - layout.html 생성 (navbar, rightcolumn, footer 는 여기서 구현)   
    - 새로운 html 생성 시 leftcolumn(메인) 만 구현           
  - TODO:   
    - 기능 개발 : 글쓰기 / 로그인 / Card 페이징 / Tag / Search    
***
- [230530] Thymeleaf 활용한 SSR 설계 시작
- Thymeleaf   
  - Natural Template   
    - 순수 HTML 지원 + View template 활용
  - Spring 통합 지원   
 - Bootstrap   
   - Template 제공으로 빠르고 쉽게 웹페이지 생성 가능   
***
- [230530] 페이징 기능 추가
  - Page 는 1부터 시작
  - Default size 는 10
***
- [230525] 기본적인 CRUD 기능 생성
  - Service 단에서 CRUD test 완료
  - MockMVC 활용하여 Controller test 완료
