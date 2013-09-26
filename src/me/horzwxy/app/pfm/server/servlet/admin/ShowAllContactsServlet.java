package me.horzwxy.app.pfm.server.servlet.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.server.model.ContactDAO;

public class ShowAllContactsServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute( "contactsList", ContactDAO.getAllContacts() );
		RequestDispatcher rd = getServletContext().getRequestDispatcher( "/allcontacts.jsp" );
		rd.forward( req, resp );
	}
}
