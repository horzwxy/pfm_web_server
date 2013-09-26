<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List" %>
<%@ page import="me.horzwxy.app.pfm.model.User" %>
<%@ page import="me.horzwxy.app.pfm.model.Dining" %>
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
				<td><%=diningList.get(i).restaurant %></td>
				<td><%=Dining.dateFormat.format( diningList.get(i).date ) %></td>
				<td><%=diningList.get(i).cost %></td>
				<td><%=diningList.get(i).participantsToString() %></td>
				<td><%=diningList.get(i).userCostMapToString( diningList.get(i).specialCosts ) %></td>
				<td><%=diningList.get(i).userCostMapToString( diningList.get(i).paids ) %></td>
				<td><%=diningList.get(i).author.nickname %></td>
				<td><%=diningList.get(i).state %></td>
			</tr>
			<%} %>
		</table>
	</body>
</html>