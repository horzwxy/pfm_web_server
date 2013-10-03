package me.horzwxy.app.pfm.server.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.horzwxy.app.pfm.model.data.ContactInfo;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.model.data.UserList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

public class ContactDAO {

	public static void update( ContactInfo info ) {
		Entity entity = createEntity( info );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
	}
	
	public static Entity createEntity( ContactInfo info ) {
		Entity entity = new Entity( "contact" );
		entity.setProperty( "owner", info.owner.nickname );
		entity.setProperty( "friend", info.friend.nickname );
		return entity;
	}
	
	public static Entity getEntity( ContactInfo info ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter1 = new FilterPredicate( "owner", FilterOperator.EQUAL, info.owner.nickname );
		Filter filter2 = new FilterPredicate( "friend", FilterOperator.EQUAL, info.friend.nickname );
		Filter andFilter =
				  CompositeFilterOperator.and( filter1, filter2 );
		Query query = new Query( "contact" ).setFilter( andFilter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
	
	public static UserList getOnesContacts( User user ) {
		UserList result = new UserList();
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate( "owner", FilterOperator.EQUAL, user.nickname );
		Query query = new Query( "contact" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		for( Entity entity : pQuery.asIterable() ) {
			result.add( new User( ( String )entity.getProperty( "friend" ) ) );
		}
		return result;
	}
	
	public static List< ContactInfo > getAllContacts() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "contact" );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		List< ContactInfo > result = new ArrayList< ContactInfo >();
		while( iterator.hasNext() ) {
			Entity entity = iterator.next();
			ContactInfo contact = new ContactInfo( 
					new User( (String)entity.getProperty( "owner" ) ), 
					new User( (String)entity.getProperty( "friend" ) ) );
			result.add( contact );
		}
		return result;
	}
}
