<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" 	rel="stylesheet" 	href="css/MainStyle.css">
<title>Warning</title>
</head>
<body>
	<% if (request.getParameter("txtGrammar").equals("")) { %>
 		N&atilde;o &eacute; poss&iacute;vel gerar gram&aacute;tica! Regras de produ&ccedil;&atilde;o n&atilde;o foram especificadas!
 	<%} else  { %>
			Os dados inseridos s&atilde;o inv&aacute;lidos!
		<%} %>
	<br/><br/>
	<a href="grammar.jsp">Inserir nova gram&aacute;tica</a> 
 
 
</body>
</html>