<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="domain.entity.Board"%>
<%@ page import="util.HtmlEscaper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp" %>

<c:if test="${empty sessionScope.loginUser}">
    <script>
        alert("로그인이 필요한 서비스입니다.");
        location.href = "${pageContext.request.contextPath}";
    </script>
    <% if(true) return; %>
</c:if>

<%
		Board board = (Board) request.getAttribute("board");
%>

<body data-page="write">
        <%@ include file="module/nav.jsp" %>

    <main class="main">
        <a href="${pageContext.request.contextPath}/boards" class="back-link">
            <span>←</span> 뒤로 가기
        </a>

        <div class="form-container">
            <h1>게시글 수정</h1>

            <form id="updatePostForm" action="${pageContext.request.contextPath}/boards"
				name="updatePost" method="post">
				
				<div class="form-group">
                    <input type="hidden" name="uuid" value="<%=HtmlEscaper.h(board.getUuid())%>" required>
                </div>
				
                <div class="form-group">
                    <label for="contactTitle">제목</label>
                    <input type="text" id="contactTitle"
						for="contactTitle" name="title" placeholder="문의 제목을 입력하세요" value="<%=HtmlEscaper.h(board.getSubject())%>" required>
                </div>
                
                <div class="form-group">
                    <label for="contactMessage">내용</label>
                    <textarea id="contactMessage" rows="6" name="body"
							placeholder="문의 내용을 상세히 작성해주세요" required><%=HtmlEscaper.h(board.getContent())%></textarea>
                </div>

                <div class="form-divider">↓</div>

                <input type="button" class="form-submit" value="게시글 수정"
					onclick="checkPost()">
					<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
            </form>
        </div>
    </main>
   	<script>
		function submitUpdateBoard() {
			let form = document.getElementById('updatePostForm');
			let uuid = form.querySelector('[name=uuid]').value;
			let params = new URLSearchParams();
			params.append('title', form.querySelector('[name=title]').value);
			params.append('body', form.querySelector('[name=body]').value);
			fetch('${pageContext.request.contextPath}/boards/' + uuid, {
				method: 'PUT',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'X-CSRF-Token': '${sessionScope.csrfToken}' },
				body: params.toString()
			}).then(res => {
				if (res.ok) { location.href = '${pageContext.request.contextPath}/boards?msg=updatepostsucceeded'; }
				else { res.text().then(msg => alert(msg || '게시글 수정에 실패했습니다.')); }
			}).catch(() => alert('서버 오류가 발생했습니다. 다시 시도해주세요.'));
		}
		function checkPost() {
			let contactTitle = document.getElementById("contactTitle")
			let contactMessage = document.getElementById("contactMessage")

			function check(regExp, e, msg) {
				if (regExp.test(e.value)) {
					return true
				} else {
					alert(msg)
					return false
				}
			}

			if (!contactTitle.value) {
				alert("제목은 필수 입력사항입니다.")
				contactTitle.select()
				contactTitle.focus()
				return false
			}
			if (contactTitle.value.length > 100) {
				alert("[제목]100자 이하로 입력해주세요.")
				contactTitle.select()
				contactTitle.focus()
				return false
			}

			if (!contactMessage.value) {
				alert("내용은 필수 입력사항입니다.")
				contactMessage.select()
				contactMessage.focus()
				return false
			}

			submitUpdateBoard()

		}
	</script>
<%@ include file="module/footer.jsp" %>