<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="module/header.jsp" %>
<body data-page="error">
    <main class="main">
        <section class="welcome-section">
            <h1><%= response.getStatus() %></h1>
            <p>요청하신 페이지를 찾을 수 없거나 접근할 수 없습니다.</p>
            <a href="<%= request.getContextPath() %>" class="btn btn-primary" style="margin-top:1.5rem;">홈으로 돌아가기</a>
        </section>
    </main>
    <%@ include file="module/footer.jsp" %>
</body>
