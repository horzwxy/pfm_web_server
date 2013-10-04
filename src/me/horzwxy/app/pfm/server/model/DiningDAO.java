package me.horzwxy.app.pfm.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import me.horzwxy.app.pfm.model.data.Cost;
import me.horzwxy.app.pfm.model.data.CostList;
import me.horzwxy.app.pfm.model.data.Dining;
import me.horzwxy.app.pfm.model.data.Restaurant;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.model.data.UserList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class DiningDAO {
	
	public static ArrayList< Dining > getOnesDinings( User user ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "diningApproval" ).setAncestor( UserDAO.getKey( user ) );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		ArrayList< Dining > dinings = new ArrayList< Dining >();
		while( iterator.hasNext() ) {
			Entity entity = iterator.next();
			long diningId = entity.getKey().getId();
			Dining dining = getDining( diningId );
			dinings.add( dining );
		}
		return dinings;
	}
	
	public static Dining getDining( long diningId ) {
		return createDining( getEntity( diningId ) );
	}
	
	private static Entity getEntity( long id ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey( "dining", id );
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "dining" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
	
	public static Key getKey( long id ) {
		Entity entity = getEntity( id );
		if( entity == null ) {
			return null;
		}
		return entity.getKey();
	}
	
	public static List< Dining > getAllDiningInfo() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "dining" );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		List< Dining > result = new ArrayList< Dining >();
		while( iterator.hasNext() ) {
			result.add( createDining( iterator.next() ) );
		}
		return result;
	}
	
	public static long update( Dining diningInfo ) {
		Entity entity = createEntity( diningInfo );	// always create new dining, no need to search
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
		return entity.getKey().getId();
	}
	
	public static void update( Dining diningInfo, Dining.DiningState newState ) {
		Entity entity = getEntity( diningInfo.id );
		entity.setProperty( "state", newState.toString() );
		DatastoreServiceFactory.getDatastoreService().put( entity );
	}
	
	private static Entity createEntity( Dining diningInfo ) {
		Entity entity = new Entity( "dining", getAvailableId() );
		
		entity.setProperty( "restaurant", diningInfo.restaurant.name );
		entity.setProperty( "date", diningInfo.date );
		entity.setProperty( "cost", diningInfo.cost.cost );
		entity.setProperty( "participants", diningInfo.participants.toJsonString() );
		entity.setProperty( "specialCosts", diningInfo.specialCosts.toJsonString() );
		entity.setProperty( "paids", diningInfo.paids.toJsonString() );
		entity.setProperty( "author", diningInfo.author.nickname );
		entity.setProperty( "state", diningInfo.state.toString() );
		
		return entity;
	}
	
	private static Dining createDining( Entity entity ) {
		Dining dining = new Dining();
		dining.id = entity.getKey().getId();
		dining.restaurant = new Restaurant( ( String )entity.getProperty( "restaurant" ) );
		dining.date = ( Date )entity.getProperty( "date" );
		dining.cost = new Cost( ( ( Long )entity.getProperty( "cost" ) ).intValue(), null );
		dining.participants = UserList.fromJsonString( ( String )entity.getProperty( "participants" ) );
		dining.specialCosts = CostList.fromJsonString( ( String )entity.getProperty( "specialCosts" ) );
		dining.paids = CostList.fromJsonString( ( String ) entity.getProperty( "paids" ) );
		dining.author = new User( ( String )entity.getProperty( "author" ) );
		dining.state = Dining.DiningState.valueOf( ( String )entity.getProperty( "state" ) );
		return dining;
	}
	
	private static long getAvailableId() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey( "diningId", 1 );
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "diningId" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		long id = 1l;
		Entity entity = pQuery.asSingleEntity();
		if( entity == null ) {
			entity = new Entity( "diningId", 1 );
		}
		else {
			id = (long)entity.getProperty( "value" );
		}
		entity.setProperty( "value", id + 1 );
		service.put( entity );
		return id;
	}
}
