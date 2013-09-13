package me.horzwxy.app.pfm.server.servlet.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.server.model.UserDAO;

public class ShowAllUsersServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute( "userList", UserDAO.getAllUsers() );
		RequestDispatcher rd = getServletContext().getRequestDispatcher( "/allusers.jsp" );
		rd.forward( req, resp );
	}
}
