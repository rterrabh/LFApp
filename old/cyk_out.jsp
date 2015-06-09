<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" 	rel="stylesheet" 	href="css/MainStyle.css">
<title>Algoritmo CYK</title>
</head>
<body>
	<table border="1">
	<% String[][] CYK = (String[][])request.getAttribute("CYK"); 
		String word = request.getParameter("word");
		for (int i = 0; i < word.length() + 1; i++) { %>
			<tr>
			<% for (int j = 0; j < word.length(); j++) { %>
				<td> 
				<% 	out.print(CYK[i][j]); 	%>
			</td>	
			<%	} %>
			</tr>
		<% } %>
	
	</table>
</body>
</html>