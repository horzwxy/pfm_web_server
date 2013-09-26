package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.AddContactRequest;
import me.horzwxy.app.pfm.model.AddContactResponse;
import me.horzwxy.app.pfm.model.AddContactResponse.AddContactResponseType;
import me.horzwxy.app.pfm.model.ContactInfo;
import me.horzwxy.app.pfm.model.User;
import me.horzwxy.app.pfm.server.model.ContactDAO;
import me.horzwxy.app.pfm.server.model.UserDAO;

public class AddContactServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String owner = req.getParameter( AddContactRequest.OWNER_KEY );
		String friend = req.getParameter( AddContactRequest.FRIEND_KEY );
		User userFriend = UserDAO.getUserByNickname( friend );
		if( userFriend == null ) {
			resp.getWriter().println( new AddContactResponse( AddContactResponseType.NO_SUCH_USER ).getPostContent() );
		}
		else {
			ContactInfo info = new ContactInfo( 
					new User( null, owner ), new User( null, friend ) );
			ContactDAO.update( info );
			resp.getWriter().println( new AddContactResponse( AddContactResponseType.SUCCESS ).getPostContent() );
		}
	}
}
