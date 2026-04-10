<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="scan-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>상품명</th>
            <th>날짜</th>
            <th>카테고리</th>
            <th>작업</th>
        </tr>
    </thead>
    <tbody id="scanTableBody">
        <c:choose>
            <c:when test="${not empty productList}">
                <c:forEach var="product" items="${productList}">
                    <tr>
                        <td>
                            <span style="font-family: 'JetBrains Mono', monospace; color: var(--text-secondary);">
                                #<c:out value="${product.productId}"/>
                            </span>
                        </td>
                        <td><div class="scan-title"><c:out value="${product.pname}"/></div></td>
                        <td>${product.simpleDate}</td>
                        <td><span class="status-badge"><c:out value="${product.category}"/></span></td>
                        <td>
                            <div class="scan-actions">
                                <button class="btn btn-small btn-info"
                                    onclick="window.location.href='${pageContext.request.contextPath}/products/${product.uuid}'">보기</button>
                                <button class="btn btn-small btn-safe"
                                    onclick="window.location.href='${pageContext.request.contextPath}/products/${product.uuid}/edit'">수정</button>
                                <button class="btn btn-small btn-danger"
                                    onclick="deleteProduct('${product.uuid}')">삭제</button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr><td colspan="5" style="text-align: center; padding: 20px;">등록된 상품이 없습니다.</td></tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>
