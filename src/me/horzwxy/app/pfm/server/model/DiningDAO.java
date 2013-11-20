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

/**
 * The Dining entity access interface.
 * Dining entity has a key of (long)diningId, which is arranged by an accumulated value in database.
 * @see DiningDAO.getAvailableId()
 * User cannot search for dining entities directly. They can only access DiningApproval entities, which encapsulate reference to the Dining entity.
 * @see DiningApprovalDAO 
 * 
 * @author horz
 * @version v0.99 There may be some bugs here.
 */
public class DiningDAO {
	
	/**
	 * Get all dining records which has recorded that the user is in the participants list.
	 * @param user
	 * @return
	 */
	public static ArrayList< Dining > getOnesDinings( User user ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "diningApproval" ).setAncestor( UserDAO.getKey( user ) );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		ArrayList< Dining > dinings = new ArrayList< Dining >();
		while( iterator.hasNext() ) {
			Entity entity = iterator.next();
			long diningId = entity.getKey().getId();
			// if inconsistency exists between dining record and DiningApproval record, a NullPointer exception will take place
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
	
	/**
	 * To create a new Dining record and insert it into database.
	 * Notice that the generated id is returned by the function, but not put into the dining model instance.
	 * 
	 * @param diningInfo the model of Dining entity, only lack of id
	 * @return the dining id for the Dining entity
	 */
	public static long update( Dining diningInfo ) {
		Entity entity = createEntity( diningInfo );	// always create new dining, no need to search
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
		return entity.getKey().getId();
	}
	
	/**
	 * To update the state of some Dining entity.
	 * Usually it is called when someone approves\rejects a Dining record.
	 * @param diningInfo
	 * @param newState
	 */
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
	
	/**
	 * Every dining info has a unique id. This is the function to generate the id.
	 * This function stores the available id in another place in database. Every time the caller needs it, \
	 * the function retrieve the value and put it back with additional ONE.
	 * So there is a case that for some value of id, there is no corresponding dining entity.
	 * 
	 * @version v1.0
	 * 
	 * @return available dining id in long type
	 */
	private synchronized static long getAvailableId() {
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
			// if this is the first time to retrieve the data
			entity = new Entity( "diningId", 1 );
		}
		else {
			id = (long)entity.getProperty( "value" );
		}
		// put the value back, with additional ONE for the next time
		entity.setProperty( "value", id + 1 );
		service.put( entity );
		// return the value without the additional ONE
		return id;
	}
}
