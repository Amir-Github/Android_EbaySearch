package com.amir.ebay;

import java.util.ArrayList;

import com.ebay.filter.BidFilter;
import com.ebay.filter.ConditionFilter;
import com.ebay.filter.ConditionalFilter;
import com.ebay.filter.ItemFilter;
import com.ebay.filter.ListingTypeFilter;
import com.ebay.filter.LocationFilter;
import com.ebay.filter.PriceFilter;
import com.ebay.filter.QuantityFilter;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class FilterHandler {
	
	private  ItemFilter[] itemFilters;
	private static FilterHandler instance;
	
	
	private FilterHandler(){
	  this.itemFilters = new ItemFilter[11];	
	  resetItemFilters();	
	}
	
	public static FilterHandler getInstance(){
		if(instance == null)
		  instance = new FilterHandler();
	 return instance;	
	}
	
	public synchronized ItemFilter[] getItemFilters(){
	  return this.itemFilters;	
	}
	
	public synchronized void setItemFilters(ItemFilter[] filters){
		this.itemFilters = filters;	
	}
	
	public synchronized ItemFilter getItemFilter(int filterId){
	    for(ItemFilter temp : this.itemFilters){
	       if(temp.getFilterId() == filterId){
	    	  return temp;
	       }   
	    }
	 return null;	
	}
	
	public synchronized ItemFilter[] getActiveItemFilters(){
		ArrayList<ItemFilter> filters = new ArrayList<ItemFilter>();
		
		for(ItemFilter temp : this.itemFilters){
		   if(temp.isActive())	   
			filters.add(temp);
		}
		
	   ItemFilter[] f = new ItemFilter[filters.size()];
	   f = filters.toArray(f);
	 return f;	
	}
	
	public synchronized void resetItemFilters(){
		ConditionalFilter[] cf = new ConditionalFilter[5];
		 for(int i=0;i<5;i++)
		   cf[i] = new ConditionalFilter();	
		
		cf[0].setCondition(ConditionalFilter.CONDITION_EXCLUDE_AUTO_PAY);
		cf[0].setFilterId(Constants.FILTER_ID_EXCLUDE_AUTO_PAY);
		
		cf[1].setCondition(ConditionalFilter.CONDITION_FEATURED_ONLY);
		cf[1].setFilterId(Constants.FILTER_ID_FEATURED_ONLY);
		
		cf[2].setCondition(ConditionalFilter.CONDITION_FREE_SHIPPING_ONLY);
		cf[2].setFilterId(Constants.FILTER_ID_FREE_SHIPPING_ONLY);
		
		cf[3].setCondition(ConditionalFilter.CONDITION_GET_IT_FAST_ONLY);
		cf[3].setFilterId(Constants.FILTER_ID_GET_IT_FAST_ONLY);
		
		cf[4].setCondition(ConditionalFilter.CONDITION_TOP_RATED_SELLERS);
		cf[4].setFilterId(Constants.FILTER_ID_TOP_RATED_SELLERS);
		
		BidFilter bid = new BidFilter();
		bid.setFilterId(Constants.FILTER_ID_BID);
		
		PriceFilter price = new PriceFilter();
		price.setFilterId(Constants.FILTER_ID_PRICE);
		
		QuantityFilter quantity = new QuantityFilter();
		quantity.setFilterId(Constants.FILTER_ID_QUANTITY);
		
		ListingTypeFilter ltf = new ListingTypeFilter();
		ltf.setFilterId(Constants.FILTER_ID_LISTING_TYPE);
		
		ConditionFilter condition = new ConditionFilter();
		condition.setFilterId(Constants.FILTER_ID_CONDITION);
		
		LocationFilter location = new LocationFilter();
		location.setFilterId(Constants.FILTER_ID_LOCATION);
		
		this.itemFilters[0] = cf[0];
		this.itemFilters[1] = cf[1];
		this.itemFilters[2] = cf[2];
		this.itemFilters[3] = cf[3];
		this.itemFilters[4] = cf[4];
		this.itemFilters[5] = bid;
		this.itemFilters[6] = price;
		this.itemFilters[7] = quantity;
		this.itemFilters[8] = ltf;
		this.itemFilters[9] = condition;
		this.itemFilters[10]= location;		
	}

}
