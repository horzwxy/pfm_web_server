package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.Request;

public abstract class PFMServlet extends HttpServlet {

	@Override
	protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
	
	protected <T extends Request> T getRequest( HttpServletRequest req, Class< T > requestClass ) throws IOException {

		String jsonString = req.getReader().readLine();
		return Request.parseRequest( jsonString, requestClass );
	}
}
