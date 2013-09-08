package me.horzwxy.app.pfm.server.tool;

import me.horzwxy.app.pfm.server.model.DiningInfo;
import me.horzwxy.app.pfm.server.model.TransactionInfo;
import me.horzwxy.app.pfm.server.model.User;

import com.google.appengine.api.datastore.Entity;

public class EntityFactory {
	
	public static Entity createUserEntity( User user ) {
		Entity result = new Entity( "user" );
		result.setProperty( "aname", user.getAccountName() );
		if( user.getNickname() != null ) {
			result.setProperty( "nickname", user.getNickname() );
		}
		return result;
	}
	
	public static Entity createDiningInfoEntity( DiningInfo info ) {
		return null;
	}
	
	public static Entity createTransactionInfoEntity( TransactionInfo info ) {
		return null;
	}
}
