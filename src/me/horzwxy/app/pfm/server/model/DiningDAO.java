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
	
//	public void updateDining( Dining diningInfo ) {
//		Entity entity = getDiningById( diningInfo.id );
//		if( entity == null ) {
//			entity = new Entity( "dining" );
//		}
//		entity.setProperty( "id", diningInfo.id );
//		entity.setProperty( "restaurant", diningInfo.restaurant );
//		entity.setProperty( "date", diningInfo.date );
//		entity.setProperty( "cost", diningInfo.cost );
//		entity.setProperty( "participants", diningInfo.participants );
//		entity.setProperty( "specialCosts", diningInfo.specialCosts );
//		entity.setProperty( "paid", diningInfo.paids );
//		entity.setProperty( "author", diningInfo.author );
//		entity.setProperty( "state", diningInfo.state );
//		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
//		service.put( entity );
//	}
	
	public List< Dining > getDiningById( int id ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate( "id", FilterOperator.EQUAL, id );
		Query query = new Query( "dining" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		
		List< Dining > result = new ArrayList< Dining >();
		Iterator< Entity > iterator = pQuery.asIterator();
		while( iterator.hasNext() ) {
			Entity entity = iterator.next();
			Dining dining = new Dining( ( int )entity.getProperty( "id" ) );
			dining.restaurant = ( String )entity.getProperty( "restaurant" );
			dining.date = ( Date )entity.getProperty( "date" );
			dining.cost = ( int )entity.getProperty( "cost" );
			dining.participants = ( ArrayList< User > ) entity.getProperty( "participants" );
			dining.specialCosts = ( Map< User, Integer > ) entity.getProperty( "specialCosts" );
			dining.paids = ( Map< User, Integer > ) entity.getProperty( "paids" );
			dining.author = ( User ) entity.getProperty( "author" );
			dining.state = ( Dining.DiningInfoState ) entity.getProperty( "state" );
			
			result.add( dining );
		}
		return result;
	}
	
	public List< Dining > getOnesDining( User user ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate( "owner", FilterOperator.EQUAL, user.nickname );
		Query query = new Query( "dining" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		
		List< Dining > result = new ArrayList< Dining >();
		Iterator< Entity > iterator = pQuery.asIterator();
		while( iterator.hasNext() ) {
			Entity entity = iterator.next();
			Dining dining = new Dining( ( int )entity.getProperty( "id" ) );
			dining.restaurant = ( String )entity.getProperty( "restaurant" );
			dining.date = ( Date )entity.getProperty( "date" );
			dining.cost = ( int )entity.getProperty( "cost" );
			dining.participants = ( ArrayList< User > ) entity.getProperty( "participants" );
			dining.specialCosts = ( Map< User, Integer > ) entity.getProperty( "specialCosts" );
			dining.paids = ( Map< User, Integer > ) entity.getProperty( "paids" );
			dining.author = ( User ) entity.getProperty( "author" );
			dining.state = ( Dining.DiningInfoState ) entity.getProperty( "state" );
			
			result.add( dining );
		}
		return result;
	}

	public void createAndDistribute( Dining diningInfo ) {
		Entity entity = new Entity( "dining" );
		
		entity.setProperty( "id", diningInfo.id );
		entity.setProperty( "restaurant", diningInfo.restaurant );
		entity.setProperty( "date", diningInfo.date );
		entity.setProperty( "cost", diningInfo.cost );
		entity.setProperty( "participants", diningInfo.participants );
		entity.setProperty( "specialCosts", diningInfo.specialCosts );
		entity.setProperty( "paid", diningInfo.paids );
		entity.setProperty( "author", diningInfo.author );
		entity.setProperty( "state", diningInfo.state );
		
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
		
		for( User user : diningInfo.participants ) {
			distribute( diningInfo, user );
		}
	}
	
	private void distribute( Dining diningInfo, User user ) {
		Entity entity = new Entity( "dining" );
		
		entity.setProperty( "owner", user.nickname );
		entity.setProperty( "id", diningInfo.id );
		entity.setProperty( "restaurant", diningInfo.restaurant );
		entity.setProperty( "date", diningInfo.date );
		entity.setProperty( "cost", diningInfo.cost );
		entity.setProperty( "participants", diningInfo.participants );
		entity.setProperty( "specialCosts", diningInfo.specialCosts );
		entity.setProperty( "paid", diningInfo.paids );
		entity.setProperty( "author", diningInfo.author );
		entity.setProperty( "state", diningInfo.state );
		
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( entity );
	}
}
