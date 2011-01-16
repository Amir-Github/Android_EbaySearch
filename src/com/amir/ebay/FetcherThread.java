package com.amir.ebay;

import java.util.Map;
import com.ebay.xml.Item;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public abstract class FetcherThread extends Thread {
	
	public volatile boolean lastRequest_finished = true;	
	protected Handler callback;
	protected SearchInfo lastSearchInfo;
	protected Map lastSearchCriteria;
	
	private boolean anyRequest  = false;
	private boolean stopFlag    = false;
	private boolean launcherReady = true; 
	
	private boolean internalErrorFlag = false;
	private int errorType;
	
	
	public FetcherThread(){
	}
	
	public FetcherThread(Handler handler){
		this.callback = handler;
	}
	
	public void setHandler(Handler handler){
		if(this.callback != null)
			  throw new IllegalStateException("Handler has been already set!");	
		this.callback = handler;
	}
	
	public Handler getHandler(){
	   return this.callback; 	
	}
	
	public void run(){
		   
		   initialize();
		  
		  do {
			while(!this.anyRequest){
				
			 try{	
				synchronized (this) {
					this.wait();	
				} 
			 }catch(InterruptedException exp){
				 ////Just Nothing
			 }
				
			}	
		  			
	        try{
	        	
	        	Message msg = new Message();
	        	Bundle content = new Bundle();  
	        	
		       	Item[] result = fetchData();
		       	
		       	if(result == null && this.internalErrorFlag){
	        		content.putInt(Constants.ERROR_TYPE, this.errorType);
	        		msg.what = Constants.FETCH_ERROR;
	        		this.internalErrorFlag = false;  //clear the status
	        	}
		       	else if(this.lastSearchInfo.searchError){
		       		content.putInt(Constants.ERROR_TYPE, Constants.UNEXPECTED_ERROR_TYPE);
		       		msg.what = Constants.FETCH_ERROR;
		       		content.putString(Constants.ERROR_MSG, this.lastSearchInfo.errorMessage);
		       		this.lastSearchInfo.searchError  = false;
		       		this.lastSearchInfo.errorMessage = null;
		       	}
	        	else{ ///Everything is OK!
	        	   content.putSerializable(Constants.FETCH_RESULT,result);	
	        	}
		       	
		        msg.setData(content);
		       	
	        	while(!this.launcherReady){

	        		try{	
	        			synchronized (this) {
	        				this.wait();	
	        			} 
	        		}catch(InterruptedException exp){
	        			////Just Nothing
	        		}	
	        	} 	

	        	
	        	this.callback.sendMessage(msg); 
	        	
	        }catch(Exception exp){
	    		exp.printStackTrace();
	    		
	        }
	        
	      
		  this.anyRequest = false;
		  this.lastRequest_finished = true;
	        
		}while(!this.stopFlag);
		
	}
		
	public synchronized void fetch(Map operationCriteria,int number,int fromPage){
		
		preFetchOperation(operationCriteria,number,fromPage);
		this.lastSearchCriteria = operationCriteria;
		this.anyRequest = true;
		this.lastRequest_finished = false;
		this.internalErrorFlag    = false;
		this.notifyAll();
		
	}
	
	public void cancelThread(){
		this.stopFlag = true;
	}
	
	public synchronized void lockIt(){
		this.launcherReady = false;	
	}

	public synchronized void unlockIt(Handler newOne){
		this.launcherReady = true;
		if(newOne != null)
			this.callback      = newOne;
		this.notifyAll();
	}
	
	public void announceError(int type){
       this.internalErrorFlag = true;
	   this.errorType = type;	
	}
	
	public SearchInfo getLastSearchInfo(){
	  return this.lastSearchInfo;	
	}
	
	public Map getLastSearchCriteria(){
	  return this.lastSearchCriteria;	
	}
	
	protected abstract void initialize();
	protected abstract void preFetchOperation(Map operationAttr,int number,int from);
	protected abstract Item[] fetchData();
	
}
