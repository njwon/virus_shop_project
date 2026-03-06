<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	input[type="file"]::-webkit-file-upload-button {
	  visibility: hidden; 
	}
	
</style>
</head>
<body>
	
	<%@ include file="menu.jsp" %>
	<fmt:setLocale value='<%=request.getParameter("language") %>'/>
	<fmt:bundle basename="bundle.message">
		<div class="jumbotron">
			<div class="container">
				<h1 class="display-3"><fmt:message key="title"/> </h1>
			</div>
		</div>
		<div class="container">
			<div class="row">
				<div class="col">
					<a href="?language=ko">korean</a>|<a href="?language=en">English</a>
				</div>
			</div>
			<div class="row">
				<div class="col">
					<form action="./processAddProduct.jsp" name="newProduct" method="post" enctype="multipart/form-data">
						<div class="row mb-3">
							<label for="productId" class="col-sm-2 col-form-label"><fmt:message key="productId"/></label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="productId" name="productId">
							</div>
						</div>
						<div class="row mb-3">
							<label for="pname" class="col-sm-2 col-form-label"><fmt:message key="pname"/></label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="pname" name="pname">
							</div>
						</div>
						<div class="row mb-3">
							<label for="unitPrice" class="col-sm-2 col-form-label"><fmt:message key="unitPrice"/></label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="unitPrice" name="unitPrice">
							</div>
						</div>
						<div class="row mb-3">
							<label for="productId" class="col-sm-2 col-form-label"><fmt:message key="description"/></label>
							<div class="col-sm-10">
								<textarea class="form-control" placeholder='<fmt:message key="textarea"/>' id="floatingTextarea2" style="height: 100px" name="description"></textarea>
							</div>
						</div>
						<div class="row mb-3">
							<label for="manufacturer" class="col-sm-2 col-form-label"><fmt:message key="manufacturer"/></label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="manufacturer" name="manufacturer">
							</div>
						</div>
						<div class="row mb-3">
							<label for="category" class="col-sm-2 col-form-label"><fmt:message key="category"/></label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="category" name="category">
							</div>
						</div>
						<div class="row mb-3">
							<label for="unitsInStock" class="col-sm-2 col-form-label"><fmt:message key="unitsInStock"/></label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="unitsInStock" name="unitsInStock">
							</div>
						</div>
						<div class="row mb-3">
							<label for="condition" class="col-sm-2 col-form-label"><fmt:message key="condition"/></label>
							<div class="col-sm-10">
								<input class="form-check-input" type="radio" name="condition" id="condition" checked value="new"><fmt:message key="condition_new"/> &nbsp;&nbsp;&nbsp;&nbsp;
								<input class="form-check-input" type="radio" name="condition" id="condition" value="old"><fmt:message key="condition_old"/> &nbsp;&nbsp;&nbsp; 
								<input class="form-check-input" type="radio" name="condition" id="condition" value="recycling"><fmt:message key="condition_recycle"/>  
							</div>
						</div>
						<div class="row mb-3">
							<label for="productImage" class="col-sm-2 col-form-label"><fmt:message key="image"/></label>
							<div class="col-sm-10">
								<label for="productImage" class="btn btn-dark"><fmt:message key="chooseFile"/></label>
								<input type="file" class="form-control" id="productImage" name="productImage">
							</div>
						</div>
						<div class="row">
							<div class="col">
								<input type="submit" class="form-control btn-info" value='<fmt:message key="submit"/>'>
								<input type="reset" value='<fmt:message key="cancel"/>'  class="form-control btn-danger" />
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</fmt:bundle>
	<%@ include file="footer.jsp" %>
</body>
</html>