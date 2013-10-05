package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.ApproveBillRequest;
import me.horzwxy.app.pfm.model.communication.ApproveBillResponse;
import me.horzwxy.app.pfm.model.data.BillApproval;
import me.horzwxy.app.pfm.server.model.BillApprovalDAO;

public class ApproveBillServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ApproveBillRequest request = getRequest( req, ApproveBillRequest.class );
		BillApproval ba = request.ba;
		BillApprovalDAO.update( ba );
		resp.getWriter().println( new ApproveBillResponse( ApproveBillResponse.ResultType.SUCCESS ) );
	}
}
