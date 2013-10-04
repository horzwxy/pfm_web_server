package me.horzwxy.app.pfm.server.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.horzwxy.app.pfm.model.data.User;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

public class UserDAO {
	
	public static List< User > getAllUsers() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "user" );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		List< User > result = new ArrayList< User >();
		while( iterator.hasNext() ) {
			Entity entity = iterator.next();
			result.add( createUser( entity ) );
		}
		return result;
	}
	
	public static Key getKey( User user ) {
		Entity entity = getEntity( user );
		if( entity == null ) {
			return null;
		}
		return entity.getKey();
	}
	
	public static void updateUser( User user ) {
		Entity entity = createEntity( user );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
	}
	
	public static User getUser( String accountName, String accountType ) {
		Entity entity = getEntity( accountName, accountType );
		return createUser( entity );
	}
	
	public static User getUser( String nickname ) {
		Entity entity = getEntity( nickname );
		return createUser( entity );
	}
 	
	private static Entity getEntity( String nickname ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey( "user", nickname );
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, key );
		Query query = new Query( "user" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
	
	private static Entity getEntity( String accountName, String accountType ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter1 = new FilterPredicate( "accountName", FilterOperator.EQUAL, accountName );
		Filter filter2 = new FilterPredicate( "accountType", FilterOperator.EQUAL, accountType );
		Filter comFilter = CompositeFilterOperator.and(filter1, filter2);
		Query query = new Query( "user" ).setFilter( comFilter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
	
	private static Entity getEntity( User user ) {
		return getEntity( user.nickname );
	}
	
	private static Entity createEntity( User user ) {
		if( user == null ) {
			return null;
		}
		Entity entity = new Entity( "user", user.nickname );
		entity.setProperty( "accountName", user.accountName );
		entity.setProperty( "accountType", user.accountType );
		return entity;
	}
	
	private static User createUser( Entity entity ) {
		if( entity == null ) {
			return null;
		}
		return new User( (String)entity.getProperty( "accountName" ),
				entity.getKey().getName(),
				(String)entity.getProperty( "accountType" ) );
	}
}
