package com.amir.ebay;

import android.content.Context;
import android.graphics.Typeface;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class ResourceHelper {
	
	private static Typeface buttonTypeFace;
	private final static int buttonSelector = R.drawable.button_selector; 
	
	public static Typeface getButtonTypeFace(Context context){		
	   if(buttonTypeFace == null){
		  if(context == null)
			  return null;
		  else
			buttonTypeFace = Typeface.createFromAsset(context.getResources().getAssets(), "verdana.ttf");     
	   }
	 return buttonTypeFace;	
	}
  
	public static int getButtonSelector(){
		return buttonSelector;
	}
	
}
