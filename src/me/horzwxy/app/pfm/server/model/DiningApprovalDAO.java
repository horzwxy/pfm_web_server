package me.horzwxy.app.pfm.server.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import me.horzwxy.app.pfm.model.data.Dining;
import me.horzwxy.app.pfm.model.data.Dining.DiningState;
import me.horzwxy.app.pfm.model.data.DiningApproval;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.model.data.UserList;

public class DiningApprovalDAO {
	
	public static Dining.DiningState checkState( Dining dining ) {
		ArrayList< DiningApproval > list = getDaForOneDining( dining );
		Dining.DiningState newState = Dining.DiningState.APPROVED;
		for( DiningApproval da : list ) {
			if( da.state == Dining.DiningState.REJECTED ) {
				newState = Dining.DiningState.REJECTED;
				break;
			}
			else if( da.state == Dining.DiningState.NOT_APPROVED_YET ) {
				newState = Dining.DiningState.NOT_APPROVED_YET;
			}
		}
		return newState;
	}
	
	private static ArrayList< DiningApproval > getDaForOneDining( Dining dining ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key diningKey = KeyFactory.createKey( "dining", dining.id );
		Query query = new Query( "diningApproval" ).setAncestor( diningKey );
		PreparedQuery pQuery = service.prepare( query );
		ArrayList< DiningApproval > result = new ArrayList< DiningApproval >();
		Iterator< Entity> iterator = pQuery.asIterator();
		while( iterator.hasNext() ) {
			result.add( createDa( iterator.next() ) );
		}
		return result;
	}
	
	public static ArrayList< DiningApproval > getAllDas() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "diningApproval" );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		ArrayList< DiningApproval > result = new ArrayList< DiningApproval >();
		while( iterator.hasNext() ) {
			result.add( createDa( iterator.next() ) );
		}
		return result;
	}
	
	private static DiningApproval createDa( Entity entity ) {
		Key thisKey = entity.getKey();
		Key parentKey = entity.getParent();
		DiningState state = DiningState.valueOf( (String)entity.getProperty( "state" ) );
		// if id is diningId
		if( thisKey.getName() == null ) {
			return new DiningApproval( thisKey.getId(), new User( parentKey.getName() ), state );
		}
		else {
			return new DiningApproval( parentKey.getId(), new User( thisKey.getName() ), state );
		}
	}
	
	/**
	 * Create DiningApproval instances for the new dining record.
	 * There are two kinds of instance to be created. One is under the ancestorship of a User entity, \
	 * which is a bound between a participant and the Dining entity. \
	 * The other one is under the Dining entity, which is used to determine the Dining record state. \
	 * For example, two users are in the record. Either of them may approve or reject the record. \
	 * It's inconvenient to determine the dining state from a Dining entity without DiningApproval instances attached to it, \
	 * since it needs to see who are the participants and looks into the user-diningApproval records.
	 * It may be a little complicated to reserve two kinds of DiningApproval, especially for maintaining consistency. But it is useful for searching.
	 * @param dining
	 */
	public static void distribute( Dining dining ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		UserList list = dining.participants;
		for( User user : list ) {
			DiningApproval da = new DiningApproval( dining.id, user, DiningState.NOT_APPROVED_YET );
			Entity entityUnderUser = createEntity( dining.id, da );
			Entity entityUnderDining = createEntity( user, da );
			service.put( entityUnderUser );
			service.put( entityUnderDining );
		}
	}
	
	public static void update( DiningApproval da ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entityUnderUser = getEntity( da.diningId, da );
		entityUnderUser.setProperty( "state", da.state.toString() );
		Entity entityUnderDining = getEntity( da.user.nickname, da );
		entityUnderDining.setProperty( "state", da.state.toString() );
		service.put( entityUnderUser );
		service.put( entityUnderDining );
	}
	
	/**
	 * Use nickname as key, Dining Entity instance as ancestor
	 * @param nickname
	 * @param da
	 * @return
	 */
	private static Entity getEntity( String nickname, DiningApproval da ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey( DiningDAO.getKey( da.diningId ), "diningApproval", nickname );
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "diningApproval" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		Entity entity = pQuery.asSingleEntity();
		return entity;
	}
	
	/**
	 * Use diningId as key, User Entity instance as ancestor
	 * @param nickname
	 * @param da
	 * @return
	 */
	private static Entity getEntity( long id, DiningApproval da ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey( UserDAO.getKey( da.user ), "diningApproval", id );
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "diningApproval" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		Entity entity = pQuery.asSingleEntity();
		return entity;
	}
	
	/**
	 * Use nickname as key, Dining Entity instance as ancestor
	 * @param user
	 * @param da
	 * @return
	 */
	private static Entity createEntity( User user, DiningApproval da ) {
		Entity entity = new Entity( "diningApproval", user.nickname, DiningDAO.getKey( da.diningId ) );
		entity.setProperty( "state", da.state.toString() );
		return entity;
	}
	
	/**
	 * Use diningId as key, User Entity instance as ancestor
	 * @param id
	 * @param da
	 * @return
	 */
	private static Entity createEntity( long id, DiningApproval da ) {
		Entity entity = new Entity( "diningApproval", id, UserDAO.getKey( da.user ) );
		entity.setProperty( "state", da.state.toString() );
		return entity;
	}
}
