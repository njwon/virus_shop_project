# 트러블슈팅 기록

작성일: 2026-04-09

---

## 1. DB 연결 실패 시 NullPointerException

**파일:** `util/DBManager.java`

**증상:**
- Repository에서 DB 작업 중 `NullPointerException` 발생
- 커넥션 풀이 고갈되거나 DB 서버가 다운됐을 때 묵시적으로 실패

**원인:**
`getConnection()`이 SQL 예외 발생 시 `null`을 반환했고, 호출부에서 null 체크를 하지 않아 NPE 발생.

**수정 내용:**
```java
// 수정 전
} catch (SQLException e) {
    log.error("커넥션 획득 실패", e);
    return null;  // 모든 Repository에서 NPE 유발
}

// 수정 후
if (dataSource == null) {
    throw new IllegalStateException("DataSource가 초기화되지 않았습니다.");
}
} catch (SQLException e) {
    log.error("커넥션 획득 실패", e);
    throw new RuntimeException("DB 연결에 실패했습니다.", e);  // 명시적 예외로 즉시 인지 가능
}
```

**체크리스트:**
- [ ] Tomcat `context.xml`에 `jdbc/oracle` JNDI 리소스 정상 등록 여부 확인
- [ ] Oracle DB 서버 접속 가능 여부 확인
- [ ] 커넥션 풀 설정 (`maxActive`, `maxIdle`) 확인

---

## 2. XSS 취약점 - JS 직접 출력

**파일:**
- `controller/Router/LoadUserDashboardServlet.java`
- `controller/Router/LoadAdminDashboardServlet.java`

**증상:**
- 비로그인/비관리자 접근 시 alert 후 redirect하는 코드가 취약점 보유

**원인:**
`request.getContextPath()`를 `PrintWriter`로 직접 JavaScript 문자열에 삽입.
서버 설정에 따라 악의적 문자가 포함될 경우 XSS 가능성 존재.

**수정 내용:**
```java
// 수정 전
response.setContentType("text/html; charset=UTF-8");
PrintWriter out = response.getWriter();
out.println("<script>alert('로그인 후 접근 가능합니다.'); location.href='" + request.getContextPath() + "/login';</script>");

// 수정 후
response.sendRedirect(request.getContextPath() + "/login?error=auth");
```

**참고:**
- 로그인 페이지에서 `error=auth` 파라미터를 보고 안내 메시지를 표시하도록 login.jsp 수정 고려
- 관리자 페이지는 `?error=forbidden`으로 리다이렉트

---

## 3. 파일 업로드 실패 감지 안 됨

**파일:**
- `controller/ProductServlet.java` (Line 157)
- `controller/ScanServlet.java` (Line 82)

**증상:**
- 파일 업로드 후 이미지가 없거나 스캔 파일이 없어도 오류 메시지 없음
- 크로스 파티션 이동 시 `renameTo()`가 실패해도 false만 반환하고 무시됨

**원인:**
`File.renameTo()`는 실패 시 `false`를 반환하지만 반환값을 확인하지 않음.
특히 OS가 다른 파일시스템 간 이동을 지원하지 않을 때 항상 실패.

**수정 내용:**
```java
// 수정 전
src.renameTo(new File(folder, uuidName));  // 실패해도 알 수 없음

// 수정 후
Files.move(src.toPath(), new File(folder, uuidName).toPath(), StandardCopyOption.REPLACE_EXISTING);
// 실패 시 IOException 발생 → 상위에서 catch하여 에러 응답
```

---

## 4. VirusTotalService API 키 로딩 실패 묵살

**파일:** `util/VirusTotalService.java`

**증상:**
- `.env` 파일이 없거나 읽기 오류 시 조용히 무시
- 이후 API 호출에서 인증 오류가 나도 원인 파악 어려움

**원인:**
```java
} catch (Exception ignored) {}  // 에러를 완전히 무시
```

