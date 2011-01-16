package com.amir.ebay;


/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import com.ebay.xml.EbayItem;
import com.ebay.xml.Item;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class ShowView extends HorizontalScrollView implements OnTouchListener{

	
	public final static int EBAY_CHANNEL_INDEX   = 0;
	public final static int AMAZON_CHANNEL_INDEX = 1;
	
	public final static int FIRST_PAGE = -2;
	public final static int LAST_PAGE  = -11;
	
	public final static int VIEW_LEFT_PADDING   = 3;
	public final static int VIEW_RIGHT_PADDING  = 9;
	public final static int VIEW_TOP_PADDING    = 2;
	public final static int VIEW_BOTTOM_PADDING = 2;

	private final static int LEFT_PADDING = 5;
	
	private int ROW_COUNT    = 3;
	
	private EventListener listener;
	
	private int event_X = -1;
	private int event_Y = -1;
	
	private ArrayList<FetcherThread> channels;
	private int currentChannel;
	private int currentPage;
	private int currentSize;
	private int windowColumns;
	private Handler handler;
	private ContainerDesigner designer;
	private ProgressDialog dialog;
	private ImageView[] items;
	private Animation bounceAnim;
	
	private Context context;
	private LinearLayout mainFrame;
	private Bitmap defaultImage;
	private LinearLayout[] rows;

	
	public ShowView(Context context,ContainerDesigner cd,int screenHeight) {
		super(context);
        initialize(context,cd,screenHeight);
	}
	
	 private void initialize(Context context,ContainerDesigner designer, int height){
		 if(designer == null)
			 throw new IllegalArgumentException("Designer cannot be null!!");
		 
		 this.designer = designer;
		                            //To handle both Portrait and landscape modes
		 final boolean highEnough = designer.getContainerHeight()*3 < height;
		 this.ROW_COUNT     = highEnough ? 3 : 1;
		 this.windowColumns = highEnough ? 5 : 15;
		 
		 this.rows = new LinearLayout[ROW_COUNT];
		 
		 final int size = this.windowColumns*ROW_COUNT;
         final Context con = getContext(); 
		 
		 this.items   = new ImageView[size];
		 for(int i=0;i<size;i++){
		   this.items[i] = new ImageView(con);
		   this.items[i].setId(i);
		   this.items[i].setPadding(VIEW_LEFT_PADDING,
			                        VIEW_TOP_PADDING, 
			                        VIEW_RIGHT_PADDING,
			                        VIEW_BOTTOM_PADDING);
		   this.items[i].setScaleType(ScaleType.CENTER);
		   this.items[i].setOnTouchListener(this);
		 }
		 
		 this.handler = new ViewHandler();
		 channels = new ArrayList<FetcherThread>();
		 FetcherThread ft = new EbayFetcher();
		 ft.setHandler(handler);
		 channels.add(EBAY_CHANNEL_INDEX,ft);	
		 ft.start();
		 
		 this.currentChannel = 0;
		 this.currentPage    = 0;  ///Nothing yet
		 
		 this.context = context;
		 this.defaultImage = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.not_found_small_1);
		 
		 LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(10000,
				                                                              LinearLayout.LayoutParams.FILL_PARENT);
		 LinearLayout.LayoutParams rowParams  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                                                                              LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                              Gravity.TOP);
		 this.mainFrame = new LinearLayout(this.context);
		 this.mainFrame.setOrientation(LinearLayout.VERTICAL);
		 this.mainFrame.setPadding(LEFT_PADDING, 0, 0, 0);
		// this.mainFrame.setBackgroundColor(0xFF1C2331);
		 this.mainFrame.setLayoutParams(mainParams);
		 
		 for(int i=0;i<ROW_COUNT;i++){
			this.rows[i] = new LinearLayout(this.context); 
			this.rows[i].setLayoutParams(rowParams);
			this.rows[i].setOrientation(LinearLayout.HORIZONTAL);
			this.mainFrame.addView(this.rows[i]);
		 }
		
		 addView(mainFrame);
		 
		 this.bounceAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
		 this.bounceAnim.setZAdjustment(Animation.ZORDER_TOP);
		 
	 }
     
	 public EbayItem[] detachContent(){
         final int size = this.currentSize;
         EbayItem[] content = new EbayItem[size];
         for(int i=0;i<size;i++)
        	content[i] = (EbayItem)this.items[i].getTag(); 
		 
         if(dialog != null && dialog.isShowing())
        	dialog.cancel(); 
         
		 return content;
	 }
	 
	 public void attachContent(EbayItem[] views){
		 if(views == null || views.length == 0)
			 return;
		 
		 for(LinearLayout row : this.rows)
			row.removeAllViews(); 
		 
		 final int lenght = views.length;
		 for(int i=0;i<lenght;i++){
			Bitmap container = designer.getContainer(views[i].getImage(), views[i].getContent());
			this.items[i].setImageBitmap(container);
			this.items[i].setTag(views[i]);   
		 }
		 this.currentSize = lenght;
		 for(int i=0;i<lenght;i++){
			addViews(i%ROW_COUNT, i/ROW_COUNT);
		 }
	 }
	 
	 public void setEventListener(EventListener listener){
		 this.listener = listener;
	 }
	 
	 public void addViews(int row,int column){
	  final ImageView view = this.items[(column*this.ROW_COUNT)+row]; 	 
	  this.rows[row].addView(view,column);
	 // view.startAnimation(this.fadeAnim);
	 }
	 
	 @Override
	 protected boolean awakenScrollBars(int startDelay, boolean invalidate) {
		 return false;
	 }
	 
	 @Override
	 protected void initializeScrollbars(TypedArray a) { 
	 }
	 
	 
	 @Override
	 public void fling(int velocityX) {
		
		 double reduction = Math.abs(velocityX)*0.20;
		 velocityX = (int)(velocityX > 0 ? (velocityX - reduction) : (velocityX + reduction));
		 
		 super.fling(velocityX);		 
	 }
	 
	 public void startNewSearch(int channelIndex,Map searchCriteria){
		 
		 this.channels.get(channelIndex).fetch(searchCriteria, this.windowColumns*ROW_COUNT,1);
		 dialog = ProgressDialog.show(getContext(),"", "Please Wait...");
		 this.currentPage    = 1;
		 this.currentChannel = channelIndex;
	 }
	 
	 public boolean nextPage(){

		 final FetcherThread fetcher = this.channels.get(currentChannel);
		 
		 SearchInfo info = fetcher.getLastSearchInfo();
		 this.currentPage  = info.pageNumber;
		 if(currentPage == info.totalEntries)
               return false;

		 this.currentPage++;
		 fetcher.fetch(fetcher.getLastSearchCriteria(), this.windowColumns*ROW_COUNT,this.currentPage);
		 dialog = ProgressDialog.show(getContext(),"", "Please Wait...");

		 return true;	 
	 }
	 
	 public boolean previousPage(){
		
		 final FetcherThread fetcher = this.channels.get(currentChannel);

		 SearchInfo info = fetcher.getLastSearchInfo();
		 this.currentPage  = info.pageNumber;
		 if(currentPage == 1)
			 return false;

		 this.currentPage--;
		 fetcher.fetch(fetcher.getLastSearchCriteria(), this.windowColumns*ROW_COUNT,this.currentPage);
		 dialog = ProgressDialog.show(getContext(),"", "Please Wait...");	 

	  return true;	 
	 }
	 
	 public int getCurrentPage(){
		 return this.currentPage;
	 }
	 
	 public void setCurrentPage(int currentP){
		 this.currentPage = currentP;
	 }
	 
	//////////////////////////////////////////////////////////// 
	 public FetcherThread lockCurrentFetcher(){
	    final FetcherThread fetcher = this.channels.get(currentChannel);

		fetcher.lockIt();
	  return fetcher;	
	 }
	 
	 public void unlockFetcher(FetcherThread thread){
		 
		 FetcherThread ft = this.channels.get(this.currentChannel);
		 ft.cancelThread();
		// ft.stop();
		 
		 thread.unlockIt(this.handler);
		 this.channels.set(currentChannel, thread);
	 }
