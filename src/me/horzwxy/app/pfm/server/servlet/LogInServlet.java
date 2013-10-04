package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.LogInRequest;
import me.horzwxy.app.pfm.model.communication.LogInResponse;
import me.horzwxy.app.pfm.model.communication.Request;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.server.model.UserDAO;

public class LogInServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LogInRequest request = getRequest( req, LogInRequest.class );
		User user = UserDAO.getUser( request.accountName, request.accountType );
		LogInResponse response = null;
		if( user == null ) {
			response = new LogInResponse( LogInResponse.ResultType.SUCCESS_BUT_FIRST, null );
		}
		else {
			response = new LogInResponse( LogInResponse.ResultType.SUCCESS, user.nickname );
		}
		resp.getWriter().println( response.toPostContent() );
	}
}
