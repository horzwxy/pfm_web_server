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
		Query query = new Query( "bill" ).setAncestor( parentKey );
		PreparedQuery pQuery = service.prepare( query );
		ArrayList< Bill > result = new ArrayList< Bill >();
		Iterator< Entity > iterator = pQuery.asIterator();
		while( iterator.hasNext() ) {
			result.add( createBill( iterator.next() ) );
		}
		return result;
	}
	
	public static void update( Bill bill ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( createEntity( bill.lender.nickname, bill ) );
		if( bill.borrower.nickname != bill.lender.nickname ) {
			service.put( createEntity( bill.borrower.nickname, bill ) );
		}
	}
	
	private static Bill createBill( Entity entity ) {
		String lenderNickname = (String)entity.getProperty( "lender" );
		String borrowerNickname = (String)entity.getProperty( "borrower" );
		int cost = ( int )( (long)entity.getProperty( "cost" ) );
		BillState state = BillState.valueOf( (String)entity.getProperty( "state" ) );
		return new Bill( lenderNickname, borrowerNickname, cost, entity.getKey().getId(), state );
	}
	
	/**
	 * Create Bill entity under a certain User entity
	 * @param nickname user's nickname
	 * @param bill
	 * @return
	 */
	private static Entity createEntity( String nickname, Bill bill ) {
		Key parentKey = KeyFactory.createKey( "user", nickname );
		Entity entity = new Entity( "bill", bill.id + "/" + bill.borrower.nickname, parentKey );
		entity.setProperty( "lender", bill.lender.nickname );
		entity.setProperty( "borrower", bill.borrower.nickname );
		entity.setProperty( "cost", bill.cost.cost );
		entity.setProperty( "state", bill.state.toString() );
		return entity;
	}
}
