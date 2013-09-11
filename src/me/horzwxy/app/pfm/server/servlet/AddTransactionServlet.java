package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddTransactionServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String restaurant = req.getParameter( "restaurant" );
		DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd-HH-mm" );
		try {
			Date date = formatter.parse( req.getParameter( "date" ) );
		} catch (ParseException e) {
			e.printStackTrace();
			// TODO return error message
		}
		int cost = Integer.parseInt( req.getParameter( "cost" ) );
		String[] participants = req.getParameter( "participants" ).split( ";" );
	}
}
