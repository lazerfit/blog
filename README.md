# [gomdolog](https://gomdolog.store)

lazerfit 의 개인 blog 프로젝트입니다.   
개발/블록체인/음식점 리뷰 등을 Post 합니다.

- MVC 프레임워크 기반 백엔드 서버 구축
- Thymeleaf 기반 SSR 구축
- 위지윅(summernote), Tagify(태그 입력 모듈), utternaces(댓글 모듈) 사용
- 게시글 임시저장 기능 구현 (HTML5 localStorage 사용)
- 중복없는 조회수 구현(HTML5 localStorage)+OptimisticLock 적용
- 캐싱 구현 
- 구글, 네이버 간편 로그인 사용
- 카카오, 네이버, 페이스북, X 공유하기 사용
- 모바일 화면 최적화 
- Github Action, nginx 이용한 CI/CD pipeline 구축
- Cloudflare 서비스 사용
- Let's encrypt 툥한 SSL 인증서 발급 및 자동 갱신
 
## *Development Environment*   

- IntelliJ IDEA
- Postman
- Git/Github
- SourceTree
- MySQL

## *Tech Stack*   
<img alt="Spring_Boot" src ="https://img.shields.io/badge/Spring_Boot-6DB33F.svg?&style=for-the-badge&logo=Spring-Boot&logoColor=white"/><img alt="Spring_Security" src ="https://img.shields.io/badge/Spring Security-6DB33F.svg?&style=for-the-badge&logo=Spring Security&logoColor=white"/><img alt="JPA" src ="https://img.shields.io/badge/JPA-59666C.svg?&style=for-the-badge&logo=Hibernate&logoColor=white"/>

<img alt="Thymeleaf" src ="https://img.shields.io/badge/Thymeleaf-005F0F.svg?&style=for-the-badge&logo=Thymeleaf&logoColor=white"/><img alt="Bootstrap" src ="https://img.shields.io/badge/Bootstrap-7952B3.svg?&style=for-the-badge&logo=Bootstrap&logoColor=white"/><img alt="Amazon_AWS" src ="https://img.shields.io/badge/Amazon_AWS-232F3E.svg?&style=for-the-badge&logo=Amazon-AWS&logoColor=white"/><img alt="Amazon_RDB" src ="https://img.shields.io/badge/ Amazon RDB-232F3E.svg?&style=for-the-badge&logo=Amazon DynamoDB&logoColor=white"/><img alt="Amazon S3" src ="https://img.shields.io/badge/ Amazon S3-569A31.svg?&style=for-the-badge&logo=Amazon S3&logoColor=white"/><img alt="NGINX" src ="https://img.shields.io/badge/NGINX-009639.svg?&style=for-the-badge&logo=NGINX&logoColor=white"/>

#### Build Tool   

<img alt="Gradle" src ="https://img.shields.io/badge/Gradle-02303A.svg?&style=for-the-badge&logo=Gradle&logoColor=white"/>


#### Database   

<img alt="MariaDB" src ="https://img.shields.io/badge/MariaDB-003545.svg?&style=for-the-badge&logo=MariaDB&logoColor=white"/><img alt="mysql" src ="https://img.shields.io/badge/mysql-4479A1.svg?&style=for-the-badge&logo=MariaDB&logoColor=white"/>
***


## *Blog 개발 목적*   

- 앞서 학습한 웹 개발 기술들을 내 것으로 체화
- 웹 애플리케이션의 동작 과정들을 숙지
- 나만의 공간을 개발함으로써 보다 높은 내적 동기 생성   
***
## *시스템 아키텍쳐*
<img src="https://drive.google.com/uc?export=download&id=111MYAYT-b6AT-dOWqUU9rgH2zm8Zat57&usp=drive_fs">

## *패키지 아키텍처*
<img src="https://drive.google.com/uc?export=download&id=1123lVd-Rq5t0xCigmbukynWFeMWlV4TA">

## *ERD*
<img src="https://drive.google.com/uc?export=download&id=115m-ELiFnMAlrOsmdr8KvBrn-js6cInL">

***
## 핵심 기능

### 반응형 웹
부트스트랩과 css를 활용하여 모바일 친화 페이지 생성
<img src="https://drive.google.com/uc?export=download&id=1-YhSO0owQpL2KfWwHmsV1vRK4apEB10Z">

