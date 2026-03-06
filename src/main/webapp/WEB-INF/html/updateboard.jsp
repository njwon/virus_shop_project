<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="dto.BoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp" %>

<c:if test="${empty sessionScope.loginUser || sessionScope.loginUser.role != 'ADMIN'}">
    <script>
        alert("잘못된 접근입니다.");
        location.href = "${pageContext.request.contextPath}";
    </script>
    <% if(true) return; %>
</c:if>

<%
		BoardDTO board = (BoardDTO) request.getAttribute("board");
%>

<body data-page="write">
        <%@ include file="module/nav.jsp" %>

    <main class="main">
        <a href="${pageContext.request.contextPath}/AdminBoard.do" class="back-link">
            <span>←</span> 뒤로 가기
        </a>

        <div class="form-container">
            <h1>게시글 수정</h1>

            <form action="${pageContext.request.contextPath}/updateBoard.do"
				name="updatePost" method="post">
				
				<div class="form-group">
                    <input type="hidden" name="Id" value="<%=board.getId() %>" required>
                </div>
				
                <div class="form-group">
                    <label for="contactTitle">제목</label>
                    <input type="text" id="contactTitle"
						for="contactTitle" name="title" placeholder="문의 제목을 입력하세요" value="<%=board.getSubject() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="contactMessage">내용</label>
                    <textarea id="contactMessage" rows="6" name="body"
							placeholder="문의 내용을 상세히 작성해주세요" required><%=board.getContent() %></textarea>
                </div>

                <div class="form-divider">↓</div>

                <input type="button" class="form-submit" value="게시글 수정"
					onclick="checkPost()">
            </form>
        </div>
    </main>
   	<script>
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

			//category
			if (!contactTitle.value) {
				alert("제목은 필수 입력사항입니다.")
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

			document.updatePost.submit()

		}
	</script>
<%@ include file="module/footer.jsp" %>