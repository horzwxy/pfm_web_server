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

import me.horzwxy.app.pfm.model.data.Bill;
import me.horzwxy.app.pfm.model.data.Bill.BillState;
import me.horzwxy.app.pfm.model.data.User;

public class BillDAO {
	
	public static ArrayList< Bill > getAllBills() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "bill" );
		PreparedQuery pQuery = service.prepare( query );
		ArrayList< Bill > result = new ArrayList< Bill >();
		Iterator< Entity > iterator = pQuery.asIterator();
		while( iterator.hasNext() ) {
			result.add( createBill( iterator.next() ) );
		}
		return result;
	}
	
	public static ArrayList< Bill > getOnesBills( User user ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key parentKey = KeyFactory.createKey( "user", user.nickname );
		Query query = new Query( "billApproval" ).setAncestor( parentKey );
		PreparedQuery pQuery = service.prepare( query );
		ArrayList< Bill > result = new ArrayList< Bill >();
		Iterator< Entity > iterator = pQuery.asIterator();
		while( iterator.hasNext() ) {
			Entity entity = iterator.next();
			Key key = KeyFactory.createKey( "bill", entity.getKey().getId() );
			Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
	                Query.FilterOperator.EQUAL,
	                key );
			Query billQuery = new Query( "bill" ).setFilter( filter );
			PreparedQuery billPQuery = service.prepare( billQuery );
			result.add( createBill( billPQuery.asSingleEntity() ) );
		}
		return result;
	}
	
	public static long update( Bill bill ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entity = getEntity( bill );
		if( entity == null ) {
			entity = createEntity( bill );
		}
		entity.setProperty( "state", bill.state.toString() );
		long id = service.put( entity ).getId();
		return id;
	}
	
	public static void update( long billId, BillState newState ) {
		Entity entity = getEntity( billId );
		Bill bill = createBill( entity );
		bill.state = newState;
		update( bill );
	}
	
	public static Entity getEntity( Bill bill ) {
		return getEntity( bill.billId );
	}
	
	private static Entity getEntity( long billId ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey( "bill", billId );
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "bill" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
	
	private static Bill createBill( Entity entity ) {
		String lenderNickname = (String)entity.getProperty( "lender" );
		String borrowerNickname = (String)entity.getProperty( "borrower" );
		int cost = ( int )( (long)entity.getProperty( "cost" ) );
		BillState state = BillState.valueOf( (String)entity.getProperty( "state" ) );
		return new Bill( entity.getKey().getId(), lenderNickname, borrowerNickname, cost, (long)entity.getProperty( "diningId" ), state );
	}
	
	/**
	 * Create Bill entity under a certain User entity
	 * @param nickname user's nickname
	 * @param bill
	 * @return
	 */
	private static Entity createEntity( Bill bill ) {
		Entity entity = new Entity( "bill" );
		entity.setProperty( "lender", bill.lender.nickname );
		entity.setProperty( "borrower", bill.borrower.nickname );
		entity.setProperty( "cost", bill.cost.cost );
		entity.setProperty( "state", bill.state.toString() );
		entity.setProperty( "diningId", bill.diningId );
		return entity;
	}
}
