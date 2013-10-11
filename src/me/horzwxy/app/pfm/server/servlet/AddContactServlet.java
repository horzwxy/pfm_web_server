package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.AddContactRequest;
import me.horzwxy.app.pfm.model.communication.AddContactResponse;
import me.horzwxy.app.pfm.model.data.ContactInfo;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.server.model.ContactDAO;
import me.horzwxy.app.pfm.server.model.UserDAO;

public class AddContactServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		AddContactRequest request = getRequest( req, AddContactRequest.class );
		User friend = UserDAO.getUser( request.friendNickname );
		AddContactResponse response = null;
		if( friend == null ) {
			response = new AddContactResponse( AddContactResponse.ResultType.NO_SUCH_USER );
		}
		else {
			User owner = UserDAO.getUser( request.ownerNickname );
			ContactInfo contact = new ContactInfo( owner, friend );
			ContactDAO.update( contact );
			response = new AddContactResponse( AddContactResponse.ResultType.SUCCEED );
		}
		resp.getWriter().println( response.toPostContent() );
	}
}
