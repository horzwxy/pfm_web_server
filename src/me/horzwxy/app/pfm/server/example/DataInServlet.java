package me.horzwxy.app.pfm.server.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.util.Date;

public class DataInServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		
		Entity testData = new Entity( "example" );
		testData.setProperty( "property1", "one" );
		testData.setProperty( "property2", (int)2 );
		testData.setProperty( "date", new Date() );
		
		service.put( testData );
	}
}
