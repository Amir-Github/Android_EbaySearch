package com.ebay.rest;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class EbayRestRequest extends RestRequest{
	
	public final static String OPERATION_TYPE_FIND_BY_KEYWORD = "findItemsByKeywords";
	public final static String OPERATION_TYPE_ADVANCED        = "findItemsAdvanced";
	
	private final static String OPERATION_TYPE  = "OPERATION-NAME";
	private final static String RESULT_PER_PAGE = "paginationInput.entriesPerPage";
	private final static String RESULT_PAGE_NUMBER = "paginationInput.pageNumber";
	private final static String KEYWORD            = "keywords";  
	private final static String CATEGORYID         = "categoryId";

    
    private final static String VERSION_VALUE     = "1.0.0";
    private final static String APPNAME_VALUE     = "AmirSadr-b877-4746-903b-a19ca53a4d1d";
    private final static String DATA_FORMAT_VALUE = "XML";
	
	
	public EbayRestRequest() {
		super("http://svcs.ebay.com/services/search/FindingService/v1");
		setOperationType(OPERATION_TYPE_ADVANCED);
		prepareRequest();
	}

	public EbayRestRequest(String operationType){
		super("http://svcs.ebay.com/services/search/FindingService/v1");
		setOperationType(operationType);
		prepareRequest();
	}
	
	private void prepareRequest(){
		setParameter("SERVICE-VERSION", VERSION_VALUE);
		setParameter("SECURITY-APPNAME", APPNAME_VALUE);
		setParameter("RESPONSE-DATA-FORMAT", DATA_FORMAT_VALUE);
		setParameter(RESULT_PER_PAGE, "6");
	}
	
	public void setOperationType(String ot){
		setParameter(OPERATION_TYPE, ot);
	}
	
	public String getOperationType(){
		return (String)getParameters().get(OPERATION_TYPE);
	}
	
	public void setResultPerPage(int rpp){
		setParameter(RESULT_PER_PAGE,Integer.toString(rpp));
	}
	
	public int getResultPerPage(){
	 String temp = (String)getParameters().get(RESULT_PER_PAGE);
	  if(temp != null)
		  return Integer.parseInt(temp);
	  
	  return -1;
	}
	
	public void setPageNumber(int pageNumber){
	  setParameter(RESULT_PAGE_NUMBER, Integer.toString(pageNumber));	
	}
	
	public int getPageNumber(){
		String temp = (String)getParameters().get(RESULT_PER_PAGE);
		if(temp != null)
			return Integer.parseInt(temp);

		return 1;
	}
	
	
	public void setKeyword(String kw){
	  setParameter(KEYWORD, kw);	
	}
	
	public String getKeyword(){
	  return (String)getParameters().get(KEYWORD);	
	}
	
	public void setCategory(int id){
		setParameter(CATEGORYID, Integer.toString(id));
	}
	
	public void removeCategory(){
		this.getParameters().remove(CATEGORYID);
	}
	
	public int getCategoryId(){
		return Integer.parseInt((String)getParameters().get(CATEGORYID));
	}
	
}
