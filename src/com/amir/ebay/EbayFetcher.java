package com.amir.ebay;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import com.ebay.filter.ItemFilter;
import com.ebay.rest.EbayRestRequest;
import com.ebay.rest.RestClient;
import com.ebay.rest.RestException;
import com.ebay.xml.EbayItem;
import com.ebay.xml.EbaySaxParser;
import com.ebay.xml.Item;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class EbayFetcher extends FetcherThread {

	private EbayRestRequest request   = new EbayRestRequest();
	
	@Override
	public Item[] fetchData() {
	 try{	
		EbayItem[] items = executeAndParse(request.getRequestUrl(), 
				                           request.getParameters());  
		
		return items;
		 
	 }catch(SAXException exp){
		 exp.printStackTrace();
		 announceError(Constants.PARSING_ERROR_TYPE);
	 }catch(RestException exp){
		 exp.printStackTrace(); 
		 announceError(Constants.FETCH_ERROR_TYPE);
	 }catch(IOException exp){
		 exp.printStackTrace(); 
		 announceError(Constants.CONNECTION_ERROR_TYPE);
	 }
	 return null;
	}
	
	@Override
	public void initialize() {
		this.lastSearchInfo = new SearchInfo();
	}

	@Override
	public void preFetchOperation(Map operationAttr,int number,int from) {
		
		this.request.setResultPerPage(number);
		this.request.setPageNumber(from);
		this.request.removeCategory();
		
		Iterator iter = operationAttr.keySet().iterator();
		
		 while(iter.hasNext()){
			String temp = (String)iter.next();
			
			if(temp.equalsIgnoreCase(Constants.EBAY_SEARCH_KEYWORD)){
				this.request.setKeyword((String)operationAttr.get(temp));
			}
			else if(temp.equalsIgnoreCase(Constants.EBAY_SEARCH_CATEGORY)){
				this.request.setCategory((Integer)operationAttr.get(temp));
			}
			
		 }
		
	}
	
	private EbayItem[] executeAndParse(String serviceUrl, Map parameters) throws SAXException,RestException,IOException {
		EbaySaxParser xmlParser = null;
		
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			xmlParser = new EbaySaxParser(this.lastSearchInfo);

			XMLReader reader = parser.getXMLReader();  
			reader.setContentHandler(xmlParser);
			
			
			ItemFilter[] filters = FilterHandler.getInstance().getActiveItemFilters();
			reader.parse(new InputSource(RestClient.call(serviceUrl, parameters, ItemFilter.getQueryParams(filters))));

		}
		catch (ParserConfigurationException e) {
			System.err.println("ERROR Parsing :");
			e.printStackTrace();
		}
		
		return xmlParser.getResult();
	}

}
