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
        <h1>주문 정보</h1>
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
			<h1 class="col text-center">영수증</h1>
		</div>
		<div class="row">
			<div class="col-4 text-left">
				<strong>배송 주소</strong><br />
				성명 : <% out.println(shipping_name); %> <br />
				우편번호 : <% out.println(shipping_post_num); %> <br /> 
				주소 : <% out.println(shipping_address); %> <br /> 
			</div>
			<div class="col-4"></div>
			<div class="col-4 text-right">
				<strong>배송 송장번호</strong><br />
				4561231312
			</div>
		</div>
		
		<div class="row">
			<table class="table table-striped">
			  <thead>
			    <tr>
			      <th scope="col">상품</th>
			      <th scope="col">가격</th>
			      <th scope="col">수량</th>
			      <th scope="col">소계</th>
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
			      <td><%=df.format(product.getUnitPrice()) %></td>
			      <td><%=product.getQuantity() %></td>
			      <td><%=df.format(total) %></td>
			      <td></td>
			    </tr>
			    <%
			  		}
			    %>
			    <tr>
					<th></th>
					<th></th>
					<th>총액</th>
					<th><%=df.format(sum)%></th> 
					<th></th>
				</tr>
			  </tbody>
			</table>
			<a href="./shippingInfo.jsp?cartId=<%=shipping_cartId %>" class="btn btn-info">이전</a>
			<a href="./thanksCustomer.jsp" class="btn btn-warning">주문완료</a>
			<a href="./checkOutCancelled.jsp" class="btn btn-danger">취소</a>
		</div>
	</div>
</main>
	
	<%@ include file="footer.jsp" %>
</body>
</html>

