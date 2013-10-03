<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="me.horzwxy.app.pfm.model.data.User" %>
<%
	List< User > userList = (List< User >) request.getAttribute( "userList" );
%>
<html>
	<head>
		<title>user list</title>
	</head>
	<body>
		<table>
			<tr><th>index</th><th>account name</th><th>nickname</th><th>account type</th></tr>
			<%for( int i = 0; i < userList.size(); i++ ){ %>
			<tr><td><%=i %></td><td><%=userList.get(i).accountName %></td><td><%=userList.get(i).nickname %></td><td><%=userList.get(i).accountType %></td></tr>
			<%} %>
		</table>
	</body>
</html>