**수정 내용:**
```java
} catch (Exception e) {
    log.debug(".env 파일을 읽을 수 없습니다: {}", envPath);
}
```

**참고:**
- 두 경로 모두 실패하면 line 38의 `throw new IllegalStateException()`으로 서버 시작 시 즉시 오류 발생 → 설계상 의도된 동작
- `.env` 파일 위치: `${catalina.home}/.env` 또는 `${user.home}/.env`

---

## 5. cart.jsp - 디버그 코드 잔존 및 NPE

**파일:** `webapp/WEB-INF/html/cart.jsp`

**증상:**
- 서버 로그에 `DEBUG:`, `데이터 타입:` 등 불필요한 출력 발생
- `test.getClass().getName()` 호출 시 `total` 어트리뷰트가 null이면 NPE

**원인:**
```java
Object test = request.getAttribute("total");
System.out.println("데이터 타입: " + test.getClass().getName());  // test가 null이면 NPE
```

**수정 내용:**
- `System.out.println` 전부 제거
- 불필요한 중복 변수(`test`, `totalObj`) 정리
- `instanceof` 조건 개선 (null 안전)

---

## 6. profile.jsp - null 체크 누락 및 파일 상단 오타

**파일:** `webapp/WEB-INF/html/profile.jsp`

**증상:**
- 세션 없이 `/profile` 직접 접근 시 `user.getName()`에서 NPE로 500 에러
- 파일 최상단에 `ss` 오타로 인해 JSP 파싱 오류 가능

**원인:**
- `Member user = (Member) request.getAttribute("user");` 후 null 체크 없이 바로 `user.getName()` 호출
- 파일 첫 줄에 `ss` 잔존 (아마 실수로 입력된 문자)

**수정 내용:**
```java
// 수정 전
Member user = (Member) request.getAttribute("user");
String name = user.getName();  // NPE 가능

// 수정 후
Member user = (Member) request.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}
String name = user.getName();
```

---

## 7. CartServlet - 음수/0 수량 허용

**파일:** `controller/CartServlet.java`

**증상:**
- `quantity=0` 또는 `quantity=-1` 파라미터로 장바구니에 비정상 수량 추가 가능

**수정 내용:**
```java
int quantity = (quantityStr != null && !quantityStr.isEmpty()) ? Integer.parseInt(quantityStr) : 1;
if (quantity <= 0) quantity = 1;  // 추가
```

---

## 미수정 사항 (추후 검토)

### CSP unsafe-inline 제거
**파일:** `filter/SecurityHeaderFilter.java`

현재 `'unsafe-inline'`이 script-src와 style-src에 설정되어 있어 CSP의 XSS 방어 효과가 반감됨.
JSP 파일 전체에 인라인 스크립트가 광범위하게 사용 중이므로 제거하려면 모든 인라인 JS를 외부 파일로 분리해야 함.

**권장:** 장기적으로 인라인 JS를 `static/js/*.js` 파일로 분리 후 `unsafe-inline` 제거

---

## 수정 파일 목록

| 파일 | 수정 내용 |
|------|-----------|
| `util/DBManager.java` | `getConnection()` null 반환 → RuntimeException throw |
| `controller/Router/LoadUserDashboardServlet.java` | XSS 취약 JS 출력 → sendRedirect |
| `controller/Router/LoadAdminDashboardServlet.java` | XSS 취약 JS 출력 → sendRedirect |
| `controller/ProductServlet.java` | `renameTo()` → `Files.move()` |
| `controller/ScanServlet.java` | `renameTo()` → `Files.move()` |
| `util/VirusTotalService.java` | 예외 묵살 → debug 로깅 |
| `webapp/WEB-INF/html/cart.jsp` | 디버그 println 제거, NPE 수정 |
| `webapp/WEB-INF/html/profile.jsp` | 상단 오타 제거, null 체크 추가 |
| `controller/CartServlet.java` | 수량 음수/0 방어 추가 |
