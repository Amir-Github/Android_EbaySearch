package com.ebay.xml;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.amir.ebay.SearchInfo;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class EbaySaxParser extends DefaultHandler {

	private ArrayList<EbayItem> items;
	private boolean readFlag = false;
	private boolean paginationFlag = false;
	private int responseStatus = UNKNOWN_STATUS;
	private String currentTag;
	private EbayItem temp;
	private final static int lenght = EbayItem.TAG_FILTER.length;
	
	private final static int UNKNOWN_STATUS       = 1;
	private final static int SUCCESSFUL_STATUS    = 2;
	private final static int ERROR_STATUS         = 3;
	
	private final static String PageNumberTag     = "pageNumber";
	private final static String EntriesPerPageTag = "entriesPerPage";
	private final static String TotalPagesTag     = "totalPages";
	private final static String TotalEntriesTag   = "totalEntries";
	private final static String ERROR_MESSAGE     = "errorMessage";
	private final static String MESSAGE           = "message";
	
	private SearchInfo search;
	
	public EbaySaxParser(SearchInfo info) {
		search = info;
	}
	
	
	public void startDocument() throws SAXException {
         this.items    = new ArrayList<EbayItem>();
         this.temp     = new EbayItem();
	} 

	public void endDocument() throws SAXException {

	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {		
		
        if(this.responseStatus != SUCCESSFUL_STATUS){
          if(localName.equalsIgnoreCase(ERROR_MESSAGE)){
        	this.responseStatus = ERROR_STATUS;
            this.search.pageNumber    = 0;
            this.search.resultPerPage = 0;
            this.search.totalEntries  = 0;
            this.search.totalPages    = 0;
            this.search.entriesCount  = 0;
            this.search.searchError   = true;
           return; 
          }
          else if(this.responseStatus == ERROR_STATUS){     	   
        	  if(localName.equalsIgnoreCase(MESSAGE))
        		  this.readFlag = true;
        	return;  
          } 
        } 	
        	      	   
		if(localName.equalsIgnoreCase("SEARCHRESULT")){
		   String str = attributes.getValue("count");
		   this.responseStatus = SUCCESSFUL_STATUS;
		     if(str != null)
		    	this.search.entriesCount = Integer.parseInt(str); 
		}		
		
        for(int i=0;i<lenght;i++){
           if(localName.equalsIgnoreCase(EbayItem.TAG_FILTER[i])){	
        	 this.readFlag = true;
        	 this.currentTag = EbayItem.TAG_FILTER[i];
        	 return;
           }	 
        }
        if(localName.equalsIgnoreCase("paginationOutput"))
        	this.paginationFlag = true;
        else if(localName.equalsIgnoreCase(PageNumberTag))
        	this.search.pageNumber = -17;
        else if(localName.equalsIgnoreCase(EntriesPerPageTag))
        	this.search.resultPerPage = -17;
        else if(localName.equalsIgnoreCase(TotalEntriesTag))
        	this.search.totalEntries = -17;
        else if(localName.equalsIgnoreCase(TotalPagesTag))
        	this.search.totalPages = -17;
		
        
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
           
		if(this.readFlag)
		   this.readFlag =false;
		else if(this.paginationFlag && localName.equalsIgnoreCase("paginationOutput"))
			this.paginationFlag = false;
		if(localName.equalsIgnoreCase("Item")){
		    this.items.add(this.temp);
			this.temp = new EbayItem();		
		}
						
	}

	public void characters(char ch[], int start, int length) throws SAXException {
        
		if(this.readFlag){
			
		  if(this.responseStatus == ERROR_STATUS){
			 final String msg = this.search.errorMessage; 
			 this.search.errorMessage = (msg == null ? new String(ch,start,length) : msg+new String(ch,start,length)); 
			return;  
		  }
			
		  String value = this.temp.get(this.currentTag);	
			if(value == null)
				this.temp.put(this.currentTag, new String(ch,start,length));
			else
				this.temp.put(this.currentTag, value+new String(ch,start,length));	
		}
		else if(paginationFlag){
			
			int value = Integer.parseInt(new String(ch,start,length));	
			
			if(this.search.pageNumber == -17)
			  this.search.pageNumber = value;
			else if(this.search.totalEntries == -17)
			  this.search.totalEntries = value;
			else if(this.search.resultPerPage == -17)
				this.search.resultPerPage = value;
			else if(search.totalPages == -17)
				this.search.totalPages = value;
			
		}
		
	}
    
	public EbayItem[] getResult(){
		EbayItem[] items = new EbayItem[this.items.size()];
		items = (EbayItem[])this.items.toArray(items);
		
      return items;		
	}

}
