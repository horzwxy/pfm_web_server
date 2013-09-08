package me.horzwxy.app.pfm.server.example;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class DataOutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		
		Query query = new Query( "example" );
		PreparedQuery pQuery = service.prepare( query );
		
		resp.setContentType("text/plain");
		
		resp.getWriter().println( pQuery.asIterable().iterator().hasNext() );
		
		for(Entity entity: pQuery.asIterable()) {
			String property1 = (String)entity.getProperty( "property1" );
			long property2 = (long)entity.getProperty( "property2" );
			Date date = (Date)entity.getProperty("date");
			resp.getWriter().println( "property1: " + property1
					+ " property2: " + property2
					+ " date: " + date );
		}
	}
}
