package com.ebay.filter;

import java.util.HashMap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class ConditionalFilter extends ItemFilter{

	public final static String CONDITION_EXCLUDE_AUTO_PAY   = "ExcludeAutoPay";
	public final static String CONDITION_FEATURED_ONLY      = "FeaturedOnly";
	public final static String CONDITION_FREE_SHIPPING_ONLY = "FreeShippingOnly";
	public final static String CONDITION_GET_IT_FAST_ONLY   = "GetItFastOnly";
	public final static String CONDITION_TOP_RATED_SELLERS  = "TopRatedSellerOnly";

	private String condition;
    private boolean enable = false;
	
	public void setCondition(String name){
	   this.condition = name;	
	}
	
	public String getCondition(){
		return this.condition;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public HashMap<String, String[]> getFilterMap() {
		
		HashMap<String,String[]> map = new HashMap<String, String[]>();
		map.put(this.condition, new String[]{"true"});
	 return map;	
	}

	@Override
	public boolean isActive() {
		return enable;
	}
	
}
