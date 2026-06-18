# PUBLIC PLUS Backend

## 프로젝트 소개

PUBLIC PLUS는 공공 체육시설 이용 활성화를 목표로 한 팀 프로젝트입니다. 사용자는 공공 체육시설 정보를 검색하고, 시설 기반 모임에 참여하며, 리뷰와 좋아요를 통해 이용 경험을 공유할 수 있습니다.

이 저장소는 PUBLIC PLUS의 백엔드 API를 담당합니다. 서울시 공공 서비스예약 데이터를 기반으로 시설 정보를 수집·검색하고, 회원 인증, 모임과 활동 참가, 채팅, 알림, 리뷰, 외부 캘린더 연동을 하나의 Spring Boot 애플리케이션 안에서 제공합니다.

## 주요 기능

- **시설 정보 수집 및 검색**: 서울시 공공 체육시설 데이터를 조회·파싱하고, 시설 목록·상세·필터·위치 기반 검색 API를 제공합니다.
- **회원 및 인증**: 회원가입, 로그인, 로그아웃, 토큰 재발급, 프로필 수정, 이메일 인증, JWT 기반 인증과 OAuth2 소셜 로그인 흐름을 지원합니다.
- **모임 게시판**: 체육시설과 스포츠 유형을 기반으로 모임을 생성하고, 목록·상세 조회, 수정, 삭제, 필터 검색을 제공합니다.
- **활동 참가 관리**: 모임 활동을 생성·조회·수정·삭제하고, 사용자의 활동 참가와 나가기를 처리합니다.
- **리뷰와 좋아요**: 시설별 내부 리뷰 작성·수정·삭제, 외부 블로그 리뷰 조회, 시설 좋아요와 좋아요 취소 기능을 제공합니다.
- **채팅**: 채팅방 생성·참여·조회와 메시지 저장 API를 제공하고, WebSocket/STOMP 기반 메시지 브로드캐스트를 구성합니다.
- **알림과 푸시**: 사용자 알림 조회·생성·삭제, Firebase Cloud Messaging 기반 푸시 알림과 FCM 토큰 관리를 포함합니다.
- **일정 연동**: Google Calendar API를 활용해 캘린더와 이벤트 관련 기능을 제공합니다.
- **API 문서화**: Springdoc OpenAPI 설정을 통해 주요 REST API와 JWT 인증 스키마를 문서화합니다.

## 기대 효과

- 공공 체육시설의 검색 접근성을 높여 이용자가 원하는 시설을 더 쉽게 찾을 수 있습니다.
- 시설 이용 후기, 좋아요, 외부 리뷰를 함께 제공해 이용 전 탐색 경험을 보강합니다.
- 시설 기반 모임과 활동 참가 흐름을 연결해 생활 체육 커뮤니티 형성을 돕습니다.
- 채팅, 알림, 캘린더 연동을 통해 모임 생성 이후의 커뮤니케이션과 일정 관리를 지원합니다.
- 공공 데이터와 커뮤니티 데이터를 함께 다루는 백엔드 구조를 통해 시설 이용 활성화 서비스를 확장할 수 있는 기반을 제공합니다.

## 프로젝트 구조

```text
WEB1_2_PublicPlus_BE/
├── build.gradle
├── settings.gradle
├── Dockerfile
├── gradlew
├── my-push-notification-app/       # FCM/캘린더 연동 확인용 보조 앱
└── src/
    ├── main/
    │   ├── java/backend/dev/
    │   │   ├── activity/           # 활동 생성, 수정, 참가, 나가기
    │   │   ├── chatroom/           # 채팅방, 참여자, 메시지, WebSocket
    │   │   ├── facility/           # 시설 API 수집, 파싱, 상세, 검색
    │   │   ├── googlecalendar/     # Google Calendar 캘린더·이벤트 연동
    │   │   ├── likes/              # 시설 좋아요
    │   │   ├── meeting/            # 모임 게시판
    │   │   ├── notification/       # 알림, FCM 토큰, 푸시 알림
    │   │   ├── review/             # 내부 리뷰와 외부 리뷰 조회
    │   │   ├── setting/            # 보안, JWT, OAuth2, Redis, Swagger, WebSocket 설정
    │   │   ├── tag/                # 리뷰·시설 태그
    │   │   ├── user/               # 회원, 관리자, 이메일, 소셜 로그인
    │   │   └── utils/              # 공통 유틸리티
    │   └── resources/              # 애플리케이션 설정과 정적 리소스
    └── test/                       # 백엔드 테스트 코드
```

## 기술 스택

### Backend

- Java 17
- Spring Boot 3.3.5
- Spring Web, Spring WebFlux
- Spring Data JPA
- Spring Security
- Spring OAuth2 Client
- Spring WebSocket
- Spring Data Redis
- Spring Mail
- Spring Validation

### Persistence & Query

- Hibernate
- QueryDSL
- MySQL Connector/J
- H2 Database

### Authentication & API

- JWT (jjwt)
- Springdoc OpenAPI
- Thymeleaf, Thymeleaf Spring Security

### External Integration

- Google API Client
- Google OAuth Client
- Google Calendar API
- Firebase Admin SDK
- Resilience4j Retry
- Jsoup

### Test & Build

- Gradle
- JUnit Platform
- Spring Boot Test
- Spring Security Test
- Mockito
- GreenMail
- MockWebServer
- Lombok

## 팀 구성

- PM: 김수빈
- 백엔드 팀장: 김병주
- 프론트엔드 팀장: 강선영
- 팀원: 신현우, 위성운, 이진우

## 프로젝트 일정

- 개발 기간: 2023.11.12 ~ 2023.12.09 (총 4주)

### 주요 일정

- 사전 기획: 2023.11.12 ~ 2023.11.15
- 요구사항 분석 및 설계: 2023.11.15 ~ 2023.11.19
- 기능 구현 및 테스트: 2023.11.20 ~ 2023.12.06
- 서비스 구축: 2023.12.07 ~ 2023.12.09

## 라이선스

This project is licensed under the MIT License.
