package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.LogInRequest;
import me.horzwxy.app.pfm.model.LogInResponse;
import me.horzwxy.app.pfm.model.User;
import me.horzwxy.app.pfm.server.model.UserDAO;

public class LogInServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter( LogInRequest.EMAIL_KEY );
		User user = UserDAO.getUserByEmail( email );
		if( user == null ) {
			resp.getWriter().println( new LogInResponse( LogInResponse.LogInResponseType.SUCCESS_BUT_FIRST, null ).getPostContent() );
		}
		else {
			resp.getWriter().println( new LogInResponse( LogInResponse.LogInResponseType.SUCCESS, user.nickname ).getPostContent() );
		}
	}
}
