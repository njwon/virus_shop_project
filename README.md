# 악성코드 스캐너 & 쇼핑몰 통합 웹 프로젝트

> Java Servlet / JSP 기반 풀스택 웹 애플리케이션
> 개발자: 노정원 | 개발 기간: 2025.12 ~ 2026.04

---

## 프로젝트 소개

VirusTotal API를 연동한 **악성코드 파일 스캐너**와 **상품 쇼핑몰**을 하나의 서버에서 운영하는 통합 웹 서비스입니다.
Spring이나 ORM 없이 **순수 Java Servlet + JDBC**로 백엔드를 직접 구현하여 웹 동작 원리와 보안 처리 전 과정을 직접 다뤘습니다.

---

## 주요 기능

### 악성코드 스캐너
- 파일 업로드 후 **VirusTotal API v3**에 분석 요청
- 악성(Malicious) / 의심(Suspicious) / 무해(Harmless) / 미탐지(Undetected) 결과 표시
- 안전 / 위험 판정 및 사용자별 스캔 이력 관리

### 쇼핑몰
- 상품 목록 조회, 상세 페이지
- 장바구니 담기 / 수량 누적 / 결제(비우기)
- 관리자 전용 상품 CRUD (이미지 포함)

### 회원 시스템
- 회원가입 / 로그인 / 로그아웃
- **자동 로그인 (Remember-Me)** — 30일 쿠키, 사용 시마다 토큰 교체
- 프로필 수정 / 비밀번호 변경 / 계정 삭제
- USER / ADMIN 권한 분리

### 게시판
- 문의글 작성 / 조회 / 수정 / 삭제
- 페이지네이션 (5개씩, 5페이지 단위 네비게이션)
- 조회수 자동 증가

---

## 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 11 |
| Server | Apache Tomcat 9 |
| Database | Oracle XE 21c (Docker) |
| DB 연결 | JDBC + JNDI + DBCP 커넥션 풀 |
| View | JSP + JSTL + Vanilla JS |
| Build | Maven |
| 외부 API | VirusTotal API v3 |
| JSON | org.json |
| 파일 업로드 | cos.jar (MultipartRequest) |
| 비밀번호 | jBCrypt |
| 로깅 | SLF4J |

---

## 아키텍처

Spring 없이 직접 구현한 **4계층 Clean Architecture**입니다.

```
HTTP 요청
    ↓
[Controller] Servlet
  - 요청 파라미터 수신
  - 인증 / 인가 체크
  - UseCase 호출
    ↓
[UseCase] 비즈니스 로직
  - 입력값 검증 (정규식, 길이, 형식)
  - 도메인 규칙 처리
  - 예외 처리 (UseCaseException)
    ↓
[Repository] 인터페이스 (추상화 경계)
    ↓
[RepositoryImpl] DB 접근
  - PreparedStatement 직접 작성
  - 트랜잭션 관리 (commit / rollback)
    ↓
Oracle DB (JDBC / JNDI)
```

---

## 프로젝트 구조

