package com.ebay.filter;

import java.util.HashMap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public abstract class RangeFilter extends ItemFilter{
 
	private double minValue = -4;
	private double maxValue = -2;
	private boolean isActive = false;
	
	@Override
	public HashMap<String, String[]> getFilterMap() {
		
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		map.put(getMinParam_Name(), new String[]{(int)this.minValue+""});
		map.put(getMaxParam_Name(), new String[]{(int)this.maxValue+""});
		
	  return map;	
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		if(this.maxValue != -2 && minValue > this.maxValue)
			 throw new IllegalArgumentException("Minimum value must be Less than or equal to Maximum Value!");			
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		if(this.minValue != -4 && maxValue < this.minValue)
			 throw new IllegalArgumentException("Maximum value must be greater than or equal to Minimum Value!");			
		this.maxValue = maxValue;
	}

	
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public abstract String getMinParam_Name();
	public abstract String getMaxParam_Name();
	
}
