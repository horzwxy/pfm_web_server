package me.horzwxy.app.pfm.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.horzwxy.app.pfm.model.communication.ApproveDiningRequest;
import me.horzwxy.app.pfm.model.communication.ApproveDiningResponse.ResultType;
import me.horzwxy.app.pfm.model.communication.ApproveDiningResponse;
import me.horzwxy.app.pfm.model.data.Bill;
import me.horzwxy.app.pfm.model.data.Cost;
import me.horzwxy.app.pfm.model.data.CostList;
import me.horzwxy.app.pfm.model.data.Dining;
import me.horzwxy.app.pfm.model.data.User;
import me.horzwxy.app.pfm.model.data.UserList;
import me.horzwxy.app.pfm.server.model.BillDAO;
import me.horzwxy.app.pfm.server.model.DiningApprovalDAO;
import me.horzwxy.app.pfm.server.model.DiningDAO;

public class ApproveDiningServlet extends PFMServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ApproveDiningRequest request = getRequest( req, ApproveDiningRequest.class );
		DiningApprovalDAO.update( request.getDiningApproval() );
		Dining.DiningState newState = DiningApprovalDAO.checkState( request.getDining() );
		if( newState != Dining.DiningState.NOT_APPROVED_YET ) {
			DiningDAO.update( request.getDining(), newState );
		}
		if( newState == Dining.DiningState.APPROVED ) {
			ArrayList< Bill > bills = getBills( DiningDAO.getDining( request.getDining().id ) );
			for( Bill bill : bills ) {
				BillDAO.update( bill );
			}
		}
		resp.getWriter().println( new ApproveDiningResponse( ResultType.SUCCESS ) );
	}
	
	private static ArrayList< Bill > getBills( Dining dining ) {
		ArrayList< Bill > billList = new ArrayList< Bill >();
		UserList participants = dining.participants;
		CostList specialCosts = dining.specialCosts;
		CostList paids = dining.paids;
		Cost totalCost = dining.cost;
		int dividedCost = totalCost.cost;
		HashMap< String, Cost > paidMap = new HashMap< String, Cost >();
		for( User user : participants ) {
			paidMap.put( user.nickname, new Cost( 0, user.nickname ) );
		}
		for( Cost cost : specialCosts ) {
			dividedCost -= cost.cost;
			paidMap.get( cost.nickname ).cost -= cost.cost;
		}
		dividedCost /= participants.size();
		for( Cost paid : paids ) {
			paidMap.get( paid.nickname ).cost += paid.cost;
		}
		TreeSet<Cost> canGetSet = new TreeSet<Cost>( new Cost.CostComparator() );
		TreeSet<Cost> shouldPaySet = new TreeSet<Cost>( new Cost.CostComparator() );
		for( User user : participants ) {
			int result = paidMap.get( user.nickname ).cost;
			result -= dividedCost;
			Cost costResult = paidMap.get( user.nickname );
			costResult.cost = result;
			// can get
			if( result > 0 ) {
				canGetSet.add( costResult );
			}
			else if( result < 0 ) {
				shouldPaySet.add( costResult );
			}
			System.out.println( "nickname=" + user.nickname + "  should get paids=" + result );
		}
		Cost payment = shouldPaySet.pollFirst();
		while( payment != null ) {
			Cost biggestGet = canGetSet.pollLast();
			int plusResult = biggestGet.cost + payment.cost;
			// get is greater
			if( plusResult > 0 ) {
				biggestGet.cost = plusResult;
				canGetSet.add( biggestGet );
				billList.add( new Bill( biggestGet.nickname, payment.nickname, -payment.cost, dining.id ) );
			}
			// exactly cleared
			else if( plusResult == 0 ) {
				billList.add( new Bill( biggestGet.nickname, payment.nickname, -payment.cost, dining.id ) );
			}
			// need more 'canGet's
			else {
				int totalGet = biggestGet.cost;
				while( totalGet + payment.cost < 0 ) {
					billList.add( new Bill( biggestGet.nickname, payment.nickname, biggestGet.cost, dining.id ) );
					biggestGet = canGetSet.pollLast();
					totalGet += biggestGet.cost;
				}
				billList.add( new Bill( biggestGet.nickname, payment.nickname, biggestGet.cost, dining.id ) );
				if( totalGet + payment.cost > 0 ) {
					biggestGet.cost = totalGet + payment.cost;
					canGetSet.add( biggestGet );
				}
			}
		}
		assert shouldPaySet.size() == 0;
		// should learn from Lei Feng!
		for( Cost canGet : canGetSet ) {
			billList.add( new Bill( canGet.nickname, canGet.nickname, canGet.cost, dining.id ) );
		}
		return billList;
	}
}
