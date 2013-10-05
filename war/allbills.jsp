<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="me.horzwxy.app.pfm.model.data.Bill" %>
<%
	List< Bill > billsList = (List< Bill >) request.getAttribute( "billsList" );
%>
<html>
	<head>
		<title>bills list</title>
	</head>
	<body>
		<table>
			<tr>
				<th>index</th>
				<th>id</th>
				<th>diningId</th>
				<th>lender</th>
				<th>borrower</th>
				<th>cost</th>
				<th>state</th>
			</tr>
			<%for( int i = 0; i < billsList.size(); i++ ){ %>
			<tr>
				<td><%=i %></td>
				<td><%=billsList.get(i).billId %></td>
				<td><%=billsList.get(i).diningId %></td>
				<td><%=billsList.get(i).lender.nickname %></td>
				<td><%=billsList.get(i).borrower.nickname %></td>
				<td><%=billsList.get(i).cost.cost %></td>
				<td><%=billsList.get(i).state.toString() %></td>
			</tr>
			<%} %>
		</table>
	</body>
</html>