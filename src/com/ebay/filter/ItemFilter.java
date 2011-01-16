package com.ebay.filter;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public abstract class ItemFilter implements Filter{

	public final static long serialVersionUID = 0x224488AA; 
	
	private final static String NAME_FORMAT  = "itemFilter(%d).name";
	private final static String VALUE_FORMAT = "itemFilter(%d).value";
	private final static String VALUE_ITER_FORMAT = "itemFilter(%d).value(%d)";
	
	private int filterId = -1;

	
	public static HashMap getQueryParams(ItemFilter[] filters) {
		
	  LinkedHashMap<String,String> queryParams = new LinkedHashMap<String,String>();
	  int counter = 0;
	  for(int j=0;j<filters.length;j++){ 	
		final HashMap<String, String[]> entries = filters[j].getFilterMap();
		final Iterator<String> keys = entries.keySet().iterator();
		 while(keys.hasNext()){
			 final String key = keys.next();
			 final String[] values = entries.get(key);
			 queryParams.put(String.format(NAME_FORMAT, counter), key);
			 
			 if(values.length == 1)
			   queryParams.put(String.format(VALUE_FORMAT, counter),values[0]);
			 else {
			   for(int i=0;i<values.length;i++)
				queryParams.put(String.format(VALUE_ITER_FORMAT, counter,i),values[i]);  
			 }  
		   counter++;
		 }
	  } 
		
	 return queryParams;
	}
	
	@Override
	public int getFilterId() {
		return filterId;
	}

	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}
	
	public abstract boolean isActive();

}
