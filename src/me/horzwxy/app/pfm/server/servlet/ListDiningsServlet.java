package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.ListDiningsRequest;
import me.horzwxy.app.pfm.model.communication.ListDiningsResponse;
import me.horzwxy.app.pfm.model.data.Dining;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.server.model.DiningDAO;

public class ListDiningsServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ListDiningsRequest request = getRequest( req, ListDiningsRequest.class );
		User user = request.getUser();
		ArrayList< Dining > dinings = DiningDAO.getOnesDinings( user );
		ListDiningsResponse response = new ListDiningsResponse( dinings );
		resp.getWriter().println( response.toPostContent() );
	}

}
