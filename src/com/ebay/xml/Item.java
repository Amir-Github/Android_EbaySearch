package com.ebay.xml;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public interface Item extends Serializable{

	public HashMap<String,String> getDetails();
	public HashMap<String, String> getContent();
	public URL getImageUrl();
	public Bitmap getImage();
	public void setImage(Bitmap img);
	
}
