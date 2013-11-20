package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.ListBillsRequest;
import me.horzwxy.app.pfm.model.communication.ListBillsResponse;
import me.horzwxy.app.pfm.model.data.BillList;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.server.model.BillDAO;

public class ListBillsServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ListBillsRequest request = getRequest( req, ListBillsRequest.class );
		User user = request.getUser();
		writebackResponse( resp, new ListBillsResponse( BillDAO.getOnesBills( user ) ) );
	}

}
