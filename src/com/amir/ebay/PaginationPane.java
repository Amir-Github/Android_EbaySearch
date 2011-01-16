package com.amir.ebay;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class PaginationPane extends LinearLayout implements OnClickListener {
	
	private final static int BACKGROUND_COLOR      = 0xFF000000;
	private final static int UNSELECTED_BACKGROUND = 0xFFFFFFFF;
	private final static int UNSELECTED_FOREGROUND = 0xFF000000;
	private final static int SELECTED_BACKGROUND   = 0xFF032834;
	private final static int SELECTED_FOREGROUND   = 0xFFFFFFFF;
	private final static String NEXT_SIGN          = ">>";
	private final static String PREVIOUS_SIGN      = "<<";
	private final static int MOVE_BACKWARD         = -1;
	private final static int MOVE_FORWARD          = 1;
	
	private TextView[] boxes;
	private Button previous;
	private Button next;
	private OnClickListener nextListener;
	
	private int pageCount    = 15;
	private int pagesPerPane = 10;
	private int selectedPage = 1;
	private int currentPane   = 1;
		
	public final static int PREVIOUS_BTN_ID = 0x111111;
	public final static int NEXT_BTN_ID     = 0x222222;
	public final static int EMPTY_PANE      = 0;
	
	
	public PaginationPane(Context context,
			              DisplayMetrics dm,
			              OnClickListener listener,
			              int pageCount,
			              int itemPerPage) {
		super(context);
		
		this.pageCount    = pageCount;
		this.pagesPerPane = itemPerPage;
		
		final int width       = dm.widthPixels;
		
		LinearLayout.LayoutParams bParams = new LinearLayout.LayoutParams((width/4)-15,
				                                                         30,
				                                                         Gravity.BOTTOM);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				                                                         LinearLayout.LayoutParams.WRAP_CONTENT,
				                                                         Gravity.BOTTOM);
		params.setMargins(3, 3, 3, 3);
		
		final boolean isMore    = this.pageCount > this.pagesPerPane; 
		final Typeface typeface = ResourceHelper.getButtonTypeFace(context);
		final int selector      = ResourceHelper.getButtonSelector();
		
		this.previous = new Button(getContext());
		this.previous.setTypeface(typeface);
		this.previous.setId(PREVIOUS_BTN_ID);
		this.previous.setText("Previous");
		this.previous.setTextSize(10);
		this.previous.setLayoutParams(bParams);
		this.previous.setBackgroundResource(selector);
		this.addView(this.previous);
	
		this.boxes = new TextView[this.pagesPerPane+2];
		this.boxes[0] = new TextView(getContext());	
		this.boxes[0].setLayoutParams(params);	
		this.boxes[0].setTextSize(10);
		this.boxes[0].setText(PREVIOUS_SIGN);
		this.boxes[0].setVisibility(GONE);
		this.addView(this.boxes[0]);
		
		final int length = this.boxes.length;
		for(int i=1;i<length-1;i++){
		  this.boxes[i] = new TextView(getContext());	
		  this.boxes[i].setLayoutParams(params);	
		  this.boxes[i].setTextSize(10);
		  this.boxes[i].setText(""+i);  
		  this.addView(this.boxes[i]);
		}
		
		this.boxes[length-1] = new TextView(getContext());	
		this.boxes[length-1].setLayoutParams(params);	
		this.boxes[length-1].setTextSize(10);
		this.boxes[length-1].setText(NEXT_SIGN);
		this.boxes[length-1].setVisibility(isMore ? VISIBLE : GONE);
		this.addView(this.boxes[length-1]);
		
		checkColors();
		
		setPadding(5,3,5,0);
		
		this.next = new Button(getContext());
		this.next.setTypeface(typeface);
		this.next.setId(NEXT_BTN_ID);
		this.next.setText("Next");
		this.next.setTextSize(10);
		this.next.setLayoutParams(bParams);
		this.next.setBackgroundResource(selector);
		this.addView(this.next);
		
		this.previous.setOnClickListener(this);
		this.previous.setEnabled(false);
		this.next.setOnClickListener(this);
		this.next.setEnabled(false);
		
		this.setBackgroundColor(BACKGROUND_COLOR);
	}
	
	
	private void checkColors(){
	
		final boolean isEmpty = this.pageCount == EMPTY_PANE;
		
		final int unselected_Background = isEmpty ? BACKGROUND_COLOR : UNSELECTED_BACKGROUND;
		final int unselected_Foreground = isEmpty ? BACKGROUND_COLOR : UNSELECTED_FOREGROUND;
		final int selected_Background   = isEmpty ? BACKGROUND_COLOR : SELECTED_BACKGROUND;
		final int selected_Foreground   = isEmpty ? BACKGROUND_COLOR : SELECTED_FOREGROUND;
		final int selectedIndex = getIndex();
		
		this.boxes[0].setBackgroundColor(unselected_Background);
		this.boxes[0].setTextColor(unselected_Foreground);
		
		final int length = this.boxes.length;
		for(int i=1;i<length-1;i++){
		  this.boxes[i].setBackgroundColor(isEmpty || (i > (this.pageCount-((this.currentPane)*this.pagesPerPane))) ? BACKGROUND_COLOR : UNSELECTED_BACKGROUND);
		  this.boxes[i].setTextColor(isEmpty || (i > (this.pageCount-((this.currentPane)*this.pagesPerPane))) ? BACKGROUND_COLOR : UNSELECTED_FOREGROUND);
		}
		
		this.boxes[length-1].setBackgroundColor(unselected_Background);
		this.boxes[length-1].setTextColor(unselected_Foreground);

		this.boxes[selectedIndex].setBackgroundColor(selected_Background);
		this.boxes[selectedIndex].setTextColor(selected_Foreground);
		
	}
	
	public void setOnClickListener(OnClickListener listener){
		this.nextListener = listener;
	}
	
	public int getPageCount() {
		return pageCount;
	}


	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;	
		this.boxes[0].setVisibility(GONE);
		this.boxes[this.boxes.length-1].setVisibility(this.pageCount > this.pagesPerPane ? VISIBLE : GONE);
		this.selectedPage = 1;
		this.currentPane  = 0;
		updateNumbers();
		this.previous.setEnabled(false);
		this.next.setEnabled(this.pageCount > 1);
		invalidate();
			
	}

	public int getSelectedPage(){
		return this.selectedPage;
	}
	
	public void setSelectedPage(int page){
	  if(page > this.pageCount || page < 1 || page == this.selectedPage)
		  return;
	  final int difference = page - this.selectedPage;	
	  move(difference);
	}
	
	@Override
	public void onClick(View v) {
		if(v == this.previous){
		  this.nextListener.onClick(this.previous);
		  move(MOVE_BACKWARD);
		}
		else {		
		 this.nextListener.onClick(this.next);
	     move(MOVE_FORWARD);
		}	
	}
	
	private void move(int howMany){
	   
        int index = getIndex();
			
		this.boxes[index].setBackgroundColor(UNSELECTED_BACKGROUND);
		this.boxes[index].setTextColor(UNSELECTED_FOREGROUND);
		
		this.selectedPage += howMany;
		
		 if(this.selectedPage < 1)
		   this.selectedPage = 1;
		 if(this.selectedPage >= this.pageCount)
			 this.selectedPage = this.pageCount;
			 
		index = getIndex();
		this.boxes[index].setBackgroundColor(SELECTED_BACKGROUND);
		this.boxes[index].setTextColor(SELECTED_FOREGROUND);
		
		updateNumbers();
		
	    this.boxes[0].setVisibility(this.selectedPage > this.pagesPerPane ? VISIBLE : GONE);
	    this.previous.setEnabled(this.selectedPage != 1);
	    this.boxes[this.boxes.length-1].setVisibility((this.currentPane+1)*this.pagesPerPane > this.pageCount ? GONE : VISIBLE);
	    this.next.setEnabled(this.selectedPage != this.pageCount);
	    
	    invalidate();
	}
	
	
	private int getIndex(){
		int index = this.selectedPage%this.pagesPerPane;
		if(index == 0)
			index = this.pagesPerPane;
     return index;
	}
	
	private void updateNumbers(){
		final int remainder  = this.selectedPage%this.pagesPerPane;
		this.currentPane = remainder == 0 ? (this.selectedPage/this.pagesPerPane)-1 : this.selectedPage/this.pagesPerPane;
		
		for(int i=1;i<=this.pagesPerPane;i++)
		  this.boxes[i].setText(Integer.toString((this.currentPane*this.pagesPerPane)+i));	
		
        checkColors();
	}
	
}
