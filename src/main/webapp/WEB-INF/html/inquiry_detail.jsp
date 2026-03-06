<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dto.BoardDTO"%>
<%@ include file="module/header.jsp"%>
<body data-page="inquiry-detail">
	<div class="grid-bg"></div>
	<%
	BoardDTO board = (BoardDTO) request.getAttribute("board");
	String content = (board.getContent() != null && board.getContent().length() > 10)
			? board.getContent().substring(0, 10) + "..."
			: board.getContent();
	%>
	<%@ include file="module/nav.jsp"%>

	<main class="main">
		<div class="inquiry-detail-container">
			<a href="<%=request.getContextPath()%>/Board" class="back-link">←
				게시판으로 돌아가기</a>

			<div class="inquiry-detail-header"
				style="margin-bottom: 2.5rem; padding-bottom: 2rem;">
				<h1 class="inquiry-detail-title" id="inquiryTitle"><%=board.getSubject()%></h1>
				<div class="inquiry-detail-meta">
					<span class="inquiry-detail-author" id="inquiryAuthor"><%=board.getMemberName()%></span>
					<span class="inquiry-detail-date" id="inquiryDate"><%=board.getDate()%></span>
					<span class="inquiry-detail-date" id="inquiryDate">조회수: <%=board.getHit()%></span>
				</div>
			</div>

			<div class="inquiry-detail-body">
				<div class="inquiry-detail-content" id="inquiryContent"><%=content%></div>
				<c:if
					test="${sessionScope.loginUser.id != null && sessionScope.loginUser.id == board.memberId}">
					<div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">

						<button class="btn btn-small btn-safe"
							onclick="event.stopPropagation(); window.location.href='${pageContext.request.contextPath}/UpdateBoard?id=${dto.id}'">
							수정</button>

						<button class="btn btn-small btn-danger"
							onclick="event.stopPropagation(); if(confirm('정말 삭제하시겠습니까?')) { window.location.href='${pageContext.request.contextPath}/DeleteBoard.do?id=${dto.id}'; }">
							삭제</button>

					</div>
				</c:if>
			</div>

			<div class="inquiry-replies" id="repliesSection"
				style="margin-top: 3.5rem; padding-top: 3.5rem;">
				<h3 class="inquiry-replies-title">찾으시는 내용이 없나요?</h3>
				<div id="repliesList">
					<div class="inquiry-item"
						onclick="window.location.href='${pageContext.request.contextPath}'">
						<div class="inquiry-header">
							<h3 class="inquiry-title">자주 묻는 질문(FAQ)에서 더 빠른 해결책을 찾아보세요.</h3>
						</div>
						<p class="inquiry-preview">자주 묻는 질문 보러가기 →</p>
					</div>
				</div>
			</div>
		</div>
	</main>

	<%@ include file="module/footer.jsp"%>