package me.horzwxy.app.pfm.server.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.server.model.UserDAO;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class AddDefaultUsersServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		
		// create users
		Entity sun = new Entity( "user", "Sun" );
		sun.setProperty( "accountName", "pyxuri111@gmail.com" );
		sun.setProperty( "accountType", "google" );
		service.put( sun );
		
		Entity wxy = new Entity( "user", "wxy" );
		wxy.setProperty( "accountName", "wangxiayang.fdt@gmail.com" );
		wxy.setProperty( "accountType", "google" );
		service.put( wxy );
		
		Entity shallgo = new Entity( "user", "shallgo" );
		shallgo.setProperty( "accountName", "18801735498" );
		shallgo.setProperty( "accountType", "renren" );
		service.put( shallgo );
		
		Entity le = new Entity( "user", "¿÷“‡¥œ" );
		le.setProperty( "accountName", "18801733953" );
		le.setProperty( "accountType", "lenovo" );
		service.put( le );
		
		Entity rSkip = new Entity( "user", "rSkip" );
		rSkip.setProperty( "accountName", "misdake@gmail.com" );
		rSkip.setProperty( "accountType", "android" );
		service.put( rSkip );
		
		// create contacts
		createContact( "Sun", "wxy" );
		createContact( "Sun", "shallgo" );
		createContact( "Sun", "rSkip" );
		createContact( "Sun", "¿÷“‡¥œ" );
		
		createContact( "wxy", "Sun" );
		createContact( "wxy", "shallgo" );
		createContact( "wxy", "rSkip" );
		createContact( "wxy", "¿÷“‡¥œ" );
		
		createContact( "shallgo", "wxy" );
		createContact( "shallgo", "Sun" );
		createContact( "shallgo", "rSkip" );
		createContact( "shallgo", "¿÷“‡¥œ" );
		
		createContact( "rSkip", "wxy" );
		createContact( "rSkip", "shallgo" );
		createContact( "rSkip", "Sun" );
		createContact( "rSkip", "¿÷“‡¥œ" );
		
		createContact( "¿÷“‡¥œ", "wxy" );
		createContact( "¿÷“‡¥œ", "shallgo" );
		createContact( "¿÷“‡¥œ", "rSkip" );
		createContact( "¿÷“‡¥œ", "Sun" );
	}
	
	private void createContact( String owner, String friend ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey( "user", owner );
		Entity entity = new Entity( "contact", friend, key );
		service.put( entity );
	}
}
