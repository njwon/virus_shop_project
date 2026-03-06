<%@page import="java.util.ArrayList"%>
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

		<form action="${pageContext.request.contextPath}/signUp.do"
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
		</form>
		<div class="form-footer">
			이미 계정이 있으신가요? <a href="login.jsp">로그인</a>
		</div>
	</div>
	<script>
		
	<%
		String errorMsg = (String) request.getAttribute("msg");%>
		
	<%if (errorMsg != null && !errorMsg.isEmpty()) {%>
		alert(
	"<%=errorMsg%>"
		);
	<%}%>
		function checkForm() {
			if (!document.newMember.id.value) {
				alert("아이디를 입력해주세요.")
				return false;
			}
			if (!document.newMember.pw.value) {
				alert("비번을 입력해주세요.")
				return false;
			}
			if (!document.newMember.pw_confirm.value) {
				alert("비번확인을 입력해주세요.")
				return false;
			}
			if (document.newMember.pw.value != document.newMember.pw_confirm.value) {
				alert("비번을 동일하게 입력해주세요.")
				return false;
			}
			if (!document.newMember.name.value) {
				alert("이름을 입력해주세요.")
				return false;
			}
			if (!document.newMember.mail.value) {
				alert("메일을 입력해주세요.")
				return false;
			}
			if (!document.newMember.phone.value) {
				alert("전화번호를 입력해주세요.")
				return false;
			}
			document.newMember.submit();
		}
	</script>
	<%@ include file="module/footer.jsp"%>