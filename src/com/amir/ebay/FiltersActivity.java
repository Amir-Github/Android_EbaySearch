package com.amir.ebay;

import java.util.Set;
import com.ebay.filter.ConditionFilter;
import com.ebay.filter.ConditionalFilter;
import com.ebay.filter.ListingTypeFilter;
import com.ebay.filter.LocationFilter;
import com.ebay.filter.RangeFilter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class FiltersActivity extends Activity implements OnCheckedChangeListener,
                                                         OnItemSelectedListener,
                                                         OnClickListener{

	private LayoutInflater inflator;
	private final static String ALL = "All";
	private final static String[] listingOptions = {ALL,
		                                            ListingTypeFilter.TYPE_ADFORMAT,
                                                    ListingTypeFilter.TYPE_AUCTION,
                                                    ListingTypeFilter.TYPE_AUCTION_WITH_BIN,
                                                    ListingTypeFilter.TYPE_CLASSIFIED,
                                                    ListingTypeFilter.TYPE_FIXED_PRICE,
                                                    ListingTypeFilter.TYPE_STORE_INVENTORY};
	
    private final static String[] ConditionOptions = {ALL,
    	                                              ConditionFilter.CONDITION_NEW,
                                                      ConditionFilter.CONDITION_UNSPECIFIED,
                                                      ConditionFilter.CONDITION_USED};
	
    private Button okBtn;
    private Button moreBtn;
    private boolean firstPage = true;
    private ViewFlipper flipper;
    private int[][] min_max_IDS = new int[3][2]; 
    
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
     
        setContentView(R.layout.filters);
        
        
        this.inflator = getLayoutInflater();  
        
        final FilterHandler handler = FilterHandler.getInstance();
        
        final ListingTypeFilter ltf = (ListingTypeFilter)handler.getItemFilter(Constants.FILTER_ID_LISTING_TYPE);
        final String[] types   = ltf.getTypes();
       
        final ConditionFilter condition = (ConditionFilter)handler.getItemFilter(Constants.FILTER_ID_CONDITION);
        final String con = condition.getCondition();
        
        ViewGroup[] children = new ViewGroup[11];
        	
       // children[0] = buildTwoStateLayout("Exclude Auto Pay : ", (ConditionalFilter)handler.getItemFilter(Constants.FILTER_ID_EXCLUDE_AUTO_PAY));
        children[0] = buildTwoStateLayout("Featured Only : ", (ConditionalFilter)handler.getItemFilter(Constants.FILTER_ID_FEATURED_ONLY));
        children[1] = buildTwoStateLayout("Free Shipping Only : ", (ConditionalFilter)handler.getItemFilter(Constants.FILTER_ID_FREE_SHIPPING_ONLY));
        children[2] = buildTwoStateLayout("Get It Fast Only : ", (ConditionalFilter)handler.getItemFilter(Constants.FILTER_ID_GET_IT_FAST_ONLY));
        children[3] = buildTwoStateLayout("Top Rated Sellers Only  : ", (ConditionalFilter)handler.getItemFilter(Constants.FILTER_ID_TOP_RATED_SELLERS));
          
        children[4] = buildMultipleStateLayout("Listing Type : ", listingOptions ,(types == null || types.length == 0 ? ALL : types[0]));
        children[5] = buildMultipleStateLayout("Condition  : ",ConditionOptions, (con == null || con.equals("") ? ALL : con));
        
        LocationFilter lf = (LocationFilter)handler.getItemFilter(Constants.FILTER_ID_LOCATION);	
      	Set<String> set = lf.getCountryMap().keySet();
      	String[] countries = new String[set.size()];
      	countries = set.toArray(countries);
      	
      	children[6] = buildMultipleStateLayout("Available To:", countries, lf.getSelectedCountry());
      	
        
     	children[7] = buildNumberRangeLayout("Price Range", (RangeFilter)handler.getItemFilter(Constants.FILTER_ID_PRICE),0);
      	children[8] = buildNumberRangeLayout("Current Bids", (RangeFilter)handler.getItemFilter(Constants.FILTER_ID_BID),1);
      	children[9] = buildNumberRangeLayout("Available Quantity", (RangeFilter)handler.getItemFilter(Constants.FILTER_ID_QUANTITY),2);

        
        LinearLayout first = (LinearLayout)findViewById(R.id.firstPage);
        LinearLayout second = (LinearLayout)findViewById(R.id.secondPage);
        
        Display display = getWindowManager().getDefaultDisplay();
      ///Portrait Mode
        if(display.getWidth() < display.getHeight()){  
        	
          for(int i=0;i<7;i++)
           first.addView(children[i]);
          
         
      	second.addView(children[7]);
      	second.addView(children[8]);
      	second.addView(children[9]);	
      	

        }
        else{  ////Landscape Mode
        
        	FrameLayout[] rows = new FrameLayout[5];
        	
        	FrameLayout.LayoutParams fParams1 = new FrameLayout.LayoutParams(250,FrameLayout.LayoutParams.WRAP_CONTENT);
            fParams1.gravity = Gravity.LEFT;

            FrameLayout.LayoutParams fParams2 = new FrameLayout.LayoutParams(250,FrameLayout.LayoutParams.WRAP_CONTENT);
            fParams2.gravity = Gravity.RIGHT;
        	
        	ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
        			                                                   ViewGroup.LayoutParams.WRAP_CONTENT);      
        	for(int i=0;i<5;i++){
        	  rows[i] = new FrameLayout(this);
        	  rows[i].setLayoutParams(params);
        	}	
        	
        	children[0].setLayoutParams(fParams1);
        	children[1].setLayoutParams(fParams2);
        	rows[0].addView(children[0]);
        	rows[0].addView(children[1]);
        	
        	children[2].setLayoutParams(fParams1);
        	children[3].setLayoutParams(fParams2);
        	rows[1].addView(children[2]);
        	rows[1].addView(children[3]);
        	
        	children[4].setLayoutParams(fParams1);
        	rows[2].addView(children[4]);
        	
        	children[5].setLayoutParams(fParams1);
        	children[6].setLayoutParams(fParams2);
        	rows[3].addView(children[5]);
        	rows[3].addView(children[6]);
        	
        	for(int i=0;i<4;i++)
        		first.addView(rows[i]);		
        	
       
        	children[7].setLayoutParams(fParams1);
        	rows[4].addView(children[7]);
        	
        	children[8].setLayoutParams(fParams2);
        	rows[4].addView(children[8]);
        	
          	second.addView(rows[4]);
          	second.addView(children[9]);
        	
        } 
        
        this.flipper = (ViewFlipper)findViewById(R.id.mainView);
        Animation s_in  = AnimationUtils.loadAnimation(this, R.anim.slidein);
        Animation s_out = AnimationUtils.loadAnimation(this, R.anim.slideout);
        this.flipper.setInAnimation(s_in);
        this.flipper.setOutAnimation(s_out);
        
        this.okBtn   = (Button)findViewById(R.id.okBtn);
        this.moreBtn = (Button)findViewById(R.id.moreBtn);
        this.okBtn.setOnClickListener(this);
        this.moreBtn.setOnClickListener(this);
        overridePendingTransition(R.anim.slidein,R.anim.slideout);
  
	}

	
	private FrameLayout buildTwoStateLayout(String desc,ConditionalFilter filter){
	  	
		FrameLayout layout = (FrameLayout)this.inflator.inflate(R.layout.checkbox_layout, null);
		TextView description = (TextView)layout.findViewById(R.id.Desc);
		CheckBox state       = (CheckBox)layout.findViewById(R.id.check);
		state.setOnCheckedChangeListener(this);
		state.setTag(filter);
		
		description.setText(desc);
		
		Log.d("<<<Filter_"+filter.getFilterId()+">>>",filter.isEnable()+"");
		
		state.setChecked(filter.isEnable());
			
	 return layout;
	}
	
	private LinearLayout buildMultipleStateLayout(String desc,
			                                      String[] states,
			                                      String selectedStat){
		
		LinearLayout layout = (LinearLayout)this.inflator.inflate(R.layout.spinner_layout, null);
		TextView description = (TextView)layout.findViewById(R.id.Desc);
		Spinner spinner      = (Spinner)layout.findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				                                  android.R.layout.simple_spinner_item,
				                                  states);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		int index = 0;
		for(int i=0;i<states.length;i++){
		  if(states[i].equals(selectedStat)){
			 index = i;
			 break;
		  }	 
		}
		spinner.setSelection(index);
		description.setText(desc);	
		
	  return layout;	
	}
	
	private FrameLayout buildNumberRangeLayout(String description, RangeFilter filter,int row){
		
	  FrameLayout layout = (FrameLayout)this.inflator.inflate(R.layout.rangelayout, null);
	  TextView view = (TextView)layout.findViewById(R.id.description);
	  view.setText(description);	
	  
	  EditText min = (EditText)layout.findViewById(R.id.Minvalue);
	  min.setTag(filter);
	  min.setId(description.hashCode()+16);
	  this.min_max_IDS[row][0] = min.getId();
	  
	  EditText max = (EditText)layout.findViewById(R.id.Maxvalue);
	  max.setId(description.hashCode()+32);
	  this.min_max_IDS[row][1] = max.getId();
	  
	  if(filter.isActive()){
		min.setText(Integer.toString((int)filter.getMinValue()));
		max.setText(Integer.toString((int)filter.getMaxValue()));
	  }
		     
	 return layout;	
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		ConditionalFilter filter = (ConditionalFilter)buttonView.getTag();
		filter.setEnable(isChecked);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		String selected = "";
		if(parent.getCount() == listingOptions.length){
		  selected = (String)parent.getItemAtPosition(position);	
		  ListingTypeFilter ltf = (ListingTypeFilter)FilterHandler.getInstance().getItemFilter(Constants.FILTER_ID_LISTING_TYPE); 	
		   if(selected.equals(ALL))
			  ltf.setTypes(new String[]{});
		   else
		     ltf.setTypes(new String[] {selected});
		}
		else if(parent.getCount() == ConditionOptions.length){
			selected = (String)parent.getItemAtPosition(position);	
			ConditionFilter condition = (ConditionFilter)FilterHandler.getInstance().getItemFilter(Constants.FILTER_ID_CONDITION); 	
			 if(selected.equals(ALL))
			  condition.setCondition(null);
			 else
			  condition.setCondition(selected);	
		}
		else{
			selected = (String)parent.getItemAtPosition(position);	
			LocationFilter lf = (LocationFilter)FilterHandler.getInstance().getItemFilter(Constants.FILTER_ID_LOCATION); 	
			lf.setSelectedCountry(selected);		
			
		}
		
	}

	@Override
	public void onBackPressed() {
	  validateNPopulateValues();	
	  super.onBackPressed();	
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onClick(View v) {
	  if(v == this.okBtn){
		  onBackPressed();
	  }	 
	  else{
		this.flipper.showNext();
		 if(this.firstPage)
		  this.moreBtn.setText("Back");
		 else
		   this.moreBtn.setText("More...");
		 
		 this.firstPage = !this.firstPage; 
	  }
		
	}

	private void validateNPopulateValues(){
	  
		for(int i=0;i<3;i++){
			
		   final EditText min = (EditText)findViewById(this.min_max_IDS[i][0]);	
		   final EditText max = (EditText)findViewById(this.min_max_IDS[i][1]);
		   
		   RangeFilter filter = (RangeFilter)min.getTag();
		   try{
			   
			int minValue = Integer.parseInt(min.getText().toString());
		    int maxValue = Integer.parseInt(max.getText().toString());
			   
			 if(minValue > maxValue)
				 maxValue = minValue;
			
			filter.setMinValue(minValue);
			filter.setMaxValue(maxValue);
			filter.setActive(true);
			
		   }catch(NumberFormatException exp){ 
			 // exp.printStackTrace();  
			  filter.setActive(false); 
		   }catch(IllegalArgumentException exp){
			  // exp.printStackTrace();
			   filter.setActive(false);
		   }
		   
		}		 	
		
	}
	
}
