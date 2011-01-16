package com.ebay.filter;

import java.util.HashMap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class ConditionFilter extends ItemFilter{

	public final static String CONDITION_NEW  = "New";
	public final static String CONDITION_USED = "Used";
	public final static String CONDITION_UNSPECIFIED = "Unspecified ";
   
	private final static String NAME = "Condition";
	private String condition;
	
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public HashMap<String, String[]> getFilterMap() {
		
	   HashMap<String,String[]> map = new HashMap<String, String[]>();
	   map.put(NAME, new String[] {this.condition});
		
	  return map; 
	}

	@Override
	public boolean isActive() {
		return this.condition != null;
	}
	
}
