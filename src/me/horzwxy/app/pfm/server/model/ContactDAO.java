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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class ContactDAO {

	public static void update( ContactInfo info ) {
		Entity entity = createEntity( info );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
	}
	
	private static Entity createEntity( ContactInfo info ) {
		Key userKey = UserDAO.getKey( info.owner );
		Entity entity = new Entity( "contact", info.friend.nickname, userKey );
		return entity;
	}
	
	private static ContactInfo createContactInfo( Entity entity ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "user" ).setAncestor( entity.getParent() );
		PreparedQuery pQuery = service.prepare( query );
		ContactInfo contact = new ContactInfo( 
				new User( pQuery.asSingleEntity().getKey().getName() ), 
				createUser( entity ) );
		return contact;
	}
	
	private static User createUser( Entity contactEntity ) {
		return new User( contactEntity.getKey().getName() );
	}
	
	public static UserList getOnesContacts( User user ) {
		UserList result = new UserList();
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "contact" ).setAncestor( UserDAO.getKey( user ) );
		PreparedQuery pQuery = service.prepare( query );
		Iterable< Entity > iterable = pQuery.asIterable();
		for( Entity entity : iterable ) {
			result.add( createUser( entity ) );
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
			result.add( createContactInfo( entity ) );
		}
		return result;
	}
}
