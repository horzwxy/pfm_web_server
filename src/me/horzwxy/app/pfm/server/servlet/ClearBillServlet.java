package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.ClearBillResponse;
import me.horzwxy.app.pfm.model.communication.ClearBillRequest;
import me.horzwxy.app.pfm.model.data.Bill;
import me.horzwxy.app.pfm.model.data.BillApproval;
import me.horzwxy.app.pfm.server.model.BillApprovalDAO;
import me.horzwxy.app.pfm.server.model.BillDAO;

public class ClearBillServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ClearBillRequest request = getRequest( req, ClearBillRequest.class );
		BillApproval ba = request.getBa();
		BillApprovalDAO.update( ba );
		Bill.BillState newState = BillApprovalDAO.checkBillClearState( ba.billId );
		if( newState == Bill.BillState.CLEARED ) {
			BillDAO.update( ba.billId, newState );
		}
		resp.getWriter().println( new ClearBillResponse( ClearBillResponse.ResultType.SUCCESS ).toPostContent() );
	}

}
