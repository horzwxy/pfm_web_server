package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.AddDiningInfoResponse;
import me.horzwxy.app.pfm.model.AddDiningInfoResponse.AddDiningInfoType;
import me.horzwxy.app.pfm.model.Dining;
import me.horzwxy.app.pfm.model.AddDiningInfoRequest;
import me.horzwxy.app.pfm.model.User;

import me.horzwxy.app.pfm.server.model.DiningDAO;

public class AddDiningInfoServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String restaurant = req.getParameter( AddDiningInfoRequest.RESTAURANT_KEY );
		DateFormat formatter = Dining.dateFormat;
		Date date = null;
		try {
			date = formatter.parse( req.getParameter( AddDiningInfoRequest.DATE_KEY ) );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int cost = Integer.parseInt( req.getParameter( AddDiningInfoRequest.COST_KEY ) );
		ArrayList< User > participants = Dining.getParticipantsFromString( req.getParameter( AddDiningInfoRequest.PARTICIPANTS_KEY ) );
		Map< User, Integer > specialCosts = Dining.getUserCostMapFromString( req.getParameter( AddDiningInfoRequest.SPECIALCOSTS_KEY ) );
		Map< User, Integer > paids = Dining.getUserCostMapFromString( req.getParameter( AddDiningInfoRequest.PAIDS_KEY ) );
		User author = new User( null, req.getParameter( AddDiningInfoRequest.AUTHOR_KEY ) );
		
		Dining diningInfo = new Dining();
		diningInfo.restaurant = restaurant;
		diningInfo.date = date;
		diningInfo.cost = cost;
		diningInfo.participants = participants;
		diningInfo.specialCosts = specialCosts;
		diningInfo.paids = paids;
		diningInfo.author = author;
		
		DiningDAO.update( diningInfo );
		
		resp.getWriter().println( new AddDiningInfoResponse( AddDiningInfoType.SUCCESS ).getPostContent() );
	}
}
