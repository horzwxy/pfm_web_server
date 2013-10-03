package me.horzwxy.app.pfm.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import me.horzwxy.app.pfm.model.data.Cost;
import me.horzwxy.app.pfm.model.data.Dining;
import me.horzwxy.app.pfm.model.data.Restaurant;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.model.data.UserCostMap;
import me.horzwxy.app.pfm.model.data.UserList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

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
		Entity entity = createEntity( diningInfo );	// always create new dining, no need to search
		if( diningInfo.id == -1 ) {
			entity.setProperty( "id", getAvailableId() );
		}
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
	}
	
	private static Entity createEntity( Dining diningInfo ) {
		Entity entity = new Entity( "dining" );
		
		entity.setProperty( "id", ( int )diningInfo.id );
		entity.setProperty( "restaurant", diningInfo.restaurant.name );
		entity.setProperty( "date", diningInfo.date );
		entity.setProperty( "cost", diningInfo.cost.cost );
		entity.setProperty( "participants", diningInfo.participants.toJsonString() );
		entity.setProperty( "specialCosts", diningInfo.specialCosts.toJsonString() );
		entity.setProperty( "paid", diningInfo.paids.toJsonString() );
		entity.setProperty( "author", diningInfo.author.nickname );
		entity.setProperty( "state", diningInfo.state.toString() );
		
		return entity;
	}
	
	private static Dining createDining( Entity entity ) {
		Dining dining = new Dining();
		dining.id = ( ( Long )entity.getProperty( "id" ) ).intValue();
		dining.restaurant = new Restaurant( ( String )entity.getProperty( "restaurant" ) );
		dining.date = ( Date )entity.getProperty( "date" );
		dining.cost = new Cost( ( ( Long )entity.getProperty( "cost" ) ).intValue() );
		dining.participants = UserList.fromJsonString( ( String )entity.getProperty( "participants" ) );
		dining.specialCosts = UserCostMap.fromJsonString( ( String )entity.getProperty( "specialCosts" ) );
		dining.paids = UserCostMap.fromJsonString( ( String ) entity.getProperty( "paids" ) );
		dining.author = new User( ( String )entity.getProperty( "author" ) );
		dining.state = Dining.DiningState.valueOf( ( String )entity.getProperty( "state" ) );
		return dining;
	}
	
	private static int getAvailableId() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "diningId" );
		PreparedQuery pQuery = service.prepare( query );
		Entity entity = pQuery.asSingleEntity();
		if( entity == null ) {
			entity = new Entity( "diningId" );
			entity.setProperty( "value", 1 );
			service.put( entity );
			return 1;
		}
		int id = ( ( Long )entity.getProperty( "value" ) ).intValue();
		entity.setProperty( "value", ++id );
		service.put( entity );
		return --id;
	}
}
