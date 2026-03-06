<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp"%>
<body data-page="contact">
	<div class="grid-bg"></div>

	<%@ include file="module/nav.jsp"%>

	<main class="main">
		<section class="welcome-section">
			<h1>게시판</h1>
			<p>문의 사항을 입력해주세요</p>
		</section>

		<div class="contact-container">
			<div class="contact-form-section">
				<h2 class="section-title">새 문의 작성</h2>
				<form id="contactForm" class="contact-form" name="newPost"
					action='${pageContext.request.contextPath}/addPost.do'
					method="post">
					<div class="form-group">
						<label for="contactTitle">제목</label> <input type="text"
							id="contactTitle" name="title" placeholder="문의 제목을 입력하세요"
							required>
					</div>
					<div class="form-group">
						<label for="contactMessage">내용</label>
						<textarea id="contactMessage" rows="6" name="body"
							placeholder="문의 내용을 상세히 작성해주세요" required></textarea>
					</div>
					<input type="button" class="form-submit" value="문의 등록"
						onclick="checkPost()">
				</form>
			</div>

			<div class="contact-list-section" id="page">
				<div class="contact-header">
					<h2 class="section-title">문의 목록</h2>
				</div>
				<div class="inquiries-list" id="inquiriesList">
					<c:choose>
						<c:when test="${empty list}">
							<div class="inquiry-item">
								<p style="padding: 20px; text-align: center;">등록된 게시물이 없습니다.</p>
							</div>
						</c:when>

						<c:otherwise>
							<c:forEach var="dto" items="${list}">
								<div class="inquiry-item"
									onclick="window.location.href='${pageContext.request.contextPath}/BoardDetail?id=${dto.id}'">
									<div class="inquiry-header">
										<h3 class="inquiry-title">${dto.subject}</h3>
										<div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
											<c:if
												test="${sessionScope.loginUser.id != null && sessionScope.loginUser.id == dto.memberId}">
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
									</div>
									<div class="inquiry-meta">
										<span class="inquiry-date">${dto.date}</span> <span
											class="inquiry-author">${dto.memberName}</span> <span
											class="inquiry-hit">조회수: ${dto.hit}</span>
									</div>
									<div class="wrapper"
										style="position: relative; width: 100%; height: 24px;">
										<p class="inquiry-preview"
											style="position: absolute; top: 0; left: 0; width: 100%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin: 0;">${dto.content}</p>
									</div>
								</div>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="pagination"
					style="text-align: center; margin-top: 30px;">
					<c:if test="${ph.showPrev}">
						<a href="Board?page=${ph.beginPage - 1}#page">[이전]</a>
					</c:if>

					<c:forEach var="i" begin="${ph.beginPage}" end="${ph.endPage}">
						<c:choose>
							<c:when test="${i == ph.page}">
								<strong style="margin: 0 5px; color: #666;">${i}</strong>
							</c:when>
							<c:otherwise>
								<a href="Board?page=${i}#page"
									style="margin: 0 5px; color: #666; text-decoration: none;">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:if test="${ph.showNext}#page">
						<a href="Board?page=${ph.endPage + 1}">[다음]</a>
					</c:if>
				</div>
			</div>
	</main>
	<script>
		const urlParams = new URLSearchParams(window.location.search);
		const message = urlParams.get('msg');
		if (message === 'addpostsucceeded') {
			alert("게시글이 성공적으로 등록되었습니다!");
			history.replaceState(null, '', window.location.pathname);
		} else if (message === 'updatepostsucceeded') {
			alert("게시글이 성공적으로 수정되었습니다!");
			history.replaceState(null, '', window.location.pathname);
		} else if (message === 'delaccountsucceeded') {
			alert("게시글이 성공적으로 삭제되었습니다!");
			history.replaceState(null, '', window.location.pathname);
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

			document.newPost.submit()

		}
	</script>
	<%@ include file="module/footer.jsp"%>