package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.ListContactsRequest;
import me.horzwxy.app.pfm.model.communication.ListContactsResponse;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.model.data.UserList;
import me.horzwxy.app.pfm.server.model.ContactDAO;

public class ListContactsServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ListContactsRequest request = getRequest( req, ListContactsRequest.class );
		UserList list = ContactDAO.getOnesContacts( new User( request.nickname ) );
		ListContactsResponse response = new ListContactsResponse( list );
		resp.getWriter().println( response.toPostContent() );
	}

}
