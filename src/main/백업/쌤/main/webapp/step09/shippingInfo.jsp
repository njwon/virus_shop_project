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

  <%@ include file="nav.jsp" %>

  <main class="main">

    <!-- Page Title -->
    <div class="page-title dark-background" style="background-image: url(assets/img/page-title-bg.webp);">
      <div class="container position-relative">
        <h1>배송 정보</h1>
        <p>Esse dolorum voluptatum ullam est sint nemo et est ipsa porro placeat quibusdam quia assumenda numquam molestias.</p>
        <nav class="breadcrumbs">
          <ol>
            <li><a href="index.jsp">Home</a></li>
            <li class="current">Blog</li>
          </ol>
        </nav>
      </div>
    </div><!-- End Page Title -->
	
	<!-- .container>.row>.col-md-6>h3+p+p*4>b^h3+p -->
	<div class="container mt-5 pt-5">
		<div class="row">
			<form action="./processShippingInfo.jsp" method="post">
			  <input type="hidden" name="cartId" value='<%=request.getParameter("cartId") %>'/>
			  <div class="form-group my-3">
			    <label for="name">성명</label>
			    <input type="text" class="form-control" id="name" name="name">
			  </div>
			  <div class="form-group my-3">
			    <label for="address">배송주소</label>
			    <input type="text" class="form-control" id="address" name="address">
			  </div>
			  <div class="form-group my-3">
			    <label for="post_num">우편번호</label>
			    <input type="text" class="form-control" id="post_num" name="post_num">
			  </div>
			  <a href="./cart.jsp?cartId=<%=request.getParameter("cartId") %>" class="btn btn-secondary">이전</a>
			  <input type="submit" class="btn btn-primary" value="등록">
			</form>
		</div>
	</div>
</main>
	
	<%@ include file="footer.jsp" %>
</body>
</html>