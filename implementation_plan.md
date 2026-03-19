# 카드 거래 중계 플랫폼(Card Trading Platform) 개발 계획서

본 플랫폼은 **SNKRDUNK**를 벤치마킹하여, 희귀 카드(포켓몬, 유희왕 등)의 안전하고 투명한 거래를 중계하는 서비스입니다.

## 1. 시스템 아키텍처
- **백엔드**: Spring Boot (Java 17+), Gradle 기반의 RESTful API 서버
- **프론트엔드**: Vue.js (독립된 클라이언트)
- **데이터베이스**: MySQL (8.0+)
- **ORM/Data Access**:
    - **Spring Data JPA**: 일반적인 CRUD 및 엔티티 매핑
    - **MyBatis**: 복잡한 동적 쿼리, 대용량 상품 검색 및 필터링
- **캐싱**: Redis (최신 거래가, 인기 검색어 등)

## 2. 핵심 도메인 및 데이터베이스 설계 (ERD 개요)

### [Component] Core Domain Entities

#### [NEW] Member (회원)
- 이메일, 비밀번호, 닉네임, 권한(ADMIN, USER), 포인트/잔액, 배송지 정보

#### [NEW] Product/Card (상품/카드)
- 카드 명칭, 세트 명, 일련번호, 레어리티, 상세 설명, 대표 이미지
- **상태 관리**: 신규 등록, 검수 대기, 판매 중 등

#### [NEW] Bid (입찰)
- `Type`: BUY_BID(구매 입찰), SELL_BID(판매 입찰)
- `Price`: 입찰 가격
- `Status`: ACTIVE(입찰 중), MATCHED(체결), CANCELED(취소), EXPIRED(만료)

#### [NEW] Trade (거래)
- `Buyer`, `Seller`, `Product` 연결
- `FinalPrice`: 최종 체결 가격
- `TradeStatus`: PAYMENT_PENDING(결제 대기), WAITING_FOR_SELLER(판매자 발송 대기), INSPECTION_PENDING(검수 대기), SHIPPING(배송 중), COMPLETED(거래 완료)

#### [NEW] Inspection (검수)
- `Trade`에 종속
- `Result`: PASS, FAIL, REJECTED
- `Notes`: 검수 상세 의견 (스크래치, 위조 의심 등)

---

## 3. 핵심 기능 구현 계획

### [Component] Trading Engine (거래 엔진)
- **즉시 구매/판매**: 가장 높은 구매 입찰가 혹은 가장 낮은 판매 입찰가와 매칭 시 즉시 거래 체결.
- **입찰 시스템**: 사용자가 지정한 유효 기간 동안 입찰가를 유지하고, 조건이 만족되면 자동 매칭.
- **MyBatis 활용**: 수만 건의 입찰 데이터 중 최적의 매칭 가격을 조회하는 로직을 MyBatis로 최적화.

### [Component] Inspection & Logistics (검수 및 물류)
- 판매자가 플랫폼(검수 센터)으로 상품을 발송하면 상태를 업데이트.
- 관리자가 검수 결과를 직접 입력하고, 합격 시 구매자에게 발송 프로세스 시작.
- 에스크로(Escrow) 로직: 검수 완료 및 구매 확정 시 판매자에게 대금 정산.

### [Component] Search & Filtering (검색 및 필터링)
- 카드 등급(PSA, BGS 등), 상태(A~D), 세트별 필터링 기능 제공.
- **MyBatis 활용**: 다중 조인 및 동적 검색 필터를 MyBatis XML로 관리하여 유지보수성 향상.

---

## 4. 단계별 개발 로드맵

### Phase 1: 기반 인프라 및 인증 (1~2주)
- 프로젝트 뼈대 구성 및 공통 Response/Error Handler 개발.
- Spring Security + JWT 기반 회원가입/로그인.

### Phase 2: 상품 및 입찰 시스템 (2~3주)
- 카드 카탈로그 구축 및 검색 기능.
- 구매 입찰/판매 입찰 CRUD 및 매칭 로직 개발.

### Phase 3: 거래 및 검수 프로세스 (3~4주)
- 거래 상태(Trade Status) 전이 로직(State Machine) 구현.
- 관리자용 검수 관리 API 개발.

### Phase 4: 동적으로 고도화 및 검증 (4주~)
- Redis를 활용한 실시간 시세 조회 최적화.
- 프론트엔드 연동 및 통합 테스트.

---

## 5. 사용자 검토 필요 사항
- **결제 모듈 연동**: 실제 결제 연동(포트원 등)이 필요한지, 아니면 가상 포인트 시스템으로 구축할지 결정이 필요합니다.
- **검수 기준**: 서비스 내에서 공통적으로 사용할 카드 상태(Condition) 등급 규격을 확정해 주시면 DB 설계에 반영하겠습니다.
- **검수 센터 권한**: 별도의 '검수자' 권한이 필요한지 확인 부탁드립니다.
