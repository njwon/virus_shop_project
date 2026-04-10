<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="domain.entity.Member"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="grid-bg"></div>

<nav class="nav">
	<div class="nav-container">
		<a href="<%=request.getContextPath()%>" class="logo">SecureWeb</a>
		<button class="mobile-menu-btn" onclick="toggleMobileMenu()"
			aria-label="메뉴">
		</button>
		<div class="nav-links" id="navLinks">
			<c:if test="${not empty sessionScope.loginUser}">
					<a href="<%=request.getContextPath()%>" class="nav-link">
						홈
					</a>
    			<a href="<%=request.getContextPath()%>/scans/new" class="nav-link">
					새 스캔
				</a>
    			<a href="<%=request.getContextPath()%>/dashboard" class="nav-link">
					대시보드
				</a>
    			<a href="<%=request.getContextPath()%>/cart" class="nav-link">
					장바구니
				</a>
    			<a href="<%=request.getContextPath()%>/boards" class="nav-link">
					게시판
				</a>
			</c:if>
    		<c:if test="${not empty sessionScope.loginUser and sessionScope.loginUser.role == 'ADMIN'}">
    			<a href="${pageContext.request.contextPath}/admin" class="nav-link">
        			관리자 대시보드
    			</a>
			</c:if>
			<c:choose>
				<c:when test="${not empty sessionScope.loginUser}">
					<div style="text-align: center;">
						<p><c:out value="${sessionScope.loginUser.name}"/>님 환영합니다!</p>
						<a href="${pageContext.request.contextPath}/account" class="btn btn-safe" style="padding: 0.1rem 1rem;">마이페이지</a>
					</div>
				</c:when>
				<c:otherwise>
					<a href="<%=request.getContextPath()%>/signup"
						class="btn btn-safe">회원가입</a> 
					<a href="<%=request.getContextPath()%>/login"
						class="btn btn-primary">로그인</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</nav>
