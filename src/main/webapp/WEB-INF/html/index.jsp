<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy.MM.dd");
    java.util.Date now = new java.util.Date();
    String today = sdf.format(now);
    today = today.replace(".", "<span class=\"dot\">.</span>");
%>
<%@ include file="module/header.jsp" %>
<body data-page="index">
    <%@ include file="module/nav.jsp" %>
    <section class="hero">
        <div class="hero-content">
            <h1 class="hero-title">
                취약점
                <span class="accent">스캐너</span>
            </h1>
            <h2 class="hero-subtitle">
                반갑습니다!
            </h2>
            <p class="hero-subtitle">
                악성코드 보안 분석으로 취약점을 사전에 발견하고 해결하세요
            </p>
            <div class="hero-cta">
                <a href="<%=request.getContextPath()%>/write" class="btn btn-primary">새 스캔 시작</a>
                <a href="<%=request.getContextPath()%>/UserBoard.do" class="btn btn-safe">대시보드 보기</a>
            </div>
        </div>
    </section>

<%@ include file="includes/products.jsp" %>

	<h1 class="onday"><%= today %></h1>
<script>
    const urlParams = new URLSearchParams(window.location.search);
    const message = urlParams.get('msg');

    if (message === 'signupsucceeded') {
        alert("회원가입이 성공적으로 완료되었습니다!");
        history.replaceState(null, '', window.location.pathname);
    } else if(message === 'loginsucceeded') {
    	alert("로그인이 성공적으로 완료되었습니다!");
        history.replaceState(null, '', window.location.pathname);
    } else if(message === 'logoutsucceeded') {
    	alert("로그아웃이 성공적으로 완료되었습니다!");
        history.replaceState(null, '', window.location.pathname);
    } else if(message === 'purchasesucceeded') {
    	alert("제품 구입이 성공적으로 완료되었습니다!");
        history.replaceState(null, '', window.location.pathname);
    } else if(message === 'accountdeletesucceeded') {
    	alert("계정 삭제가 성공적으로 완료되었습니다!");
        history.replaceState(null, '', window.location.pathname);
    }
</script>
<%@ include file="module/footer.jsp" %>