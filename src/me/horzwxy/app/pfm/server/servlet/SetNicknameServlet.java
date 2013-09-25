package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.SetNicknameResponse;
import me.horzwxy.app.pfm.model.User;
import me.horzwxy.app.pfm.model.SetNicknameRequest;
import me.horzwxy.app.pfm.server.model.UserDAO;

public class SetNicknameServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter( SetNicknameRequest.EMAIL_KEY );
		String nickname = req.getParameter( SetNicknameRequest.NICKNAME_KEY );
		User user = new User( email, nickname );
		UserDAO.updateUser( user );
		
		resp.getWriter().println( new SetNicknameResponse( SetNicknameResponse.SetNicknameResponseType.SUCCESS ).getPostContent() );
	}
}
