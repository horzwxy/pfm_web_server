<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="me.horzwxy.app.pfm.model.data.BillApproval" %>
<%
	List< BillApproval > basList = (List< BillApproval >) request.getAttribute( "basList" );
%>
<html>
	<head>
		<title>bill approvals list</title>
	</head>
	<body>
		<table>
			<tr>
				<th>index</th>
				<th>bill id</th>
				<th>owner nickname</th>
				<th>state</th>
			</tr>
			<%for( int i = 0; i < basList.size(); i++ ){ %>
			<tr>
				<td><%=i %></td>
				<td><%=basList.get(i).billId %></td>
				<td><%=basList.get(i).owner.nickname %></td>
				<td><%=basList.get(i).state.toString() %></td>
			</tr>
			<%} %>
		</table>
	</body>
</html>