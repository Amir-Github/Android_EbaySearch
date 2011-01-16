package com.amir.ebay.looknfeel;

import android.graphics.Typeface;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public interface LookNFeel {

	public int getLookNFeelID();
	public void setContainerBorderColor(int color);
	public int getContainerBorderColor();
	public void setContainerPrimaryColor(int color);
	public int getContainerSecondaryColor();
	public void setContainerTextColor(int color);
	public int getContainerTextColor();
	public void setContainerTextTypeface(Typeface tf);
	public Typeface getContainerTextTypeface();
	public void setMainBackgroundColor(int color);
	public int getMainBackgroundColor();
	public void setButtonBackgroungResource(int resource);
	public int getButtonBackgroundResource();
	public void setButtonTypeface(Typeface tf);
	public int getButtonTypeface();
	public void setPaginationBackgroundColor(int color);
	public int getPaginationBackgroundColor();
	public void setPaginationBoxColor(int color);
	public int getPaginationBoxColor();
	public void getSecondaryBackgroundColor(int color);
	public int getSecondaryBackgroundColor();
	public void setSecondaryFontColor(int color);
	public int getSecondaryFontColor();
	public void setSecondaryTextTypsface(Typeface tf);
	public Typeface getSecondaryTexTypeface();
}
