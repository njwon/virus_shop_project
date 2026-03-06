<%@ page import="dto.ProductDTO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp" %>
<body data-page="article">
    <%@ include file="module/nav.jsp" %>

	<%
		ProductDTO product = (ProductDTO) request.getAttribute("product");
	%>
<main class="main">
<a href="<%=request.getContextPath()%>" class="back-link">
<span>←</span> 상품 목록으로
</a>
<div class="article-header">
<div class="article-meta">
<span id="articleDate"><%=product.getSimpleDate() %></span> 
<span class="status-badge" id="status"><%= product.getCategory() %></span>
</div>
<%if(product.getProductImage() != null) {%>    
<img src="<%= request.getContextPath() %>/resources/images/<%= product.getProductImage() %>" style="max-width: 100%;">
<%} %>
<h1 id="articleTitle"><%=product.getPname() %></h1>
<div class="url-display" id="urlDisplay" style="background:none; border:none;"><%=product.getUnitPrice() %>원</div>
<div class="description" id="description"><%=product.getDescription() %></div>
<form action="${pageContext.request.contextPath}/addCart.do" name="newCartProduct" method="post">
	<input type="hidden" name="productId" value="<%= product.getProductId() %>"> 
	<input type="hidden" name="pname"  value="<%= product.getPname() %>">
	<input type="hidden" name="unitPrice"  value="<%= product.getUnitPrice() %>">
	<input type="hidden" name="category"  value="<%= product.getCategory() %>">
	<input type="hidden" name="description"  value="<%= product.getDescription() %>">
	<input type="submit" class="form-submit" value="구매하기">
</form>
</div>
</main> 
<%@ include file="module/footer.jsp" %>