```
project/
├── src/main/
│   ├── java/
│   │   ├── controller/          # HTTP 요청 처리 (Servlet)
│   │   │   ├── Router/          # 페이지 라우팅 전용 서블릿
│   │   │   ├── Member/          # 자동 로그인 필터
│   │   │   ├── SessionServlet   # 로그인 / 로그아웃
│   │   │   ├── MemberServlet    # 회원 CRUD
│   │   │   ├── BoardServlet     # 게시판 CRUD
│   │   │   ├── ProductServlet   # 상품 CRUD
│   │   │   ├── CartServlet      # 장바구니
│   │   │   └── ScanServlet      # 파일 스캔
│   │   ├── domain/
│   │   │   ├── entity/          # Member, Board, Product, Cart, Scan, PageHandler
│   │   │   └── repository/      # Repository 인터페이스
│   │   ├── infrastructure/
│   │   │   └── persistence/     # Repository 구현체 (SQL 직접 작성)
│   │   ├── usecase/             # 비즈니스 로직 (도메인별 분리)
│   │   ├── filter/
│   │   │   ├── CsrfFilter       # CSRF 토큰 검증
│   │   │   └── SecurityHeaderFilter  # 보안 헤더 주입
│   │   └── util/
│   │       ├── DBManager        # JNDI 커넥션 풀 관리
│   │       ├── HtmlEscaper      # XSS 방어 출력 이스케이프
│   │       ├── PasswordManager  # BCrypt 해싱
│   │       └── VirusTotalService # API 연동
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── html/            # JSP 페이지
│       │   └── web.xml          # 서블릿 설정
│       ├── META-INF/context.xml # DB 커넥션 풀 설정
│       └── assets/              # CSS, JS, 이미지
├── schema.sql                   # DB 스키마 (현재 DB 기준)
├── TROUBLESHOOTING.md           # 트러블슈팅 기록
└── README.md
```

---

## 보안 구현 상세

직접 구현한 보안 기능 목록입니다. 프레임워크에 의존하지 않고 하나씩 손으로 작성했습니다.

### 1. CSRF 방어 (`CsrfFilter.java`)
- 서버 세션에 `SecureRandom` 기반 32바이트 토큰 발급
- 모든 `POST` / `PUT` / `DELETE` 요청에서 토큰 검증
- 폼 파라미터(`_csrf`) 또는 HTTP 헤더(`X-CSRF-Token`) 양쪽 지원

### 2. XSS 방어 (`HtmlEscaper.java`)
- 모든 JSP 출력부에 `HtmlEscaper.h()` 적용
- `&` `<` `>` `"` `'` → HTML 엔티티 변환

### 3. SQL Injection 방어
- 전체 쿼리 `PreparedStatement` + `?` 파라미터 바인딩
- 문자열 직접 연결 방식 사용 없음

### 4. 비밀번호 보안 (`PasswordManager.java`)
- **BCrypt** 단방향 해싱 (자동 Salt 포함)
- 비밀번호 변경 시 모든 기기 자동 로그아웃 (Remember-Me 토큰 전체 삭제)

### 5. 자동 로그인 보안 (`AutoLoginFilterServlet.java`)
- 토큰 사용 시마다 **새 UUID로 교체** (Token Rotation)
- **IP 주소 + User-Agent** 일치 검증 → 불일치 시 토큰 즉시 삭제
- `HttpOnly` + `SameSite=Strict` 쿠키 설정

### 6. 파일 업로드 3중 검증 (`ProductImageValidator.java`)
```
확장자 검사 (.jpg .jpeg .png .gif .webp)
    ↓
MIME 타입 검사 (Content-Type 헤더)
    ↓
파일 시그니처 검사 (Magic Byte)
  JPEG: FF D8 FF
  PNG:  89 50 4E 47
  GIF:  47 49 46 38
  WEBP: 52 49 46 46
```
- 저장 시 UUID 파일명으로 변환 (원본명 노출 차단)
- 최대 파일 크기: 5MB

### 7. 보안 HTTP 헤더 (`SecurityHeaderFilter.java`)
| 헤더 | 값 | 효과 |
|------|-----|------|
| X-Content-Type-Options | nosniff | MIME 스니핑 방지 |
| X-Frame-Options | SAMEORIGIN | 클릭재킹 방지 |
| X-XSS-Protection | 1; mode=block | 브라우저 XSS 필터 활성화 |
| Referrer-Policy | strict-origin-when-cross-origin | 참조자 정보 최소화 |
| Content-Security-Policy | default-src 'self' ... | 외부 리소스 차단 |
| Permissions-Policy | camera=(), microphone=(), geolocation=() | 브라우저 API 접근 차단 |

---

## DB 설계

### ERD 관계도

