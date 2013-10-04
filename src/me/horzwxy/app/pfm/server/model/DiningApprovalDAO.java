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
			return new DiningApproval( ( int )thisKey.getId(), new User( parentKey.getName() ), state );
		}
		else {
			return new DiningApproval( ( int )parentKey.getId(), new User( thisKey.getName() ), state );
		}
	}
	
	public static void distribute( Dining dining ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		UserList list = dining.participants;
		for( User user : list ) {
			DiningApproval da = new DiningApproval( dining.id, user, DiningState.NOT_APPROVED_YET );
			Entity entityUnderUser = createEntity( user, da );
			System.out.println( "user = " + user.nickname + " diningId = " + da.diningId );
			System.out.println( "under user id =" + service.put( entityUnderUser ) );
			Entity entityUnderDining = createEntity( dining.id, da );
			System.out.println( "under dining name = " + service.put( entityUnderDining ) );
		}
	}
	
	public static void update( DiningApproval da ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entityUnderUser = getEntity( da.user.nickname, da );
		entityUnderUser.setProperty( "state", da.state.toString() );
		Entity entityUnderDining = getEntity( da.diningId, da );
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
		Key key = KeyFactory.createKey( "diningApproval", nickname );
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "dining" ).setAncestor( DiningDAO.getKey( da.diningId ) ).setFilter( filter );
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
		Key key = KeyFactory.createKey( "diningApproval", id );
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "dining" ).setAncestor( UserDAO.getKey( da.user ) ).setFilter( filter );
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
