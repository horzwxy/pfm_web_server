package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.Request;
import me.horzwxy.app.pfm.model.communication.Response;

public abstract class PFMServlet extends HttpServlet {

	@Override
	protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
	
	protected <T extends Request> T getRequest( HttpServletRequest req, Class< T > requestClass ) throws IOException {
		return Request.parseRequest( URLDecoder.decode( req.getReader().readLine(), "utf-8"), requestClass );
	}
	
	protected void writebackResponse( HttpServletResponse resp , Response response ) throws IOException {
		resp.getWriter().println( URLEncoder.encode( response.toPostContent(), "utf-8" ) );
	}
}
