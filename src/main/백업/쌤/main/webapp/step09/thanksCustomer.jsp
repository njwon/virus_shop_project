<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.ProductRepository"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="dto.Product"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <jsp:useBean id="productDAO" class="dao.ProductRepository" scope="session"/> --%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="header.jsp" %>
<title>products</title>
<style>
	html,body{
	height:3000px;
	}
</style>
</head>
<body class="blog-page">
	<%!
		DecimalFormat df = new DecimalFormat("#,###");
	%>
	<%
		String cartId = session.getId();//Jsession아이디 얻어옴
		String shipping_cartId ="";
		String shipping_name ="";
		String shipping_address ="";
		String shipping_post_num ="";
		
		//쿠키안의 모든 데이터를 얻어서 배열로 저장하고 loop를 돌려 속성값을 꺼내 변수에 저장
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(int i=0; i<cookies.length; i++){
				Cookie thisCookie = cookies[i];
				String n = thisCookie.getName();
				//out.println(n);
				
				if(n.equals("shipping_cartId")) shipping_cartId = thisCookie.getValue();
				if(n.equals("shipping_name")) shipping_name = URLDecoder.decode(thisCookie.getValue());
				if(n.equals("shipping_address")) shipping_address = URLDecoder.decode((thisCookie.getValue()), "utf-8");
				if(n.equals("shipping_post_num")) shipping_post_num = URLDecoder.decode(thisCookie.getValue());
				
			}
		}
	%>

  <%@ include file="nav.jsp" %>

  <main class="main">

    <!-- Page Title -->
    <div class="page-title dark-background" style="background-image: url(assets/img/page-title-bg.webp);">
      <div class="container position-relative">
        <h1>주문 완료</h1>
        <p>Esse dolorum voluptatum ullam est sint nemo et est ipsa porro placeat quibusdam quia assumenda numquam molestias.</p>
        <nav class="breadcrumbs">
          <ol>
            <li><a href="index.jsp">Home</a></li>
            <li class="current">orderCompleted</li>
          </ol>
        </nav>
      </div>
    </div><!-- End Page Title -->
	
	<!-- .container>.row-->
	<div class="container mt-5 pt-5">
		<div class="row">
			<div class="col">
				<h2 class="alert alert-info text-center">주문해 주셔서 감사합니다.</h2>
				<p>주문번호 : <% out.println(shipping_cartId); %></p>
				<p>송장번호 : 4561231312</p>
				<p>주문은 내일 배송예정입니다.</p>
			</div>
		</div>
	</div>
	<div class="container">
		<p>
			<a href="./products.jsp" class="btn btn-secondary">상품 목록 페이지로 바로 이동</a>
		</p>
	</div>
</main>
	<%
		//세션정보를 모두 삭제
		session.invalidate();
		//쿠키정보를 모두 삭제
		for(Cookie thisCookie : cookies){
			thisCookie.setMaxAge(0);
			response.addCookie(thisCookie);
		}
	%>
	<%@ include file="footer.jsp" %>
</body>
</html>











