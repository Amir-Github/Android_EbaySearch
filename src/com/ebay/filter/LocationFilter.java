package com.ebay.filter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class LocationFilter extends ItemFilter{

	private final static String NAME_AVAILABLE = "AvailableTo";
	private final static String COUNTRIES_ALL  = "All";
	
	
	private String selectedCountry;
    private final TreeMap<String,String> countries;
    
    public LocationFilter(){
      this.selectedCountry = COUNTRIES_ALL;	
      this.countries = new TreeMap<String,String>();
          	  
      ResourceBundle bundle = ResourceBundle.getBundle("com.ebay.filter.countries");
      Enumeration<String> iter = bundle.getKeys();
      
      this.countries.put(COUNTRIES_ALL, COUNTRIES_ALL);
      while(iter.hasMoreElements()){
    	  String code = iter.nextElement();
    	  String name = bundle.getString(code);
    	  this.countries.put(name, code);
      }
       
    }
    
    public TreeMap<String,String> getCountryMap(){
    	return this.countries;
    }
    
    public void setSelectedCountry(String name){
    	this.selectedCountry = name;
    }
    
    public String getSelectedCountry(){
    	return this.selectedCountry;
    }
    
	@Override
	public HashMap<String, String[]> getFilterMap() {
		
		HashMap<String,String[]> map = new HashMap<String,String[]>();
		if(this.countries.containsKey(this.selectedCountry))
		  map.put(NAME_AVAILABLE, new String[] {this.countries.get(this.selectedCountry)});
		
		return map;
	}

	@Override
	public boolean isActive() {
		return !this.selectedCountry.equalsIgnoreCase(COUNTRIES_ALL);
	}

}
