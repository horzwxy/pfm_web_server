package me.horzwxy.app.pfm.server.model;

import java.util.Date;
import java.util.List;
import java.util.HashMap;


public class DiningInfo {
	
	private String restaurant;
	private Date date;
	private int cost;
	private List< User > participants;
	private HashMap< User, Integer > specialCostMap;
	private HashMap< User, Integer > paidMap;
	private User author;
	private DiningInfoType state;
	
	public DiningInfo() {
		
	}
}
