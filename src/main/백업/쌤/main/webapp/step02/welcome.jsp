<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>welcome</title>
<body>
	<%@ include file="menu.jsp" %>
	<%! String greeting = "Welcome to Web shopping mall";
		String tagline = "Welcome to Web market";
	%>
	<div class="jumbotron">
		<h1>
			<%= greeting %>
		</h1>
	</div>
	<div class="container">
		
		<h3>
			<%= tagline %>
		</h3>
	</div>
	<%@ include file="footer.jsp" %>
</body>
</html>