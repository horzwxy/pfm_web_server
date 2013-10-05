<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="me.horzwxy.app.pfm.model.data.User" %>
<%@ page import="me.horzwxy.app.pfm.model.data.DiningApproval" %>
<%
	List< DiningApproval > diningList = (List< DiningApproval >) request.getAttribute( "dasList" );
%>
<html>
	<head>
		<title>dining approvals list</title>
	</head>
	<body>
		<table>
			<tr>
				<th>index</th>
				<th>dining id</th>
				<th>user nickname</th>
				<th>state</th>
			</tr>
			<%for( int i = 0; i < diningList.size(); i++ ){ %>
			<tr>
				<td><%=i %></td>
				<td><%=diningList.get(i).diningId %></td>
				<td><%=diningList.get(i).user.nickname %></td>
				<td><%=diningList.get(i).state.toString() %></td>
			</tr>
			<%} %>
		</table>
	</body>
</html>