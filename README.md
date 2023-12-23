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
## 개선 사항

### summernote 코드 블럭 추가
- Summernote에 코드 블록을 추가하기 위해 code-prettify와 highlight 플러그인을 활용하여 코드의 가독성을 개선했습니다. 이를 통해 사용자가 작성한 코드가 보다 명확하게 표현되고 하이라이팅되어 더욱 시각적으로 잘 구분되도록 하였습니다.

***
## 핵심 기능

### 반응형 웹
모바일 친화적인 UI를 부트스트랩과 CSS를 통해 개발하여 사용자 경험을 향상시켰습니다.  

<img src="https://drive.google.com/uc?export=download&id=1-YhSO0owQpL2KfWwHmsV1vRK4apEB10Z">

### 소셜 로그인
Spring security OAuth2 활용하여 다양한 소셜 플랫폼의 로그인을 지원합니다.
[OAuthAttributes](https://github.com/lazerfit/blog/blob/main/src/main/java/com/blog/config/user/OAuthAttributes.java)
클래스를 통해 플랫폼별로 다른 속성을 효과적으로 수집합니다.

### 외부 모듈 사용
위지윅 에디터([summernote](https://summernote.org/)), 태그 입력 모듈([Tagify](https://yaireo.github.io/tagify/)), 댓글 모듈([utterances](https://utteranc.es/))을 통해 생산성을 높이고 사용자 경험을 향상시켰습니다.

<img src="https://drive.google.com/uc?export=download&id=116A4jv6rd0wAaW9CSUHbwWRfjOjC8GNG">

### LocalStorage 활용  
- 브라우저의 LocalStorage를 이용하여 중복 없는 조회수 카운팅 및 게시글 임시저장 기능을 구현했습니다.
- 동시성 문제를 예방하기 위해 Optimistic Lock을 통해 안정성을 확보했습니다.

### 게시글 CRUD, Pageable
- Thymeleaf와 Spring Security를 결합하여 ADMIN 권한을 가진 사용자만이 게시글 수정/삭제를 수행할 수 있도록 구현했습니다.
- 기본적인 CRUD와 페이징 기능을 제공하여 웹 페이지의 유연한 데이터 관리를 지원합니다.

<img src="https://drive.google.com/uc?export=download&id=117gVBNUL0ex8UE9xUcLW5E8ZUSLNLu-k">
<img src="https://drive.google.com/uc?export=download&id=116dSBO_lqeOo-pF4lDvPAyqK2aaSgBpT">

### 소셜 공유하기
- Facebook, Twitter, 카카오톡, 네이버 등 다양한 플랫폼에 대한 소셜 공유 기능을 구현했습니다.
- URL 복사 기능도 함께 제공하여 사용자의 편의성을 고려했습니다.

### Cache
- Caffeine Cache를 활용하여 페이징된 포스트, 포스트, 카테고리를 효율적으로 캐싱합니다.
- 게시글 생성, 수정, 삭제 시에는 캐시를 적절히 관리하여 일관된 데이터를 제공합니다.

### 카테고리/태그/검색어 별 게시글 검색 기능
- 카테고리, 태그, 검색어에 대한 검색 기능을 각각의 SELECT ~ WHERE 문을 통해 구현했습니다.
- 검색어는 LIKE %검색어% 를 사용하므로 성능에 주의가 필요합니다.

### 에러 처리
- ExceptionHandler를 활용하여 발생 가능한 에러에 대한 세밀한 예외 처리를 수행합니다.
- Ajax 통신 시 에러 메시지를 간결하게 제공하여 사용자에게 명확한 정보를 전달합니다.
- 401/403 커스텀 에러페이지 생성하였습니다.

### ADMIN 페이지 구현
- [AsmrProg](https://github.com/AsmrProg-YT) 테마를 커스터마이징하여 사용합니다.
- *Method Security* 활용하여 ADMIN role 가진 사람만 접속할 수 있습니다.

<img src="https://drive.google.com/uc?export=download&id=11ApD1yFt4wnBJlDIqtLvyg_ECLqaBmrN">

### AWS
- Amazon EC2, RDS(MariaDB), S3, CodeDeploy를 통해 안정적이고 확장 가능한 클라우드 환경을 구축했습니다.
- 메모리 부족 문제를 극복하기 위해 EC2에 메모리 스왑을 설정했습니다.

### HTTPS 적용
- 가비아 도메인 구입과 Cloudflare DNS를 활용하여 보안 강화를 위한 전문적인 HTTPS 프로토콜을 구성했습니다.
- Let's Encrypt Certbot을 활용하여 SSL 인증서를 발급하고 자동 갱신하도록 구성했습니다.
- Nginx에 SSL을 적용하여 데이터의 기밀성과 무결성을 유지했습니다.

### CI/CD Pipeline
- Github Action을 활용하여 지속적 통합 및 배포 파이프라인을 자동화했습니다.
- 빌드 후에는 S3에 업로드하고 CodeDeploy 스크립트를 실행하여 신속한 배포를 구현했습니다.
- Nginx와 연결되지 않은 스프링 부트를 중지하고, 새로운 포트로 스프링 부트를 실행하여 롤링 업데이트를 수행합니다.

### EC2 모니터링
- Prometheus Grafana를 활용하여 EC2 인스턴스의 CPU 사용량, 램 사용량, 네트워크 트래픽 등을 모니터링하고 시각화했습니다.

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
