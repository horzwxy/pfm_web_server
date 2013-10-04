<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="me.horzwxy.app.pfm.model.data.User" %>
<%@ page import="me.horzwxy.app.pfm.model.data.Dining" %>
<%
	List< Dining > diningList = (List< Dining >) request.getAttribute( "diningList" );
%>
<html>
	<head>
		<title>dining list</title>
	</head>
	<body>
		<table>
			<tr>
				<th>index</th>
				<th>id</th>
				<th>restaurant</th>
				<th>date</th>
				<th>cost</th>
				<th>participants</th>
				<th>special costs</th>
				<th>paids</th>
				<th>author</th>
				<th>state</th>
			</tr>
			<%for( int i = 0; i < diningList.size(); i++ ){ %>
			<tr>
				<td><%=i %></td>
				<td><%=diningList.get(i).id %></td>
				<td><%=diningList.get(i).restaurant.name %></td>
				<td><%=diningList.get(i).date.toString() %></td>
				<td><%=diningList.get(i).cost.cost %></td>
				<td><%=diningList.get(i).participants.toJsonString() %></td>
				<td>"xx"</td>
				<td>"xx"</td>
				<td><%=diningList.get(i).author.nickname %></td>
				<td><%=diningList.get(i).state %></td>
			</tr>
			<%} %>
		</table>
	</body>
</html>