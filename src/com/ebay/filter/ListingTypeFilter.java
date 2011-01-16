package com.ebay.filter;

import java.util.ArrayList;
import java.util.HashMap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class ListingTypeFilter extends ItemFilter{

	public final static String TYPE_ADFORMAT = "AdFormat";
	public final static String TYPE_AUCTION  = "Auction";
	public final static String TYPE_AUCTION_WITH_BIN = "AuctionWithBIN";
	public final static String TYPE_CLASSIFIED  = "Classified";
	public final static String TYPE_FIXED_PRICE = "FixedPrice";
	public final static String TYPE_STORE_INVENTORY = "StoreInventory";
	
	private final static String NAME = "ListingType";
	
	private ArrayList<String> types;

	public ListingTypeFilter(){
	  this.types = new ArrayList<String>();	
	}
	
	public String[] getTypes() {
		String[] types = new String[this.types.size()];
		types = this.types.toArray(types);
	 return types;	
	}

	public void setTypes(String[] type) {
		if(type != null){
		 types = new ArrayList<String>();	
	     for(String temp : type)
	    	this.types.add(temp);
		} 
	}
	
	public void addType(String type){
		this.types.add(type);
	}
	
	public boolean removeType(String type){	
	 return this.types.remove(type);	 
	}

	@Override
	public HashMap<String, String[]> getFilterMap() {
		
		if(this.types.size() == 0)
	      return null;		
		
		final String[] values = getTypes();
		final HashMap<String,String[]> map = new HashMap<String, String[]>();
		map.put(NAME,values);
		
	 return map;	
	}

	@Override
	public boolean isActive() {
		return !this.types.isEmpty();
	}
	
}
