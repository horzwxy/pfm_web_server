package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.ApproveDiningRequest;
import me.horzwxy.app.pfm.model.communication.ApproveDiningResponse.ResultType;
import me.horzwxy.app.pfm.model.communication.ApproveDiningResponse;
import me.horzwxy.app.pfm.model.data.Bill;
import me.horzwxy.app.pfm.model.data.Dining;
import me.horzwxy.app.pfm.server.model.DiningApprovalDAO;
import me.horzwxy.app.pfm.server.model.DiningDAO;

public class ApproveDiningServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ApproveDiningRequest request = getRequest( req, ApproveDiningRequest.class );
		DiningApprovalDAO.update( request.getDiningApproval() );
		Dining.DiningState newState = DiningApprovalDAO.checkState( request.getDining() );
		if( newState != Dining.DiningState.NOT_APPROVED_YET ) {
			DiningDAO.update( request.getDining(), newState );
		}
		if( newState == Dining.DiningState.APPROVED ) {
			ArrayList< Bill > bills = getBills( DiningDAO.getDining( request.getDining().id ) );
		}
		resp.getWriter().println( new ApproveDiningResponse( ResultType.SUCCESS ) );
	}
	
	private static ArrayList< Bill > getBills( Dining dining ) {
		return null;
	}
}
