package com.ebay.filter;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class PriceFilter extends RangeFilter{

    private final static String NAME_MIN = "MinPrice";
    private final static String NAME_MAX = "MaxPrice";
	@Override
	public String getMaxParam_Name() {
	  return NAME_MAX;
	}
	@Override
	public String getMinParam_Name() {
	 return NAME_MIN;
	}
	

}
