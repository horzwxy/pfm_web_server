package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.SetNicknameRequest;
import me.horzwxy.app.pfm.model.communication.SetNicknameResponse;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.server.model.UserDAO;

public class SetNicknameServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		SetNicknameRequest request = getRequest( req, SetNicknameRequest.class );
		User user = request.user;
		SetNicknameResponse response = null;
		if( UserDAO.getUser( user.nickname ) != null ) {
			response = new SetNicknameResponse( SetNicknameResponse.ResultType.USED );
		}
		else {
			UserDAO.updateUser( user );
			response = new SetNicknameResponse( SetNicknameResponse.ResultType.SUCCESS );
		}
		resp.getWriter().println( response.toPostContent() );
	}
}
