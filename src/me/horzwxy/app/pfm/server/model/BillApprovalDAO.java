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
	
	public static Bill.BillState checkBillClearState( long billId ) {
		Key parentKey = KeyFactory.createKey( "bill", billId );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "billApproval" ).setAncestor( parentKey );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		Bill.BillState result = Bill.BillState.CLEARED;
		while( iterator.hasNext() ) {
			BillApproval ba = createBa( iterator.next() );
			if( ba.state == Bill.BillState.APPROVED ) {
				result = Bill.BillState.APPROVED;
			}
		}
		return result;
	}
	
	public static Bill.BillState checkBillState( long billId ) {
		Key parentKey = KeyFactory.createKey( "bill", billId );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query( "billApproval" ).setAncestor( parentKey );
		PreparedQuery pQuery = service.prepare( query );
		Iterator< Entity > iterator = pQuery.asIterator();
		Bill.BillState result = Bill.BillState.APPROVED;
		while( iterator.hasNext() ) {
			BillApproval ba = createBa( iterator.next() );
			if( ba.state == Bill.BillState.REJECTED ) {
				return Bill.BillState.REJECTED;
			}
			else if( ba.state == Bill.BillState.NOT_APPROVED_YET ) {
				result = Bill.BillState.NOT_APPROVED_YET;
			}
		}
		return result;
	}
	
	public static void update( BillApproval ba ) {
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Entity entityUnderUser = getEntityUnderUser( ba );
		entityUnderUser.setProperty( "state", ba.state.toString() );
		service.put( entityUnderUser );
		Entity entityUnderBill = getEntityUnderBill( ba );
		entityUnderBill.setProperty( "state", ba.state.toString() );
		service.put( entityUnderBill );
	}
	
	public static void distribute( Bill bill ) {
		BillApproval baUnderLender = new BillApproval( bill.lender, bill.billId );
		BillApproval baUnderBorrower = new BillApproval( bill.borrower, bill.billId );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		service.put( createEntityUnderUser( baUnderLender ) );
		service.put( createEntityUnderUser( baUnderBorrower ) );
		service.put( createEntityUnderBill( baUnderLender ) );
		service.put( createEntityUnderBill( baUnderBorrower ) );
	}
	
	private static Entity getEntityUnderBill( BillApproval ba ) {
		Key parentKey = KeyFactory.createKey( "bill", ba.billId );
		Key key = KeyFactory.createKey( parentKey, "billApproval", ba.owner.nickname );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "billApproval" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
	
	private static Entity getEntityUnderUser( BillApproval ba ) {
		Key parentKey = KeyFactory.createKey( "user", ba.owner.nickname );
		Key key = KeyFactory.createKey( parentKey, "billApproval", ba.billId );
		DatastoreService service = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate( Entity.KEY_RESERVED_PROPERTY,
                Query.FilterOperator.EQUAL,
                key );
		Query query = new Query( "billApproval" ).setFilter( filter );
		PreparedQuery pQuery = service.prepare( query );
		return pQuery.asSingleEntity();
	}
	
	private static Entity createEntityUnderBill( BillApproval ba ) {
		Key parentKey = KeyFactory.createKey( "bill", ba.billId );
		Entity entity = new Entity( "billApproval", ba.owner.nickname, parentKey );
		entity.setProperty( "state", ba.state.toString() );
		return entity;
	}
	
	private static Entity createEntityUnderUser( BillApproval ba ) {
		Key parentKey = KeyFactory.createKey( "user", ba.owner.nickname );
		Entity entity = new Entity( "billApproval", ba.billId, parentKey );
		entity.setProperty( "state", ba.state.toString() );
		return entity;
	}
	
	private static BillApproval createBa( Entity entity ) {
		// if under user
		if( entity.getKey().getId() != 0 ) {
			String nickname = entity.getParent().getName();
			Bill.BillState state = Bill.BillState.valueOf( ( String )entity.getProperty( "state" ) );
			return new BillApproval( new User( nickname ), entity.getKey().getId(), state );
		}
		// if under bill
		else {
			String nickname = entity.getKey().getName();
			Bill.BillState state = Bill.BillState.valueOf( ( String )entity.getProperty( "state" ) );
			return new BillApproval( new User( nickname ), entity.getParent().getId(), state );
		}
	}
}
