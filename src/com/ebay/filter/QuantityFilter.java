package com.ebay.filter;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class QuantityFilter extends RangeFilter{


	private final static String NAME_MIN = "MinQuantity";
	private final static String NAME_MAX = "MaxQuantity";
	
	
	@Override
	public String getMaxParam_Name() {
	 return NAME_MAX;
	}
	@Override
	public String getMinParam_Name() {
	  return NAME_MIN;
	}
	
	@Override
	public void setMinValue(double min){
	  if(min < 1)
		throw new IllegalArgumentException("Quantity cannot be less than 1.");
	  super.setMinValue(min);
	}
	
	@Override
	public void setMaxValue(double max){
	 if(max < 1)
	   throw new IllegalArgumentException("Quantity cannot be less than 1.");	 
	 super.setMaxValue(max);	
	}
	

}
