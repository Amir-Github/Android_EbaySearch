package com.ebay.xml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class EbayItem implements Item{
	
	public final static long serialVersionUID = EbayItem.class.getName().hashCode();

	public final static String TITLE = "Title";
	public final static String IMAGE_URL = "galleryURL";
	public final static String PRICE     = "convertedCurrentPrice";	
	public final static String ITEM_DETAILS_LOCATION      = "location";
	public final static String ITEM_DETAILS_SHIPPING_COST = "shippingServiceCost";
	public final static String ITEM_DETAILS_SHIPPING_TO   = "shipToLocations";
	public final static String ITEM_DETAILS_LISTING_TYPE  = "listingType";
	public final static String ITEM_DETAILS_TIME_LEFT     = "timeLeft";        
	
	public final static String PERIOD = "P";
	public final static String TIME   = "T";
	public final static String DAY    = "D";
	public final static String HOUR   = "H";
	public final static String MINUTE = "M";
	public final static String SECOND = "S";
	
	public final static String DAY_LABEL    = " Day(s), ";
	public final static String HOUR_LABEL   = " Hour(s), ";
	public final static String MINUTE_LABEL = " Minutes(s)";
	//public final static String SECOND_LABEL = " Seconds(s)";
	public final static String[] LABELS = {DAY_LABEL , HOUR_LABEL, MINUTE_LABEL};
	
	
	public final static String[] TAG_FILTER = {
		TITLE,
		IMAGE_URL,
		PRICE,
		ITEM_DETAILS_LOCATION,
		ITEM_DETAILS_SHIPPING_COST,
		ITEM_DETAILS_SHIPPING_TO,
		ITEM_DETAILS_LISTING_TYPE,
		ITEM_DETAILS_TIME_LEFT
	};
	
	
	private HashMap<String,String> map;
    private Bitmap image;
	
	public EbayItem(){	
		this.map = new HashMap<String,String>();
	}
	
	public EbayItem(HashMap<String,String> hm){
		this.map = hm;
	}
	
	public EbayItem(String name,String price,String imageURL){

		this.map.put(TITLE, name);
		this.map.put(PRICE, price);
		this.map.put(IMAGE_URL, imageURL);
	}
  
	public void put(String key,String value){
		this.map.put(key, value);
	}
	
	public String get(String key){
		return this.map.get(key);
	}
	
	public void setMap(HashMap<String,String> hm){
		this.map = hm;
	}
	
	public HashMap<String,String> getMap(){
		return this.map;
	}
   
	//ISO 8601 Duration FormatParser
	public static String convertTime(final String timeLeft_EbayFormat){
		
		if(timeLeft_EbayFormat == null || timeLeft_EbayFormat.length() == 0)
			return null;
		
		StringBuilder builder = new StringBuilder();
		final int pIndex = timeLeft_EbayFormat.indexOf(PERIOD);
		final int tIndex = timeLeft_EbayFormat.indexOf(TIME);
		final int DIndex = timeLeft_EbayFormat.indexOf(DAY);
		final int HIndex = timeLeft_EbayFormat.indexOf(HOUR);
		final int MIndex = timeLeft_EbayFormat.indexOf(MINUTE);
		//final int SIndex = timeLeft_EbayFormat.indexOf(SECOND);
	
		final int[] indexes = {DIndex, HIndex, MIndex};
		int baseIndex = pIndex;
		
	    if(pIndex < 0)
	    	return null;
	    
	   for(int i=0;i<3;i++){    
	    if(indexes[i] > 0){
	    	 if(indexes[i] > tIndex && baseIndex < tIndex)
	    		 baseIndex = tIndex;
	    	String str = timeLeft_EbayFormat.substring(baseIndex+1,indexes[i]);
	    	builder.append(Integer.parseInt(str)+LABELS[i]);
	    	baseIndex = indexes[i];
	    }
	   }	
	    
		
	 return builder.toString();
	}
	
	@Override
	public HashMap<String, String> getContent() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("Title", EbayItem.this.get(TITLE));
		map.put("Current Price", EbayItem.this.get(PRICE));
		
	  return map;
	}
	
	@Override
	public URL getImageUrl() {	
		URL url = null;
		try{
			url = new URL(map.get(IMAGE_URL));
		}catch(MalformedURLException exp){
			exp.printStackTrace();
		}
		return url;
	}

	@Override
	public HashMap<String, String> getDetails() {
		HashMap<String,String> details = new HashMap<String,String>();
		details.put("Location", (String)map.get(ITEM_DETAILS_LOCATION));
		details.put("Shipping Cost", (String)map.get(ITEM_DETAILS_SHIPPING_COST));
		details.put("Shipping To", (String)map.get(ITEM_DETAILS_SHIPPING_TO));
		details.put("Time Left", (String)map.get(ITEM_DETAILS_TIME_LEFT));
		details.put("Listing Type", (String)map.get(ITEM_DETAILS_LISTING_TYPE));
		
	 return details;
	}

	@Override
	public Bitmap getImage() {
		return this.image;
	}

	@Override
	public void setImage(Bitmap img) {
		this.image = img;
	}
	
}
