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
	%>

  <%@ include file="nav.jsp" %>

  <main class="main">

    <!-- Page Title -->
    <div class="page-title dark-background" style="background-image: url(assets/img/page-title-bg.webp);">
      <div class="container position-relative">
        <h1>주문 취소</h1>
        <p>Esse dolorum voluptatum ullam est sint nemo et est ipsa porro placeat quibusdam quia assumenda numquam molestias.</p>
        <nav class="breadcrumbs">
          <ol>
            <li><a href="index.jsp">Home</a></li>
            <li class="current">orderCancelled</li>
          </ol>
        </nav>
      </div>
    </div><!-- End Page Title -->
	
	<!-- .container>.row-->
	<div class="container mt-5 pt-5">
		<div class="row">
			<h2 class="alert alert-danger text-center">주문이 취소되었습니다.</h2>
		</div>
	</div>
	<div class="container">
		<p>
			<a href="./products.jsp" class="btn btn-secondary">상품 목록 페이지로 바로 이동</a>
		</p>
	</div>
</main>
	
	<%@ include file="footer.jsp" %>
</body>
</html>