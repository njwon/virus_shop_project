<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="menu.jsp" %>
	
	<div class="jumbotron">
		<div class="container">
			<h1 class="display-3">상품 등록</h1>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col">
				<form action="./processAddProduct.jsp" name="newProduct" method="post" enctype="multipart/form-data">
					<div class="row mb-3">
						<label for="productId" class="col-sm-2 col-form-label">상품 코드</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="productId" name="productId">
						</div>
					</div>
					<div class="row mb-3">
						<label for="pname" class="col-sm-2 col-form-label">상품명</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="pname" name="pname">
						</div>
					</div>
					<div class="row mb-3">
						<label for="unitPrice" class="col-sm-2 col-form-label">상품가격</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="unitPrice" name="unitPrice">
						</div>
					</div>
					<div class="row mb-3">
						<label for="productId" class="col-sm-2 col-form-label">상품 설명</label>
						<div class="col-sm-10">
							<textarea class="form-control" placeholder="상품에 대한 자세한 설명을 넣어주세요" id="floatingTextarea2" style="height: 100px" name="description"></textarea>
						</div>
					</div>
					<div class="row mb-3">
						<label for="manufacturer" class="col-sm-2 col-form-label">제조사</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="manufacturer" name="manufacturer">
						</div>
					</div>
					<div class="row mb-3">
						<label for="category" class="col-sm-2 col-form-label">상품 분류</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="category" name="category">
						</div>
					</div>
					<div class="row mb-3">
						<label for="unitsInStock" class="col-sm-2 col-form-label">재고 수</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="unitsInStock" name="unitsInStock">
						</div>
					</div>
					<div class="row mb-3">
						<label for="condition" class="col-sm-2 col-form-label">제품상태</label>
						<div class="col-sm-10">
							<input class="form-check-input" type="radio" name="condition" id="condition" checked value="new">신상품 &nbsp;&nbsp;&nbsp;&nbsp;
							<input class="form-check-input" type="radio" name="condition" id="condition" value="old">중고품 &nbsp;&nbsp;&nbsp; 
							<input class="form-check-input" type="radio" name="condition" id="condition" value="recycling">재활용품  
						</div>
					</div>
					<div class="row mb-3">
						<label for="productImage" class="col-sm-2 col-form-label">제품 이미지 등록</label>
						<div class="col-sm-10">
							<input type="file" class="form-control" id="productImage" name="productImage">
						</div>
					</div>
					<div class="row">
						<div class="col">
							<input type="button" class="form-control btn-info" value="등록" onclick="checkAddProduct()">
							<input type="reset" value="취소"  class="form-control btn-danger" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<%@ include file="footer.jsp" %>
	<script src="../assets/js/form_validation.js"></script>
</body>
</html>