/////////////////////////////////////////////////////////////////////	 
	 
	  private class ViewHandler extends Handler{
			
			@Override
			public void handleMessage(Message msg){
				
				for(LinearLayout row : rows)
					row.removeAllViews();
				
				scrollTo(0, 0);
				Bundle bundle = msg.getData();
				 if(msg.what == Constants.FETCH_ERROR){
					final int errorType = bundle.getInt(Constants.ERROR_TYPE);
					String errorMsg = "<<< ERROR!! >>>";
					
					switch(errorType){
					
					case Constants.CONNECTION_ERROR_TYPE : errorMsg = "<<< Connection cannot be established, Pleass Check your internet access and try again >>>";
					                                       break;
					case Constants.FETCH_ERROR_TYPE :      errorMsg = "<<< Server is not reachable at the moment, Please try later >>>"; 
					                                       break;
					case Constants.PARSING_ERROR_TYPE :    errorMsg = "<<< Unexpected Result Format!! >>>";
					                                       break;
					case Constants.UNEXPECTED_ERROR_TYPE : errorMsg = "<<< "+bundle.getString(Constants.ERROR_MSG)+" >>>";
					                                       break;
					
					}			
					currentSize = 0;
					listener.errorOccured(errorMsg); 
	
				 }
				 else{   
					 
					 if(currentPage == 1)
					  listener.searchDone(channels.get(currentChannel).getLastSearchInfo().totalPages);
					  
					  Item[] items = (Item[])bundle.getSerializable(Constants.FETCH_RESULT);
					  populateViews(items);  
					  
					   for(int i=0;i<currentSize;i++){
						   addViews(i%ROW_COUNT, i/ROW_COUNT);
					   }
				 }  
				
				 if(dialog != null)
						dialog.cancel();
				 
			}		
			
			private void populateViews(Item[] data){
				
				SearchInfo info = channels.get(currentChannel).getLastSearchInfo();
				Log.d("<<<Search Info>>>", info.entriesCount+" -- "+info.totalEntries+" -- "+info.pageNumber+" -- "+info.totalPages+" -- "+info.resultPerPage);
			    currentSize = 0;
		        try{
		         
		         if(data != null){
		           
		           currentSize = data.length;
		           final int lenght = currentSize;
		           
		           for(int i=0;i<lenght;i++){
		            
		        	 InputStream in = null;  
		        	 Bitmap bitmap  = null;
		        	   
		        	 try{  
		              URL url = data[i].getImageUrl();
		              
		               if(url == null){
		            	   bitmap = defaultImage;
		               }
		               else{
		                 in = url.openStream();
		                 bitmap = BitmapFactory.decodeStream(in);
		        	   }  
		              
		        	 }catch(IOException exp){
		        	    //Ignore 	 
		        	 }finally{
		        	  if(in != null)	 
		        		in.close(); 
		        	 } 
		        	 
		              Bitmap container = designer.getContainer(bitmap, data[i].getContent());
		              
		              items[i].setImageBitmap(container);
		              data[i].setImage(bitmap);
		              items[i].setTag(data[i]);
		                
		                
		           } 

		         }
		         
		        }catch(Exception exp){
		        	exp.printStackTrace();
		        }
		        
			}
		
	 }		
	
	  @Override
		public boolean onTouch(View v, MotionEvent event) {
			
		 if(this.event_X == (int)event.getX() && this.event_Y == (int)event.getY()){	
				
			   ImageView view = (ImageView)v;
			   view.startAnimation(this.bounceAnim);
			   if(this.listener != null){
				 EbayItem item = (EbayItem)view.getTag();   
			     this.listener.itemClicked(item);
			   } 
		       this.event_X = -1;
		       this.event_Y = -1; 
		   }	
		  else {
			  this.event_X = (int)event.getX();	
			  this.event_Y  = (int)event.getY();	
			}
		   	
		 return false;
		}  
	  
	  
	static interface EventListener{
		public void errorOccured(final String errorMsg);
		public void itemClicked(Item item);
		public void searchDone(int resultNumber);
	}
	
	
}
