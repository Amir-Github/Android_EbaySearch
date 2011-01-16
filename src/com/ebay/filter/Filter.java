package com.ebay.filter;

import java.io.Serializable;
import java.util.HashMap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public interface Filter extends Serializable{
	
   public HashMap<String,String[]>getFilterMap();
   public int getFilterId();
}
