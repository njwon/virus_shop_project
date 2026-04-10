<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<section class="scans-section">
    <div class="section-header">
        <h2 class="section-title">판매 목록</h2>
    </div>

    <div class="scans-grid" id="scansGrid">
        <c:choose>
            <c:when test="${empty productList}">
                <div class="no-data-msg" style="grid-column: 1/-1; text-align: center; padding: 50px;">
                    <h3>등록된 상품이 없습니다.</h3>
                    <p>관리자 페이지에서 상품을 등록해주세요.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="product" items="${productList}">
                    <div class="scan-card" onclick="window.location.href='${pageContext.request.contextPath}/products/${product.uuid}'">
                        <div class="scan-meta">
                            <span><c:out value="${product.pname}"/></span>
                            <span class="scan-status"><c:out value="${product.category}"/></span>
                        </div>
                        <c:if test="${not empty product.productImage}">
                            <img src="${pageContext.request.contextPath}/resources/images/<c:out value="${product.productImage}"/>"
                                 style="width: 100%; height: 200px; object-fit: cover;">
                        </c:if>
                        <h3><c:out value="${product.pname}"/></h3>
                        <p><c:out value="${product.description}"/></p>
                        <div class="scan-url"><fmt:formatNumber value="${product.unitPrice}" pattern="#,###"/>원</div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</section>