### 소셜 로그인
Spring security OAuth2 활용  
[OAuthAttributes](https://github.com/lazerfit/blog/blob/main/src/main/java/com/blog/config/user/OAuthAttributes.java)
활용하여 각 플랫폼마다 다른 attribute 를 회원 정보로 가져올 수 있음

### 모듈 사용
높은 생산성과 유지보수성을 위하여 위지윅([summernote](https://summernote.org/)), [Tagify](https://yaireo.github.io/tagify/)(태그 입력 모듈), [utterances](https://utteranc.es/)(댓글 모듈) 사용
<img src="https://drive.google.com/uc?export=download&id=116A4jv6rd0wAaW9CSUHbwWRfjOjC8GNG">

### LocalStorage 활용  
#### (손쉬운 구현, 5MB 넉넉한 저장 공간)
- 중복없는 조회수 카운팅 시 로컬 스토리지 활용
  - 'visitedPosts' 배열에 해당 postId가 없다면 서버에 조회수 증가 요청 + 배열에 현재 postId 입력
  - 'expiredDate' 에 처음 배열이 생성된 날짜 입력
    - 생성 날짜가 하루 이상 지나게 되면 삭제
  - OptimisticLock을 사용하여 혹시 발생할 수 있는 동시성 문제 예방
- 게시글 임시저장 구현 시 로컬 스토리지 활용
  - 30초 단위로 자동 저장되며 글쓰기 제출 시 삭제

### 게시글 CRUD, Pageable
- 기본적인 CRUD와 페이징을 구현
- 게시글 수정/삭제/생성은 csrf 토큰과 ADMIN role을 가진 유저만 데이터를 전송할 수 있음
- thymeleaf와 spring security 연동하여 ADMIN role을 가진 사람만 게시글 수정/삭제 버튼이 보임
<img src="https://drive.google.com/uc?export=download&id=117gVBNUL0ex8UE9xUcLW5E8ZUSLNLu-k">
<img src="https://drive.google.com/uc?export=download&id=116dSBO_lqeOo-pF4lDvPAyqK2aaSgBpT">

### 소셜 공유하기
- Facebook, X(twitter), 카카오톡, 네이버, url복사 기능 구현

### Cache
- 간단한 단계로 구현할 수 있고 뛰어난 성능을 제공하는 Caffeine cache 사용
- 페이징된 포스트와 포스트, 카테고리를 캐싱
- 깔끔하고 충돌없이 캐싱하기 위하여 포스트 게시/수정/삭제 시 캐시 삭제

### 카테고리/태그/검색어 별 게시글 검색 기능
- 카테고리/태그/검색어 모두 SELECT ~ WHERE 통하여 검색
- 검색어는 LIKE %검색어% 사용하여 검색하기 때문에 검색 성능 낮을 수 있음

### 에러 처리
- ExceptionHandler 통해 에러코드와 메시지를 responseEntity로 반환하도록 함
- ajax로 통신한 경우 에러 메시지를 alert으로 반환
- 401/403 커스텀 에러페이지 생성

### ADMIN 페이지 구현
- [AsmrProg](https://github.com/AsmrProg-YT) Theme Custom
- *Method Security* 활용하여 ADMIN role 가진 사람만 접속 가능
- User 편집
- Category 편집 구현
<img src="https://drive.google.com/uc?export=download&id=11ApD1yFt4wnBJlDIqtLvyg_ECLqaBmrN">

### AWS
- EC2 / RDS(MariaDB) / S3 / CodeDeploy 사용
- EC2의 부족한 램 용량을 극복하기 위하여 메모리 swap 설정
- RDS 인텔리제이와 연동하여 사용

### HTTPS
- 가비아 도메인 구입
- cloudflare DNS 사용
- Let's encrypt certbot 사용하여 SSL 발급 및 자동 갱신
- nginx SSL 적용


### CI/CD
- Github action 사용하여 main branch push 시 build 한 후 zip 파일 압축
- 압축 파일 AWS S3 업로드
- codeDeploy 스크립트 실행
- nginx와 연결되어 있지 않은 스프링 부트를 중지
- nginx와 연결되어 있지 않는 port로 새 스프링 부트를 실행

### EC2 모니터링
- EC2 인스턴스의 CPU 사용량, 램 사용량, 네트워크 트래픽 등을 모니터링
- 오픈소스 Prometheus grafana 사용
<img src="https://drive.google.com/uc?export=download&id=10zKiLGhsY7HwTLb2nW3L68Ae-FmoXkMw">

***
- ### TODO:
  - ~~글쓰기~~ [V] / [230607]
  - ~~로그인~~ [V] / [230619]
  - ~~Card 페이징~~ [V] / [230609]
  - ~~Tag~~ [V] / [230708]
  - ~~Search~~ [V] / [230628]
  - ~~조회수~~ [V] / [230709]
  - ~~댓글~~ [V] / [230718]
  - ~~댓글 수정시 (수정) 표시~~ [V] [231122]
  - Admin 페이지 개선
  - 다크모드 토글 만들기
***

## *개발 일지*
[개발 일지](https://github.com/lazerfit/blog/wiki/%EA%B0%9C%EB%B0%9C-%EC%9D%BC%EC%A7%80)
