package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.User;
import me.horzwxy.app.pfm.model.LogInResponseType;
import me.horzwxy.app.pfm.model.tool.LogInMessage;
import me.horzwxy.app.pfm.server.model.UserDAO;

public class LogInServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{
			String email = req.getParameter( "email" );
			User user = UserDAO.getUserByEmail( email );
			
			if( user == null ) {
				resp.getWriter().println( LogInMessage.getLogInResponse( LogInResponseType.SUCCESS_BUT_FIRST, null ) );
			}
			else {
				resp.getWriter().println( LogInMessage.getLogInResponse( LogInResponseType.SUCCESS, (String)user.nickname ) );
			}
		} catch ( Exception e ) {
			resp.getWriter().println( LogInMessage.getLogInResponse( LogInResponseType.FAILED, null ) );
		}
	}
}
