<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>180_update.jsp</title>
</head>
<body>
	<form action="process_update.jsp" method="post">
		<p>아이디  : <input type="text" name="id"/></p>
		<p>비번  : <input type="password" name="pw"/></p>
		<p>수정할 이름  : <input type="text" name="name"/></p>
		<input type="submit" value="전송"/>
	</form>
</body>
</html>