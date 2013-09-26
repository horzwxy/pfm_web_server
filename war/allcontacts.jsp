<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="me.horzwxy.app.pfm.model.ContactInfo" %>
<%
	List< ContactInfo > contactList = (List< ContactInfo >) request.getAttribute( "contactsList" );
%>
<html>
	<head>
		<title>contact list</title>
	</head>
	<body>
		<table>
			<tr><th>index</th><th>email</th><th>nickname</th></tr>
			<%for( int i = 0; i < contactList.size(); i++ ){ %>
			<tr>
				<td><%=i %></td>
				<td><%=contactList.get(i).owner.nickname %></td>
				<td><%=contactList.get(i).friend.nickname %></td>
			</tr>
			<%} %>
		</table>
	</body>
</html>