package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import me.horzwxy.app.pfm.server.model.User;
import me.horzwxy.app.pfm.server.tool.EntityFactory;

public class LogInServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{

			String accountName = req.getParameter( "aname" );
			Entity entity = EntityFactory.createUserEntity( new User( accountName ) );
			
			DatastoreService service = DatastoreServiceFactory.getDatastoreService();
			
			Query query = new Query( "aname" );
			PreparedQuery pQuery = service.prepare( query );
			
			resp.setContentType("text/plain");
			
			Iterator<Entity> qResult = pQuery.asIterable().iterator();
			if( !qResult.hasNext() ) {
				resp.getWriter().write( createResponseXML( LogInResponse.SUCCESS_BUT_FIRST, null ) );
			}
			else {
				Entity resultEntity = qResult.next();
				resp.getWriter().write( createResponseXML( LogInResponse.SUCCESS, (String)resultEntity.getProperty( "nickname" ) ) );
			}
		} catch ( Exception e ) {
			resp.getWriter().write( createResponseXML( LogInResponse.FAILED, null ) );
		}
	}
	
	private static String createResponseXML( LogInResponse responseType, String nickname ) {
		StringBuffer resultBuffer = new StringBuffer();
		resultBuffer.append( "<login_response>" );
		resultBuffer.append( "<type>" + responseType.toString() + "</type>" );
		resultBuffer.append( "<nickname>" + nickname + "</nickname>" );
		resultBuffer.append( "</login_response>" );
		return resultBuffer.substring( 0 );
	}
	
	enum LogInResponse {
        SUCCESS,
        FAILED,
        SUCCESS_BUT_FIRST;
    }
}
