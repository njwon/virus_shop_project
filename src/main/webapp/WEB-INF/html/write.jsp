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

			<form action="${pageContext.request.contextPath}/ScanFile.do"
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
	<script>
		function checkNewscan() {
			let fileInput = document.getElementById("file")
			file = fileInput.files[0];
			/*
			[1]productId:첫글자를 반드시 P로 시작하고 숫자를 조합해서 3-10자리까지 입력
			[2]pname:최소 3자리 최대 12자리까지 입력
			[3]unitPrice: 숫자만 입력, 음수 입력X
			[4]manufacturer, category: 입력이나 선택 필수
			[5]unitsInStock: 숫자만 입력
			[6]productImage: 등록 필수
			 */

			function check(regExp, e, msg) {
				if (regExp.test(e.value)) {
					return true
				} else {
					alert(msg)
					return false
				}
			}

			if(fileInput.files.length === 0){
				alert("대상 파일을 등록해주세요")
				return false
			}
			
			if ((1024 * 1024) * 32 < file.size) {
				alert("32MB 사이즈 미만만 업로드가 가능합니다.")
				return false
			}

			document.newScan.submit()

		}
	</script>
	<%@ include file="module/footer.jsp"%>