package me.horzwxy.app.pfm.server.servlet.admin;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class AddTestUserServlet extends HttpServlet {
	
	private static final String ADMIN_EMAIL = "admin@pfm.pfm";
	private static final String ADMIN_NICKNAME = "admin";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate("email", FilterOperator.EQUAL, ADMIN_EMAIL);
		Query query = new Query( "user" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		
		resp.setContentType("text/plain");
		
		Iterator<Entity> qResult = pQuery.asIterable().iterator();
		if( qResult.hasNext() ) {
			resp.getWriter().println( "already inserted" );
			return;
		}
		
		Entity admin = new Entity( "user" );
		admin.setProperty( "email", ADMIN_EMAIL );
		admin.setProperty( "nickname", ADMIN_NICKNAME );
		
		service.put( admin );
	}
}
