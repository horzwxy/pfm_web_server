<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="me.horzwxy.app.pfm.model.User" %>
<%
	List< User > userList = (List< User >) request.getAttribute( "userList" );
%>
<html>
	<head>
		<title>user list</title>
	</head>
	<body>
		<table>
			<tr><th>index</th><th>email</th><th>nickname</th></tr>
			<%for( int i = 0; i < userList.size(); i++ ){ %>
			<tr><td><%=i %></td><td><%=userList.get(i).email %></td><td><%=userList.get(i).nickname %></td>
			<%} %>
		</table>
	</body>
</html>