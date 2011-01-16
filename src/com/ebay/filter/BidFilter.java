package com.ebay.filter;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class BidFilter extends RangeFilter{
 
	private final static String NAME_MIN = "MinBids";
	private final static String NAME_MAX = "MaxBids";
	@Override
	public String getMaxParam_Name() {
       return NAME_MAX;
	}
	@Override
	public String getMinParam_Name() {
       return NAME_MIN;
	}

}
