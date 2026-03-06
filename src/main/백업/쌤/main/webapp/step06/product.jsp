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
        <h1>상품상세정보</h1>
        <p>Esse dolorum voluptatum ullam est sint nemo et est ipsa porro placeat quibusdam quia assumenda numquam molestias.</p>
        <nav class="breadcrumbs">
          <ol>
            <li><a href="index.jsp">Home</a></li>
            <li class="current">Blog</li>
          </ol>
        </nav>
      </div>
    </div><!-- End Page Title -->
	<%
		String id = request.getParameter("id");
		ProductRepository dao = ProductRepository.getInstance();
		Product product = dao.getProductById(id);
	%>
	<!-- .container>.row>.col-md-6>h3+p+p*4>b^h3+p -->
	<div class="container mt-5 pt-5">
		<div class="row">
			<div class="col-md-5">
				<img src="../resources/images/<%=product.getFilename() %>" alt="" class="mb-5 img-fluid"/>
			</div>
			<div class="col-md-6">
				<h3><%=product.getPname() %></h3>
				<p><%=product.getDescription()%>
				<p><b>상품 코드 : </b><span class="badge badge-danger"> <%=product.getProductId()%></span>
				<p><b>제조사</b> : <%=product.getManufacturer()%>
				<p><b>분류</b> : <%=product.getCategory()%>
				<%
					DecimalFormat formatter = new DecimalFormat("#,###");
				%>
				<p><b>재고 수</b> : <%=formatter.format(product.getUnitsInStock())%>
				
				<h3><%=formatter.format(product.getUnitPrice())  %>원</h3>
				<p>
					<a href="#" class="btn btn-info"> 상품 주문 &raquo;</a>
					<a href="./products.jsp" class="btn btn-secondary"> 상품 목록 &raquo;</a>
					<form action="./addCart.jsp?id=<%=product.getProductId() %>" name="addForm" method="post">
						<input class="btn btn-warning" onclick="addToCart()" value="장바구니에 담기 &raquo;">
						<a href="./cart.jsp" class="btn btn-primary">장바구니로 이동</a>
					</form>
			</div>
		</div>
	</div>
</main>
	
	<%@ include file="footer.jsp" %>
	<script>
		function addToCart(){
			if(confirm("상품을 장바구니에 추가하시겠습니까?")){
				document.addForm.submit();
			}else{
				document.addForm.reset();
			}
		}
	</script>
</body>
</html>