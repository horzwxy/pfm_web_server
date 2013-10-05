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
import me.horzwxy.app.pfm.model.data.BillApproval;
import me.horzwxy.app.pfm.model.data.User;

public class BillApprovalDAO {
	
	public static ArrayList< BillApproval > getAllBas() {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "billApproval" );
		PreparedQuery pQuery = service.prepare( query );
		ArrayList< BillApproval > result = new ArrayList< BillApproval >();
		Iterator< Entity > iterator = pQuery.asIterator();
		while( iterator.hasNext() ) {
			result.add( createBa( iterator.next() ) );
		}
		return result;
	}
	
	public static void update( BillApproval ba ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entity = getEntity( ba.user.nickname, ba.diningId );
		entity.setProperty( "state", ba.state );
		service.put( entity );
	}
	
	public static void distribute( Bill bill ) {
		BillApproval baUnderLender = new BillApproval( bill.lender, bill.id );
		BillApproval baUnderBorrower = new BillApproval( bill.borrower, bill.id );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( createEntity( baUnderLender ) );
		service.put( createEntity( baUnderBorrower ) );
	}
	
	private static Entity getEntity( String nickname, long diningId ) {
		Key parentKey = KeyFactory.createKey( "user", nickname );
		Key key = KeyFactory.createKey( parentKey, "billApproval", diningId );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "billApproval" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
	
	private static Entity createEntity( BillApproval ba ) {
		Key parentKey = KeyFactory.createKey( "user", ba.user.nickname );
		Entity entity = new Entity( "billApproval", ba.diningId, parentKey );
		entity.setProperty( "state", ba.state );
		return entity;
	}
	
	private static BillApproval createBa( Entity entity ) {
		String nickname = ( String )entity.getProperty( "nickname" );
		Bill.BillState state = Bill.BillState.valueOf( ( String )entity.getProperty( "state" ) );
		return new BillApproval( new User( nickname ), entity.getKey().getId(), state );
	}
}
