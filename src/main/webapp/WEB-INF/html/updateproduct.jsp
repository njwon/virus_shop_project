<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="dto.ProductDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp" %>

<c:if test="${empty sessionScope.loginUser || sessionScope.loginUser.role != 'ADMIN'}">
    <script>
        alert("잘못된 접근입니다.");
        location.href = "${pageContext.request.contextPath}";
    </script>
    <% if(true) return; %>
</c:if>

<%
		ProductDTO product = (ProductDTO) request.getAttribute("product");
%>

<body data-page="write">
        <%@ include file="module/nav.jsp" %>

    <main class="main">
        <a href="${pageContext.request.contextPath}/AdminBoard.do" class="back-link">
            <span>←</span> 뒤로 가기
        </a>

        <div class="form-container">
            <h1>상품 정보 변경</h1>

            <form action="${pageContext.request.contextPath}/updateProduct.do"
				name="newProduct" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="url">상품명</label>
                    <input type="text" id="pname"
						name="pname" placeholder="예: Basic 플랜" value="<%=product.getPname() %>" required>
                </div>
				
				<div class="form-group">
                    <input type="hidden" name="productId" value="<%=product.getProductId() %>" required>
                </div>
				
                <div class="form-group">
                    <label for="purpose">상품 가격</label>
                    <input type="text" id="unitPrice"
						name="unitPrice" placeholder="예: 250000" value="<%=product.getUnitPrice() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="purpose">상품 분류</label>
                    <input type="text" id="category"
						name="category" placeholder="예: plan" value="<%= product.getCategory() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="purpose">상품 설명</label>
                    <textarea id="purpose" name="description"
						placeholder="상품에 대한 자세한 설명을 넣어주세요" required><%= product.getDescription() %></textarea>
					<div class="form-hint">이용자가 상품에 대해 더 자세하게 알 수 있도록 정보를 제공하세요.</div>
                </div>
                
                <div class="form-group">
                    <label for="purpose">상품 이미지</label>
                    <input type="file"
						class="form-control" id="productImage" name="productImage">
                </div>

                <div class="form-divider">↓</div>

                <input type="button" class="form-submit" value="상품 정보 변경"
					onclick="checkProduct()">
            </form>
        </div>
    </main>
    <script>
	function checkProduct(){
		let pname = document.getElementById("pname")
		let unitPrice = document.getElementById("unitPrice")
		let category = document.getElementById("category")
		let productImage = document.getElementById("productImage")
		
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
<%@ include file="module/footer.jsp" %>