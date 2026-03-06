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
        <h1>장바구니</h1>
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
			<table class="table table-striped">
			  <thead>
			    <tr>
			      <th scope="col">상품</th>
			      <th scope="col">가격</th>
			      <th scope="col">수량</th>
			      <th scope="col">소계</th>
			      <th scope="col">비고</th>
			    </tr>
			  </thead>
			  <tbody>
			  	<%
			  		int sum = 0; //결제총액
			  		ArrayList<Product> cartList = (ArrayList<Product>)session.getAttribute("cartlist");
			  		
			  		if(cartList == null) cartList = new ArrayList<Product>();
			  		
			  		for(int i=0; i<cartList.size(); i++){
			  			Product product = cartList.get(i);
			  			int total = product.getUnitPrice() * product.getQuantity();
			  			sum += total;
			  		
			  	%>
			    <tr>
			      <th scope="row"><%=product.getProductId() %> | <%=product.getPname() %></th>
			      <td><%=product.getUnitPrice() %></td>
			      <td><%=product.getQuantity() %></td>
			      <td><%=total %></td>
			      <td><a href="#" class="btn btn-danger">삭제</a></td>
			    </tr>
			    <%
			  		}
			    %>
			    <tr>
					<th></th>
					<th></th>
					<th>총액</th>
					<th><%=sum%></th> 
					<th></th>
				</tr>
			  </tbody>
			</table>
		</div>
	</div>
</main>
	
	<%@ include file="footer.jsp" %>
</body>
</html>