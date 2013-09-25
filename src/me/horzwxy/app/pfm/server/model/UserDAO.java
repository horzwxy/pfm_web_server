package me.horzwxy.app.pfm.server.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import me.horzwxy.app.pfm.model.User;

public class UserDAO {
	
	public static List< User > getAllUsers() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "user" );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		List< User > result = new ArrayList< User >();
		while( iterator.hasNext() ) {
			Entity entity = iterator.next();
			User user = new User( (String)entity.getProperty( "email" ), (String)entity.getProperty( "nickname" ) );
			result.add( user );
		}
		return result;
	}
	
	public static User getUserByEmail( String email ) {
		Entity entity = getUserEntityByOneProperty( "email", email );
		User result = null;
		if( entity != null ) {
			result = new User( (String)entity.getProperty( "email" ), (String)entity.getProperty( "nickname" ) );
		}
		return result;
	}
	
	public static User getUserByNickname( String nickname ) {
		Entity entity = getUserEntityByOneProperty( "nickname", nickname );
		User result = null;
		if( entity != null ) {
			result = new User( (String)entity.getProperty( "email" ), (String)entity.getProperty( "nickname" ) );
		}
		return result;
	}
	
	public static void updateUser( User user ) {
		Entity entity = getUserEntityByOneProperty( "email", user.email );
		if( entity == null ) {
			entity = new Entity( "user" );
			entity.setProperty( "email", user.email );
			entity.setProperty( "nickname", user.nickname );
		}
		entity.setProperty( "nickname", user.nickname );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
	}
	
	private static Entity getUserEntityByOneProperty( String property, String value ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate( property, FilterOperator.EQUAL, value );
		Query query = new Query( "user" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
}
