package me.horzwxy.app.pfm.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import me.horzwxy.app.pfm.model.Dining;
import me.horzwxy.app.pfm.model.User;

public class DiningDAO {
	
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
	
	public static void update( Dining diningInfo ) {
		Entity entity = createEntity( diningInfo );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
	}
	
	private static Entity createEntity( Dining diningInfo ) {
		Entity entity = new Entity( "dining" );
		
		entity.setProperty( "id", ( int )diningInfo.id );
		entity.setProperty( "restaurant", diningInfo.restaurant );
		entity.setProperty( "date", diningInfo.date );
		entity.setProperty( "cost", diningInfo.cost );
		entity.setProperty( "participants", diningInfo.participantsToString() );
		entity.setProperty( "specialCosts", diningInfo.userCostMapToString( diningInfo.specialCosts ) );
		entity.setProperty( "paid", diningInfo.userCostMapToString( diningInfo.paids ) );
		entity.setProperty( "author", diningInfo.author.nickname );
		entity.setProperty( "state", diningInfo.state.toString() );
		
		return entity;
	}
	
	private static Dining createDining( Entity entity ) {
		Dining dining = new Dining( ( ( Long )entity.getProperty( "id" ) ).intValue() );
		dining.restaurant = ( String )entity.getProperty( "restaurant" );
		dining.date = ( Date )entity.getProperty( "date" );
		dining.cost = ( ( Long )entity.getProperty( "cost" ) ).intValue();
		dining.participants = ( ArrayList< User > ) Dining.getParticipantsFromString( ( String )entity.getProperty( "participants" ) );
		dining.specialCosts = ( Map< User, Integer > ) Dining.getUserCostMapFromString( ( String )entity.getProperty( "specialCosts" ) );
		dining.paids = ( Map< User, Integer > ) Dining.getUserCostMapFromString( ( String ) entity.getProperty( "paids" ) );
		dining.author = new User( null, ( String )entity.getProperty( "author" ) );
		dining.state = Dining.DiningInfoState.valueOf( ( String )entity.getProperty( "state" ) );
		return dining;
	}
}
