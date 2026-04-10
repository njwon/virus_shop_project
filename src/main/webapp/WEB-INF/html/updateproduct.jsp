<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="domain.entity.Product"%>
<%@ page import="util.HtmlEscaper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp" %>

<c:if test="${empty sessionScope.loginUser || sessionScope.loginUser.role != 'ADMIN'}">
    <script>
        alert("로그인이 필요한 서비스입니다.");
        location.href = "${pageContext.request.contextPath}";
    </script>
    <% if(true) return; %>
</c:if>

<%
		Product product = (Product) request.getAttribute("product");
%>

<body data-page="write">
        <%@ include file="module/nav.jsp" %>

    <main class="main">
        <a href="${pageContext.request.contextPath}/admin" class="back-link">
            <span>←</span> 뒤로 가기
        </a>

        <div class="form-container">
            <h1>상품 정보 변경</h1>

            <form id="updateProductForm" action="${pageContext.request.contextPath}/products"
				name="newProduct" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="url">상품명</label>
                    <input type="text" id="pname"
						name="pname" placeholder="예: Basic 플랜" value="<%=HtmlEscaper.h(product.getPname())%>" required>
                </div>
				
				<div class="form-group">
                    <input type="hidden" name="productId" value="<%=HtmlEscaper.h(product.getProductId())%>" required>
				<input type="hidden" name="uuid" value="<%=HtmlEscaper.h(product.getUuid())%>">
                </div>
				
                <div class="form-group">
                    <label for="purpose">상품 가격</label>
                    <input type="text" id="unitPrice"
						name="unitPrice" placeholder="예: 250000" value="<%=HtmlEscaper.h(product.getUnitPrice())%>" required>
                </div>
                
                <div class="form-group">
                    <label for="purpose">상품 분류</label>
                    <input type="text" id="category"
						name="category" placeholder="예: plan" value="<%=HtmlEscaper.h(product.getCategory())%>" required>
                </div>
                
                <div class="form-group">
                    <label for="purpose">상품 설명</label>
                    <textarea id="purpose" name="description"
						placeholder="상품에 대한 자세한 설명을 넣어주세요" required><%=HtmlEscaper.h(product.getDescription())%></textarea>
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
					<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
            </form>
        </div>
    </main>
    <script>
	function submitUpdateProduct() {
		let form = document.getElementById('updateProductForm');
		let uuid = '<%=HtmlEscaper.h(product.getUuid())%>';
		let formData = new FormData(form);
		fetch('${pageContext.request.contextPath}/products/' + uuid, {
			method: 'PUT',
			headers: { 'X-CSRF-Token': '${sessionScope.csrfToken}' },
			body: formData
		}).then(res => {
			if (res.ok) { alert('상품 정보가 변경되었습니다.'); location.href = '${pageContext.request.contextPath}/admin'; }
			else { res.text().then(msg => alert(msg || '상품 정보 수정에 실패했습니다.')); }
		}).catch(() => alert('서버 오류가 발생했습니다. 다시 시도해주세요.'));
	}
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
		if(isNaN(unitPrice.value) || unitPrice.value.length==0 || parseInt(unitPrice.value)<=0){
				alert("[상품가격]양의 정수만 입력하세요.")
				unitPrice.select()
				unitPrice.focus()
				return false
		}
		if(parseInt(unitPrice.value) > 2147483647){
				alert("[상품가격]최대 2,147,483,647 이하로 입력하세요.")
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
		if (category.value.length > 20) {
			alert("[카테고리]20자 이하로 입력해주세요.")
			category.select()
			category.focus()
			return false
		}
		
		submitUpdateProduct()
		
	}
	</script>
<%@ include file="module/footer.jsp" %>