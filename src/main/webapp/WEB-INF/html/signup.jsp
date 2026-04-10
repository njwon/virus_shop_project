<%@ page import="util.HtmlEscaper"%>
<%@ page import="java.util.ArrayList"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<body data-page="signup">
	<div class="grid-bg"></div>
	<div class="glow"></div>

	<div class="container">
		<a href="<%=request.getContextPath()%>" class="back-link"> <span>←</span>
			뒤로 가기
		</a>

		<div class="form-header">
			<h1>회원가입</h1>
			<p class="form-subtitle">새 계정을 만드세요</p>
		</div>

		<form action="${pageContext.request.contextPath}/members/"
			name="newMember" method="post">
			<div class="form-group">
				<label for="id">아이디</label> <input type="text" class="form-control"
					id="id" name="id" placeholder="Mrhong" required>
			</div>
			<div class="form-group">
				<label for="name">이름</label> <input type="text" class="form-control"
					id="name" name="name" placeholder="홍길동" required>
			</div>
			<div class="form-group">
				<label for="email">이메일</label> <input type="email"
					class="form-control" name="mail" placeholder="your@email.com"
					required>
			</div>
			<div class="form-group">
				<label for="phone">연락처</label> <input type="text"
					class="form-control" id="phone" name="phone"
					placeholder="010-0000-0000">
			</div>
			<div class="form-group">
				<label for="pw">비밀번호</label> <input type="password"
					class="form-control" id="pw" name="pw" placeholder="••••••••"
					required>
			</div>
			<div class="form-group">
				<label for="pw_confirm">비밀번호 확인</label> <input type="password"
					id="pw_confirm" name="pw_confirm" placeholder="••••••••" required>
			</div>
			<input type="button" class="form-control form-submit" value="회원가입"
				onclick="checkForm()" />
			<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
		</form>
		<div class="form-footer">
			이미 계정이 있으신가요? <a href="login.jsp">로그인</a>
		</div>
	</div>
	<%
		String errorMsg = (String) request.getAttribute("msg");
		if (errorMsg != null && !errorMsg.isEmpty()) {
	%>
	<div id="_signupSrvMsg" style="display:none"><%=HtmlEscaper.h(errorMsg)%></div>
	<script>(function(){ var m=document.getElementById('_signupSrvMsg'); if(m) alert(m.textContent); })();</script>
	<% } %>
	<script>
		function checkForm() {
			const f = document.newMember;
			const id    = f.id.value.trim();
			const pw    = f.pw.value;
			const pwCf  = f.pw_confirm.value;
			const name  = f.name.value.trim();
			const mail  = f.mail.value.trim();
			const phone = f.phone.value.trim();

			if (!id) { alert("아이디를 입력해주세요."); return false; }
			if (!/^[a-zA-Z0-9]{4,20}$/.test(id)) {
				alert("아이디는 영문/숫자 4~20자로 입력해주세요."); return false;
			}
			if (!name) { alert("이름을 입력해주세요."); return false; }
			if (name.length < 2 || name.length > 20) {
				alert("이름은 2~20자로 입력해주세요."); return false;
			}
			if (!mail) { alert("이메일을 입력해주세요."); return false; }
			if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(mail)) {
				alert("올바른 이메일 형식으로 입력해주세요."); return false;
			}
			if (mail.length > 50) { alert("이메일은 50자 이하로 입력해주세요."); return false; }
			if (!phone) { alert("전화번호를 입력해주세요."); return false; }
			if (!/^[0-9]{10,11}$/.test(phone.replaceAll("-", ""))) {
				alert("올바른 전화번호 형식으로 입력해주세요. (예: 010-1234-5678)"); return false;
			}
			if (!pw) { alert("비밀번호를 입력해주세요."); return false; }
			if (pw.length < 8 || pw.length > 50) {
				alert("비밀번호는 8자 이상 50자 이하로 입력해주세요."); return false;
			}
			if (!pwCf) { alert("비밀번호 확인을 입력해주세요."); return false; }
			if (pw !== pwCf) { alert("비밀번호가 일치하지 않습니다."); return false; }

			document.newMember.submit();
		}
	</script>
	<%@ include file="module/footer.jsp"%>