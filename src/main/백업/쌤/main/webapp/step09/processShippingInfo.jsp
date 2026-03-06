<%@page import="java.net.URLEncoder"%>
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
		request.setCharacterEncoding("utf-8");
	
		Cookie cartId = new Cookie("shipping_cartId",request.getParameter("cartId"));
		Cookie name = new Cookie("shipping_name",URLEncoder.encode(request.getParameter("name")));
		Cookie address = new Cookie("shipping_address", URLEncoder.encode(request.getParameter("address")));
		Cookie post_num = new Cookie("shipping_post_num",URLEncoder.encode(request.getParameter("post_num")));
		
		
		//생성한 쿠키의 유효기간을 24시간을 설정
		cartId.setMaxAge(60*60*24);
		name.setMaxAge(60*60*24);
		address.setMaxAge(60*60*24);
		post_num.setMaxAge(60*60*24);
		
		//쿠키를 등록
		response.addCookie(cartId);
		response.addCookie(name);
		response.addCookie(address);
		response.addCookie(post_num);
		
		//주문 목록을 볼 수 있는 주문 정보 페이지로 이동
		
		response.sendRedirect("orderConfirmation.jsp");
	%>	
		
</body>
</html>