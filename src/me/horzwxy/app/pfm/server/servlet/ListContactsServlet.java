package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.ListContactsRequest;
import me.horzwxy.app.pfm.model.ListContactsResponse;
import me.horzwxy.app.pfm.model.User;
import me.horzwxy.app.pfm.server.model.ContactDAO;

public class ListContactsServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String nickname = req.getParameter( ListContactsRequest.NICKNAME_KEY );
		User user = new User( null, nickname );
		List< User > contactList = ContactDAO.getOnesContacts( user );
		resp.getWriter().println( new ListContactsResponse( contactList ).getPostContent() );
	}
}