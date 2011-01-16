package com.amir.ebay;


import java.util.HashMap;

import com.amir.ebay.ShowView.EventListener;
import com.ebay.filter.ItemFilter;
import com.ebay.xml.EbayItem;
import com.ebay.xml.Item;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class MainActivity extends Activity implements EventListener,OnClickListener,OnKeyListener{
	
	private final static int ALLCATEGORIES = 0;
	
	private ShowView view;
	private PopupWindow searchWindow;
	private EditText searchField;
	private FrameLayout mainFrame;
	private PaginationPane pagination;
	private Button searchBtn;
	private Button categoryBtn;
	private Button filterBtn;
	private String[] categoryKeys;
	private int[] categoryValues;
	private AlertDialog categoryPicker;
	private int currentCategory = ALLCATEGORIES;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
   
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
  	    
       ContainerDesigner designer = new SimpleContainerDesigner(dm);     
       this.view = new ShowView(this, designer,dm.heightPixels);  
    
       final int width = dm.widthPixels;
       
       FrameLayout frame = new FrameLayout(this);
       frame.setPadding(0, 5, 0, 0);

       FrameLayout.LayoutParams fParams0 = new FrameLayout.LayoutParams(width/3,30);
       fParams0.gravity = Gravity.TOP | Gravity.CENTER;
       
       FrameLayout.LayoutParams fParams1 = new FrameLayout.LayoutParams(width/3,30);
       fParams1.gravity = Gravity.TOP | Gravity.LEFT;

       FrameLayout.LayoutParams fParams2 = new FrameLayout.LayoutParams(width/3,30);
       fParams2.gravity = Gravity.TOP | Gravity.RIGHT;
       
       FrameLayout.LayoutParams fParams3 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
    		                                                            FrameLayout.LayoutParams.WRAP_CONTENT);
       fParams3.gravity = Gravity.CENTER | Gravity.BOTTOM;
       
       FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
    		                                                      RelativeLayout.LayoutParams.WRAP_CONTENT,
    		                                                      RelativeLayout.LayoutParams.WRAP_CONTENT);  
       params.gravity = Gravity.CENTER;
       
       this.pagination = new PaginationPane(this,dm,this,PaginationPane.EMPTY_PANE,8);
       this.pagination.setLayoutParams(fParams3);
       this.pagination.setOnClickListener(this);
            
       final Typeface typeface  = ResourceHelper.getButtonTypeFace(this);
       final int buttonSelector = ResourceHelper.getButtonSelector();
       
       this.searchBtn = new Button(this);
       this.searchBtn.setTextSize(11);
       this.searchBtn.setText("Search");
       this.searchBtn.setLayoutParams(fParams1);
       this.searchBtn.setBackgroundResource(buttonSelector);
       this.searchBtn.setTypeface(typeface);
              
       this.filterBtn = new Button(this);
       this.filterBtn.setTextSize(11);
       this.filterBtn.setText("Filter");
       this.filterBtn.setLayoutParams(fParams0);
       this.filterBtn.setBackgroundResource(buttonSelector);
       this.filterBtn.setTypeface(typeface);
       
       this.categoryBtn = new Button(this);
       this.categoryBtn.setTextSize(11);
       this.categoryBtn.setText("Category");
       this.categoryBtn.setLayoutParams(fParams2);
       this.categoryBtn.setBackgroundResource(buttonSelector);
       this.categoryBtn.setTypeface(typeface);
       
       this.searchBtn.setOnClickListener(this);
       this.categoryBtn.setOnClickListener(this);
       this.filterBtn.setOnClickListener(this);
       
       this.view.setLayoutParams(params);
      // frame.setBackgroundColor(0xFF1C2331);
       frame.setBackgroundColor(0xFF010314);
       frame.addView(this.searchBtn);
       frame.addView(this.categoryBtn);
       frame.addView(this.filterBtn);
       frame.addView(this.pagination);
       frame.addView(view);
       this.mainFrame = frame;
       
       
       if(savedInstanceState != null){
    	  Object[] lastState = (Object[])this.getLastNonConfigurationInstance();
    	  if(lastState != null){
    	    EbayItem[] content = (EbayItem[])lastState[0]; 
    	    this.view.attachContent(content); 
    	    int selectedPage = (Integer)lastState[1];
    	    int pageCount    = (Integer)lastState[2];
    	    FetcherThread ft = (FetcherThread)lastState[3];
    	    this.currentCategory = (Integer)lastState[4];
    	    this.pagination.setPageCount(pageCount);
    	    this.pagination.setSelectedPage(selectedPage); 
    	    this.view.setCurrentPage(selectedPage);
    	    this.view.unlockFetcher(ft);
    	    FilterHandler.getInstance().setItemFilters((ItemFilter[])lastState[5]);
    	  } 
       }
       else{   ///////Just First time
    	       ////// although it's not an appropriate way to do this but it seems there is
    	       ///// no other way, I need to have the main window initialized before being able
    	       ////  to do this; something like "ActivityListener" would be quite handy here!!
    	 new Handler().postDelayed(new Runnable() {		
			@Override
			public void run() {
				try{
                createSearchWindow();
				}catch(Throwable anything){
					anything.printStackTrace();
					searchWindow = null; /// as if nothing has happened!
				}
			}
		 }, 1000); 
       }	 
    	  
       this.view.setEventListener(this);
       setContentView(frame);
    }
  
    @Override
    public Object onRetainNonConfigurationInstance(){
       Object[] state = new Object[6];	
       state[0] = this.view.detachContent();
       state[1] = this.pagination.getSelectedPage();
       state[2] = this.pagination.getPageCount();
       state[3] = this.view.lockCurrentFetcher();
       state[4] = this.currentCategory;
       state[5] = FilterHandler.getInstance().getItemFilters();
       
      return state; 
    }
    
	@Override
	public void errorOccured(String errorMsg) {		
	   this.pagination.setPageCount(0);	
	   Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();	
	}
	
	@Override
	public void itemClicked(Item item){
		
	   if(item != null){	
		Intent intent = new Intent("com.amir.ebay.SHOW");
		HashMap<String,String> map = new HashMap<String,String>();
		map.putAll(item.getContent());
		map.putAll(item.getDetails());
		
		intent.putExtra("Item", map);
		intent.putExtra("Pic", item.getImage());
		startActivity(intent);
	   }
	   else
		 System.err.println("Clicked Item is empty!");  
	}

	@Override
	public void searchDone(int resultNumber) {
		this.pagination.setPageCount(resultNumber);
		 if(resultNumber == 0)
			 showMessage("Nothing could be found!");
	}

	
	public void showMessage(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}


	/*public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, LOOK_N_FEEL_MENU, 0, "LOOK & Feel").setIcon(android.R.drawable.ic_menu_view);
	    menu.add(0, FILTER_MENU, 1, "Search Filter").setIcon(android.R.drawable.ic_menu_recent_history);
	    return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case LOOK_N_FEEL_MENU:
	    	
	        return true;
	    case FILTER_MENU:
	    	Intent intent = new Intent();
			intent.setAction("com.amir.ebay.FILTERS");
			this.startActivity(intent);
	        return true;
	    }
	    return false;
	}*/
	
	@Override
	public void onClick(View v) {
		
		if(v == this.searchBtn){
		  if(this.searchWindow == null){
			  createSearchWindow();
		  }
		  this.searchWindow.showAtLocation(mainFrame, Gravity.CENTER, 20, 50); 	
		}
		else if(v == this.categoryBtn){
			                   //Lazy Initialization of categoryPicker
		 if(this.categoryPicker == null){
			 
			 final String[] values = getResources().getStringArray(R.array.categories);
			 final int length = values.length;
			 this.categoryKeys   = new String[length+1];	
			 this.categoryValues = new int[length+1];

			 this.categoryKeys[0]   = "All Categories";
			 this.categoryValues[0] = ALLCATEGORIES;
			 int categoryIndex = 0;

			 for(int i=1;i<=length;i++){
				 try{  
					 String[] tokens = values[i-1].split("=");
					 this.categoryKeys[i] = tokens[0];
					 this.categoryValues[i] = Integer.parseInt(tokens[1]);
					  if(this.currentCategory == this.categoryValues[i])
						 categoryIndex = i; 
						   
				 }catch(NumberFormatException exp){
					 exp.printStackTrace();
					 continue;
				 }
			 }

			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
			 builder.setTitle("Pick a category");
			 builder.setSingleChoiceItems(this.categoryKeys, categoryIndex, new DialogInterface.OnClickListener() {
				 public void onClick(DialogInterface dialog, int item) {
					 currentCategory = categoryValues[item];
					 categoryPicker.dismiss();
				 }
			 });
			 
		    this.categoryPicker = builder.create();
		 }
			
		  this.categoryPicker.show();	
		}
		else if(v == this.filterBtn){
			Intent intent = new Intent();
			intent.setAction("com.amir.ebay.FILTERS");
			this.startActivity(intent);	
		}
		
		if(v.getId() == PaginationPane.PREVIOUS_BTN_ID){
			if(!this.view.previousPage())
				showMessage("There is no previous page");  
		}	
		else if(v.getId() == PaginationPane.NEXT_BTN_ID){
			if(!this.view.nextPage())
				showMessage("There is no next page");
		}
		
	}
	
	private void Search(){
		String str = this.searchField.getText().toString();
		if(str == null || str.length() == 0){
			Toast.makeText(MainActivity.this, "Empty Search is not allowed.", Toast.LENGTH_LONG).show();
		}
		else {
			doSearch(str); 
			this.searchWindow.dismiss();
		}  	
	}
	
	private void createSearchWindow(){

		  final int width = mainFrame.getWidth();
		  LayoutInflater inflater = getLayoutInflater();
		  final RelativeLayout windowContent = (RelativeLayout)inflater.inflate(R.layout.search,null);
		  
		  searchWindow = new PopupWindow(windowContent,width,100,true);
		  searchWindow.showAtLocation(mainFrame, Gravity.CENTER, 0, 0);   

		  Button btn  = (Button)windowContent.findViewById(R.id.searchBtn);	    	
		  Button btn2 = (Button)windowContent.findViewById(R.id.cancelBtn);
		  this.searchField = (EditText)windowContent.findViewById(R.id.searchBox);
		  this.searchField.setOnKeyListener(this);


		  btn2.setOnClickListener(new OnClickListener() {

			  @Override
			  public void onClick(View v) {
				  searchWindow.dismiss();
			  }
		  });
		  btn.setOnClickListener(new OnClickListener() {
			  
			  @Override
			  public void onClick(View v) {
				Search();
			  }
		  });	
	}
	
	private void doSearch(String criteria){
		
		  HashMap<String,Object> map = new HashMap<String,Object>(); 
		  map.put(Constants.EBAY_SEARCH_KEYWORD, criteria);
		  map.put(Constants.EBAY_RESULT_START_INDEX, 1);
		  if(this.currentCategory != ALLCATEGORIES)
		     map.put(Constants.EBAY_SEARCH_CATEGORY, this.currentCategory);
	     
		  this.view.startNewSearch(ShowView.EBAY_CHANNEL_INDEX, map);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_ENTER){
			Search();
		   	return true;
		}		   
			   
	 return false;
	}

	
}