```
MEMBER ──┬──< BOARD          (1:N, ON DELETE CASCADE)
         ├──< CART            (1:N, ON DELETE CASCADE)
         ├──< SCAN            (1:N, ON DELETE CASCADE)
         └──< REMEMBER_ME_TOKEN (1:N, ON DELETE CASCADE)

PRODUCT ────< CART            (1:N, ON DELETE CASCADE)
```

### 테이블 설명

| 테이블 | 역할 | 주요 컬럼 |
|--------|------|-----------|
| `MEMBER` | 회원 정보 | ID, PASSWORD(BCrypt), NAME, EMAIL, PHONE, ROLE, UUID |
| `REMEMBER_ME_TOKEN` | 자동 로그인 토큰 | TOKEN_VALUE, EXPIRY_DATE, IP_ADDRESS, USER_AGENT |
| `PRODUCT` | 상품 정보 | PRODUCT_CODE, PRODUCT_NAME, PRICE, CATEGORY, DESCRIPTION(CLOB), IMAGE_NAME |
| `CART` | 장바구니 | MEMBER_ID, PRODUCT_ID, QUANTITY, PRICE |
| `BOARD` | 게시판 | MEMBER_ID, TITLE, CONTENT(CLOB), VIEW_COUNT, IP, UUID |
| `SCAN` | 스캔 이력 | MEMBER_ID, FILE_NAME, MALICIOUS, SUSPICIOUS, HARMLESS, UNDETECTED |

### UUID 전략
모든 테이블에 `RAW(16)` UUID 컬럼 (`SYS_GUID()`) 사용 — URL 노출 시 순번 기반 추측 공격 방지

### 인덱스 전략
- 자주 조회되는 컬럼 (`EMAIL`, `PHONE`, `UUID`, `PRODUCT_CODE`) — UNIQUE 인덱스
- JOIN / WHERE 복합 조건 (`CART.MEMBER_ID + PRODUCT_ID`, `SCAN.MEMBER_ID + NUM`) — 복합 인덱스
- 정렬 컬럼 (`PRODUCT.CREATED_AT DESC`) — 내림차순 인덱스

---

## API 엔드포인트

| Method | URL | 인증 | 설명 |
|--------|-----|------|------|
| GET | `/` | - | 홈 (상품 목록) |
| GET | `/login` | - | 로그인 폼 |
| GET | `/signup` | - | 회원가입 폼 |
| POST | `/sessions` | - | 로그인 처리 |
| DELETE | `/sessions` | - | 로그아웃 |
| POST | `/members/` | - | 회원가입 |
| POST | `/members/me` | ✓ 본인 | 계정 삭제 |
| POST | `/members/me/detail` | ✓ 본인 | 정보 수정 |
| POST | `/members/me/password` | ✓ 본인 | 비밀번호 변경 |
| GET | `/account` | ✓ 본인 | 프로필 페이지 |
| GET | `/dashboard` | ✓ 로그인 | 스캔 대시보드 |
| GET | `/admin` | ✓ ADMIN | 관리자 대시보드 |
| GET | `/boards/` | - | 게시판 목록 |
| GET | `/boards/{uuid}` | - | 게시글 상세 |
| POST | `/boards/` | ✓ 로그인 | 게시글 작성 |
| PUT | `/boards/{uuid}` | ✓ 작성자·ADMIN | 게시글 수정 |
| DELETE | `/boards/{uuid}` | ✓ 작성자·ADMIN | 게시글 삭제 |
| GET | `/products/{uuid}` | - | 상품 상세 |
| POST | `/products/` | ✓ ADMIN | 상품 등록 |
| PUT | `/products/{uuid}` | ✓ ADMIN | 상품 수정 |
| DELETE | `/products/{uuid}` | ✓ ADMIN | 상품 삭제 |
| GET | `/cart` | ✓ 로그인 | 장바구니 조회 |
| POST | `/cart` | ✓ 로그인 | 장바구니 담기 |
| DELETE | `/cart` | ✓ 로그인 | 장바구니 비우기 |
| GET | `/scans/new` | ✓ 로그인 | 스캔 폼 |
| POST | `/scans/` | ✓ 로그인 | 파일 스캔 요청 |
| DELETE | `/scans/{id}` | ✓ 작성자·ADMIN | 스캔 기록 삭제 |

