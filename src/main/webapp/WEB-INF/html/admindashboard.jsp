<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp"%>
<%
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy.MM.dd");
java.util.Date now = new java.util.Date();
String today = sdf.format(now);
%>
<body data-page="dashboard">
	<%@ include file="/WEB-INF/html/module/nav.jsp"%>

	<main class="main">
		<section class="welcome-section">
			<h1>관리자 대시보드</h1>
			<p>상품 전체를 관리 및 제어하세요</p>
		</section>

		<div class="stats-grid">
			<div class="stat-card">
				<h3>상품</h3>
				<div class="stat-number"><%=request.getAttribute("productCount") %></div>
			</div>
			<div class="stat-card info">
				<h3>관리자</h3>
				<div class="stat-number"><%=request.getAttribute("accountName") %></div>
			</div>
		</div>
		<section class="admin-section">
			<h2>상품 글 작성</h2>
			<form action="${pageContext.request.contextPath}/addProduct.do"
				name="newProduct" method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="title">상품 코드</label> <input type="text" id="productId"
						name="productId" placeholder="예: P123456" required>
				</div>

				<div class="form-group">
					<label for="title">상품명</label> <input type="text" id="pname"
						name="pname" placeholder="예: Basic 플랜" required>
				</div>

				<div class="form-group">
					<label for="title">상품 가격</label> <input type="text" id="unitPrice"
						name="unitPrice" placeholder="예: 250000" required>
				</div>

				<div class="form-group">
					<label for="title">상품 분류</label> <input type="text" id="category"
						name="category" placeholder="예: plan" required>
				</div>

				<div class="form-group">
					<label for="purpose">상품 설명</label>
					<textarea id="purpose" name="description"
						placeholder="상품에 대한 자세한 설명을 넣어주세요" required></textarea>
					<div class="form-hint">이용자가 상품에 대해 더 자세하게 알 수 있도록 정보를 제공하세요.</div>
				</div>

				<div class="form-group">
					<label for="title">상품 이미지</label> <input type="file"
						class="form-control" id="productImage" name="productImage">
				</div>

				<input type="hidden" name="date" value="<%=today%>">

				<div class="form-divider">↓</div>

				<input type="button" class="form-submit" value="등록"
					onclick="checkAddProduct()">
			</form>
		</section>

		<section class="admin-section">
			<h2>모든 상품 관리</h2>
			<%@ include file="includes/productList.jsp" %>	
		</section>
	</main>
	<script>
	const urlParams = new URLSearchParams(window.location.search);
    const message = urlParams.get('msg');
    if (message === 'updateproductsucceeded') {
        alert("상품 정보가 성공적으로 변경되었습니다!");
        history.replaceState(null, '', window.location.pathname);
    } else if (message === 'delaccountsucceeded') {
        alert("상품이 성공적으로 삭제되었습니다!");
        history.replaceState(null, '', window.location.pathname);
    }
	
	function checkAddProduct(){
		let productId = document.getElementById("productId")
		let pname = document.getElementById("pname")
		let unitPrice = document.getElementById("unitPrice")
		let category = document.getElementById("category")
		
		/*
		[1]productId:첫글자를 반드시 P로 시작하고 숫자를 조합해서 3-10자리까지 입력
		[2]pname:최소 3자리 최대 12자리까지 입력
		[3]unitPrice: 숫자만 입력, 음수 입력X
		[4]manufacturer, category: 입력이나 선택 필수
		[5]unitsInStock: 숫자만 입력
		[6]productImage: 등록 필수
		*/
		
		function check(regExp, e, msg){
			if(regExp.test(e.value)){
				return true
			}else{
				alert(msg)
				return false
			}
		}
		
		//productId
		if(!check(/^P[0-9]{2,9}$/,productId,"[상품코드]첫글자를 반드시 P로 시작하고 숫자를 조합해서 3-10자리까지 입력해주세요")) {
			productId.select()
			productId.focus()
			return false
			}
		
		//pname
		if(pname.value.length<3 || pname.value.length>12){
			alert("[상품명]최소 3자리 최대 12자리까지 입력하세요.")
			pname.select()
			pname.focus()
			return false
		}
		
		//unitPrice
		if(isNaN(unitPrice.value) || unitPrice.value.length==0 || unitPrice.value<0){
				alert("[상품가격]양의 정수만 입력하세요.")
				unitPrice.select()
				unitPrice.focus()
				return false
		}
		
		//category
		if (!category.value) {
			alert("[카테고리]필수 입력사항입니다.")
			category.select()
			category.focus()
			return false
		}
		
		document.newProduct.submit()
		
	}
	</script>
	<%@ include file="/WEB-INF/html/module/footer.jsp"%>