<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="util.PasswordManager"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>processAddMember.jsp</title>
</head>
<body>
<%
	request.setCharacterEncoding("utf-8");

	String id = request.getParameter("id");
	String pw = request.getParameter("pw");
	String hashPw = PasswordManager.hashPassword(pw);
%>

<sql:setDataSource var="dataSource" 
url="jdbc:oracle:thin:@192.168.56.1:1522:xe"
driver="oracle.jdbc.driver.OracleDriver"
user="C##dbexam"
password="m1234"
/>

<sql:query dataSource="${dataSource}" var="resultSet">
	select password from NJW_MEMBER where id=?
	<sql:param value="<%=id %>"/>
</sql:query>

<c:if test="${resultSet >= 1 }">
	<c:redirect url="resultMemeber.jsp?msg=joinsucces"/>
</c:if>

</body>
</html>