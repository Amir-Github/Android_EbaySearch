package com.amir.ebay;

import java.util.HashMap;
import android.graphics.Bitmap;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public interface ContainerDesigner {
   
	public void setContainerSize(int width,int height);
	public int getContainerWidth();
	public int getContainerHeight();
	public Bitmap getContainer(Bitmap image,HashMap<String,String> content);
	public Bitmap addContent(Bitmap container,HashMap<String, String> content);
	public Bitmap addImage(Bitmap container,Bitmap image);
	public Bitmap makeEmptyContainer();	
}
