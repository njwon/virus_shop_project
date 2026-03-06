<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		//세션정보를 모두 삭제
		session.invalidate();
		//쿠키정보를 모두 삭제
		Cookie[] cookies = request.getCookies();
		for(Cookie thisCookie : cookies){
			thisCookie.setMaxAge(0);
			response.addCookie(thisCookie);
		}
		response.sendRedirect("./cart.jsp");
	%>
</body>
</html>