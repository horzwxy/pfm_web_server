package me.horzwxy.app.pfm.server.tool;

import me.horzwxy.app.pfm.model.DiningInfo;
import me.horzwxy.app.pfm.model.TransactionInfo;
import me.horzwxy.app.pfm.model.User;

import com.google.appengine.api.datastore.Entity;

public class EntityFactory {
	
	public static Entity createUserEntity( User user ) {
		Entity result = new Entity( "user" );
		result.setProperty( "email", user.email );
		if( user.nickname != null ) {
			result.setProperty( "nickname", user.nickname );
		}
		return result;
	}
	
	public static Entity createDiningInfoEntity( DiningInfo info ) {
		Entity result = new Entity( "" );
		return null;
	}
	
	public static Entity createTransactionInfoEntity( TransactionInfo info ) {
		return null;
	}
}
