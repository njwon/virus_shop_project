<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp"%>

<c:if test="${empty sessionScope.loginUser}">
	<script>
		alert("로그인이 필요한 서비스입니다.");
		location.href = "<%=request.getContextPath()%>/login";
	</script>
	<%
	if (true)
		return;
	%>
</c:if>
<body data-page="write">
	<%@ include file="module/nav.jsp"%>

	<main class="main">
		<a href="<%=request.getContextPath()%>" class="back-link"> <span>←</span>
			뒤로 가기
		</a>

		<div class="form-container">
			<h1>새 스캔</h1>

			<form action="${pageContext.request.contextPath}/scans?_csrf=${sessionScope.csrfToken}"
				name="newScan" method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="title">검사 대상 파일</label> <input type="file"
						class="form-control" id="file" name="file">
				</div>

				<div class="form-divider">↓</div>

				<input type="button" class="form-submit" value="검사"
					onclick="checkNewscan()">
			</form>
		</div>
	</main>

	<div id="loadingModal" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.7); backdrop-filter:blur(6px); z-index:9999; align-items:center; justify-content:center; flex-direction:column; gap:1.2rem;">
		<div style="width:48px; height:48px; border:4px solid var(--border); border-top-color:var(--accent); border-radius:50%; animation:spin 0.8s linear infinite;"></div>
		<p style="color:var(--text-primary); font-size:1rem; letter-spacing:0.05em;">검사 중입니다. 잠시만 기다려주세요...</p>
	</div>
	<style>
		@keyframes spin { to { transform: rotate(360deg); } }
	</style>

	<script>
		function checkNewscan() {
			const fileInput = document.getElementById("file");
			const file = fileInput.files[0];

			if (fileInput.files.length === 0) {
				alert("검사할 파일을 선택해주세요.");
				return false;
			}

			if ((1024 * 1024) * 32 < file.size) {
				alert("32MB 미만의 파일만 업로드 가능합니다.");
				return false;
			}

			const modal = document.getElementById("loadingModal");
			modal.style.display = "flex";
			document.newScan.submit();
		}
	</script>
	<%@ include file="module/footer.jsp"%>