---

## 설치 및 실행

### 사전 요구 사항
- JDK 11 이상
- Apache Tomcat 9
- Oracle XE 21c (또는 Docker)
- Maven 3.6 이상

### 1. Oracle DB 실행 (Docker)
```bash
docker run -d \
  --name oracle-xe \
  -p 1521:1521 \
  gvenzl/oracle-xe:21-slim

# 이미 컨테이너가 있는 경우
docker start oracle-xe
```

### 2. DB 스키마 초기화
```bash
# sqlplus로 접속 후 실행
sqlplus C##dbexam/m1234@localhost:1521/XEPDB1 @schema.sql
```

### 3. VirusTotal API 키 설정
```bash
# ~/.env 파일 생성
echo "VIRUSTOTAL_API_KEY=여기에_API_키_입력" >> ~/.env

# 또는 환경변수로 설정
export VIRUSTOTAL_API_KEY=여기에_API_키_입력
```

### 4. Tomcat context.xml 설정
`src/main/webapp/META-INF/context.xml`에서 DB 접속 정보 확인:
```xml
<Resource
    name="jdbc/oracle"
    url="jdbc:oracle:thin:@localhost:1521/XEPDB1"
    username="C##dbexam"
    password="m1234"
    ...
/>
```

### 5. 빌드 및 배포
```bash
mvn clean package
# target/*.war 파일을 Tomcat webapps에 복사
```

### 초기 관리자 계정
| 항목 | 값 |
|------|-----|
| ID | `admin` |
| 비밀번호 | `admin1234` |
| 권한 | ADMIN |

---

## 구현 시 고민했던 점

### 자동 로그인 보안 설계
단순히 쿠키에 사용자 ID를 저장하는 방식 대신, **UUID 토큰을 DB에 저장**하고 매 요청마다 검증하는 방식을 선택했습니다. 토큰 탈취 위험을 줄이기 위해 사용할 때마다 새 토큰으로 교체(Token Rotation)하고, IP와 User-Agent가 달라지면 즉시 무효화합니다.

### 장바구니 동시성 문제
같은 상품을 동시에 장바구니에 담을 때 중복 행이 생기는 문제를 `SELECT ... FOR UPDATE`로 해결했습니다. 해당 행에 락을 걸어 트랜잭션이 완료될 때까지 다른 요청이 접근하지 못하게 했습니다.

### 파일 업로드 보안
확장자만 검사하면 `virus.exe`를 `virus.jpg`로 바꿔서 올릴 수 있습니다. 이를 막기 위해 **파일 시그니처(Magic Byte)**를 직접 읽어서 실제 파일 형식을 확인하는 3단계 검증을 구현했습니다.

### Clean Architecture 선택
UseCase 계층을 별도로 분리해서 컨트롤러가 비즈니스 로직을 모르게 했습니다. 덕분에 같은 UseCase를 여러 컨트롤러에서 재사용할 수 있고, 로직 변경 시 컨트롤러 코드를 건드리지 않아도 됩니다.

---

## 트러블슈팅

개발 중 발생한 버그와 해결 과정은 [`TROUBLESHOOTING.md`](./TROUBLESHOOTING.md)에 상세히 정리했습니다.

주요 해결 항목:
- DB 커넥션 null 반환으로 인한 NullPointerException
- XSS 취약점 (JS 직접 출력 → sendRedirect 방식으로 변경)
- `File.renameTo()` 크로스 파티션 실패 → `Files.move()` 교체
- JSP `System.out.println` 디버그 코드 잔존 및 NPE

---

## 라이선스

본 프로젝트는 학습 및 포트폴리오 목적으로 제작되었습니다.
