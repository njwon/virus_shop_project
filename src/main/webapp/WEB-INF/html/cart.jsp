<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
System.out.println("DEBUG: " + request.getAttribute("unitPrice"));
Object test = request.getAttribute("total");
System.out.println("데이터 타입: " + test.getClass().getName());
System.out.println("데이터 내용: " + test);
Object totalObj = request.getAttribute("total");
int totalSum = 0;

if (totalObj != null) {
	if (totalObj instanceof String) {
		totalSum = Integer.parseInt((String) totalObj);
	} else {
		totalSum = ((Number) totalObj).intValue();
	}
}

int tax = (int) (totalSum * 0.1);
int finalTotal = (int) (totalSum * 1.1);

System.out.println("최종 계산 결과: " + finalTotal);
%>
<body data-page="cart">
	<div class="grid-bg"></div>

	<%@ include file="module/nav.jsp"%>

	<main class="main">
		<section class="welcome-section">
			<h1>장바구니</h1>
			<p>선택한 상품를 확인하고 구매하세요</p>
		</section>
		<div class="cart-container">
			<div class="cart-items">
				<h2 class="cart-title">선택한 항목</h2>
				<div id="cartItemsList">
					<div id="cartItemsList">
						<c:forEach var="cart" items="${cartList}">
							<div class="inquiry-item"
								style="pointer-events: none; margin-bottom: 10px;">
								<div class="inquiry-header">
									<h3 class="inquiry-title">${cart.pname}</h3>
									<span class="inquiry-category">${cart.quantity}개</span>
								</div>
								<div class="inquiry-meta">
									<span class="inquiry-author"><span class="price-format">${cart.unitPrice}</span>원</span>
								</div>
							</div>
						</c:forEach>
						<c:if test="${empty cartList}">
							<h3 class="inquiry-title" style="text-align: center;">장바구니가 비어있습니다.</h3>
						</c:if>
					</div>
				</div>
			</div>

			<div class="cart-summary">
				<h2 class="cart-title">주문 요약</h2>
				<div class="summary-item">
					<span>소계</span> <span id="subtotal">₩ <%=String.format("%,d", totalSum)%>
				</div>
				<div class="summary-item">
					<span>부가세 (10%)</span> <span id="tax">₩ <%=String.format("%,d", tax)%></span>
				</div>
				<div class="summary-divider"></div>
				<div class="summary-total">
					<span>총 금액</span> <span id="total">₩ <%=String.format("%,d", finalTotal)%></span>
				</div>

				<button class="btn btn-primary"
					style="width: 100%; margin-top: 1.5rem;"
					onclick="
							<c:if test="${not empty cartList}">
							if(confirm('결제를 진행하시겠습니까? 장바구니가 비워집니다.')) { location.href='${pageContext.request.contextPath}/clearCart.do'; }
							</c:if>
							<c:if test="${empty cartList}">
							alert('로그인 후 진행가능합니다.'); location.href='${pageContext.request.contextPath}/login'; 
							</c:if>">결제하기</button>
				<a href="<%=request.getContextPath()%>" class="btn btn-safe"
					style="width: 100%; margin-top: 1rem; text-align: center;">계속
					쇼핑하기</a>
			</div>
		</div>
	</main>
	<script>
    	document.querySelectorAll('.price-format').forEach(el => {
        	const value = parseInt(el.innerText);
        	if (!isNaN(value)) {
            	// 숫자를 현지 통화 형식(콤마 포함)으로 변환
            	el.innerText = value.toLocaleString();
        	}
    	});
	</script>
	<%@ include file="module/footer.jsp"%>