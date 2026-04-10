<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="util.HtmlEscaper"%>
<%@ include file="module/header.jsp"%>
<body data-page="login">
	<div class="grid-bg"></div>
	<div class="glow"></div>

	<div class="container">
		<a href="<%=request.getContextPath()%>" class="back-link"> <span>←</span>
			뒤로 가기
		</a>

		<div class="form-header">
			<h1>로그인</h1>
			<p class="form-subtitle">계정에 접속하세요</p>
		</div>

		<form name="loginMember"
			action='${pageContext.request.contextPath}/sessions' method="post">
			<div class="form-group">
				<label for="id">아이디</label> 
				<input type="text" id="id" name="id" placeholder="Mrhong" required>
			</div>
			<div class="form-group">
				<label for="password">비밀번호</label> 
				<input type="password" id="pw" name="pw" placeholder="••••••••" required>
			</div>
			<div class="login-form-group">
				<label class="checkbox-container"> <input type="checkbox"
					id="remember" name="remember_me" /> <span class="checkbox-label">로그인
						유지하기</span>
				</label>
			</div>
			<button type="button" class="form-submit" onclick="checkLogin()">로그인</button>
			<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
		</form>

		<div class="form-footer">
			계정이 없으신가요? <a href="signup.jsp">회원가입</a>
		</div>
	</div>

	<%String errorMsg = (String) request.getAttribute("message");%>
	<%if (errorMsg != null && !errorMsg.isEmpty()) {%>
	<div id="_loginSrvMsg" style="display:none"><%=HtmlEscaper.h(errorMsg)%></div>
	<script>(function(){ var m=document.getElementById('_loginSrvMsg'); if(m) alert(m.textContent); })();</script>
	<%}%>
	<script>
	function checkLogin() {
		const id = document.getElementById('id');
		const pw = document.getElementById('pw');
		if (!id.value) { alert("아이디를 입력해주세요."); id.focus(); return; }
		if (id.value.length > 20) { alert("아이디는 20자 이하로 입력해주세요."); id.select(); id.focus(); return; }
		if (!pw.value) { alert("비밀번호를 입력해주세요."); pw.focus(); return; }
		if (pw.value.length > 50) { alert("비밀번호는 50자 이하로 입력해주세요."); pw.select(); pw.focus(); return; }
		document.loginMember.submit();
	}
	const _msg = new URLSearchParams(window.location.search).get('msg');
	const _err = new URLSearchParams(window.location.search).get('error');
	if (_msg === 'updateaccountpwsucceeded') {
		alert('비밀번호가 변경되었습니다. 다시 로그인해주세요.');
		history.replaceState(null, '', window.location.pathname);
	} else if (_err === 'auth') {
		alert('로그인이 필요한 페이지입니다.');
		history.replaceState(null, '', window.location.pathname);
	}
	</script>
	<%@ include file="module/footer.jsp"%>