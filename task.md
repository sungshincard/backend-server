# 카드 거래 중계 플랫폼(Card Trading Platform) 개발 계획

- [O] **프로젝트 초기 설정 및 기획**
    - [O] 기술 스택 및 아키텍처 확정 (Spring Boot 3.4, Java 21, MySQL, JPA + MyBatis)
    - [O] 핵심 도메인 분석 및 DB 스키마 설계 (Member, Product, Bid, Trade, Inspection)
    - [O] 공통 API 응답 규격(DTO) 정의
        - [O] `ApiResponse` 객체 및 `GlobalExceptionHandler` 구현
    - [O] 프로젝트 기본 구조 세팅 (Gradle 빌드 환경, application.yml 설정)

- [/] **핵심 기능 개발 - 백엔드**
    - [/] 사용자(Member) 및 인증(Security/JWT) 시스템
        - [O] `Member` 엔티티 및 `Repository` (JPA) 구현
        - [O] `MemberService` (회원가입/로그인 로직) 및 `MemberController` (Join/Login API) 구현
        - [O] `SecurityConfig` (BCryptPasswordEncoder, JWT Filter) 설정
        - [O] JWT 발급 및 인증 필터 구현
    - [ ] 상품(Product/Card) 카탈로그 및 검색 기능 (MyBatis 활용)
    - [ ] 입찰(Bidding) 및 즉시 구매/판매 시스템
    - [ ] 정산 및 거래 상태(Trade State) 관리 시스템
    - [ ] 검수(Inspection) 관리 시스템

- [ ] **프론트엔드 연동 및 검증**
    - [ ] API 문서화 (Swagger 등)
    - [ ] Vue.js 프론트엔드 연동 테스트
    - [ ] 최종 워크스루 작성
