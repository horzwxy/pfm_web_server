package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.AddDiningRequest;
import me.horzwxy.app.pfm.model.communication.AddDiningResponse;
import me.horzwxy.app.pfm.model.data.Dining;
import me.horzwxy.app.pfm.server.model.DiningApprovalDAO;
import me.horzwxy.app.pfm.server.model.DiningDAO;

public class AddDiningServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		AddDiningRequest request = getRequest( req, AddDiningRequest.class );
		Dining dining = request.dining;
		dining.id = DiningDAO.update( dining );
		DiningApprovalDAO.distribute( dining );
		AddDiningResponse response = new AddDiningResponse( AddDiningResponse.ResultType.SUCCESS );
		resp.getWriter().println( response.toPostContent() );
	}
}
