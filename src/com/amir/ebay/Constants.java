package com.amir.ebay;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */  
public interface Constants {

	public final static int CONNECTION_ERROR_TYPE = -2; /// Network connection is off or server is down or something like this...
	public final static int FETCH_ERROR_TYPE      = -3; /// Server is blocked or busy or....
	public final static int PARSING_ERROR_TYPE    = -4; /// Response Format is not what we were expecting
	public final static int UNEXPECTED_ERROR_TYPE = -5;      
	
	public final static int FILTERS_ACTIVITY_REQUEST_CODE = 8080;
	
	public final static int FILTER_ID_EXCLUDE_AUTO_PAY      = 0;
	public final static int FILTER_ID_FEATURED_ONLY         = 1;
	public final static int FILTER_ID_FREE_SHIPPING_ONLY    = 2;
	public final static int FILTER_ID_GET_IT_FAST_ONLY      = 3;
	public final static int FILTER_ID_TOP_RATED_SELLERS     = 4;
	public final static int FILTER_ID_BID                   = 5;
	public final static int FILTER_ID_PRICE                 = 6;
	public final static int FILTER_ID_QUANTITY              = 7;
	public final static int FILTER_ID_LISTING_TYPE          = 8;
	public final static int FILTER_ID_CONDITION             = 9;
	public final static int FILTER_ID_LOCATION              = 10;
	
	
	public final static String FETCH_RESULT = "Result";
	public final static String ERROR_TYPE    = "ERROR_TYPE";
	
	public final static String EBAY_SEARCH_KEYWORD     = "EbaySearchKeword";
	public final static String EBAY_SEARCH_CATEGORY    = "EbaySearchCategory";
	public final static String EBAY_RESULT_START_INDEX = "EbayResultStartIndex"; 
	
	public final static int    FETCH_ERROR  = -21;
	public final static String ERROR_MSG    = "Err_Msg";
}
