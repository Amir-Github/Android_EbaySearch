package com.amir.ebay;

import java.util.HashMap;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
public class SimpleContainerDesigner implements ContainerDesigner {
    
	private final static int WIDTH    = 210;
	private final static int HEIGHT   = 210; 
	
	private int containerWidth;
	private int containerHeight;
	private int top;
	
	private Rect container;
	private RectF containerF;
	
	private final static float BORDER     = 3.0f;
	private final static int BORDER_COLOR = 0xFFF0F9CC;
	private final static int BOTTOM_COLOR = 0xEE617A79;
	
	private final static Paint simplePaint  = new Paint(Paint.ANTI_ALIAS_FLAG | 
			                                            Paint.FILTER_BITMAP_FLAG);
	
	private final static Paint bottomPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final static Paint borderPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private final static Paint textPaint    = new Paint();
	
	private float densityToPixel;
	
	
	static{
		simplePaint.setColor(0xEEF0F9CC);
		
		bottomPaint.setColor(BOTTOM_COLOR);
		bottomPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		
		borderPaint.setColor(BORDER_COLOR);
		borderPaint.setStrokeWidth(BORDER);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setShadowLayer(1.0f, 1.0f,1.0f, 0xddffffff);
		borderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		
		textPaint.setColor(0xffffffff);
		textPaint.setTextSize(8);
	}
	
	
	public SimpleContainerDesigner(DisplayMetrics metrics){
		
        this.densityToPixel = metrics.density;
		
        this.containerWidth  = convertDipToPixel(WIDTH);
        this.containerHeight = convertDipToPixel(HEIGHT);
        this.top             = this.containerHeight-(containerHeight/4);
        
        this.container  = new Rect(0,0,this.containerWidth,this.containerHeight);
		this.containerF = new RectF(container);
       
	}
	
	@Override
	public Bitmap getContainer(Bitmap image, HashMap<String, String> content) {

         Bitmap container = makeEmptyContainer();
         if(image != null)
           addImage(container,image);
         if(content != null && content.size() > 0)
           addContent(container,content);
		
       return container;  
	}

	@Override
	public int getContainerHeight() {
       return this.containerHeight;
	}

	@Override
	public int getContainerWidth() {
	  return this.containerWidth;
	}

	@Override
	public void setContainerSize(int width, int height) {
		this.containerHeight = height;
		this.containerWidth  = width;
	}

	@Override
	public Bitmap addContent(Bitmap container,HashMap<String, String> content) {
        
		Canvas canvas = new Canvas(container);
		textPaint.setTextSize(10);
		
        //Simple Container Designer just shows two lines of text..
		Iterator<String> iter = content.keySet().iterator();
		int i = 1;
		 while(iter.hasNext() && i<=2){
			 
		  String key   = iter.next();
		  String value = content.get(key);
			 
		  value = key+" : "+value;
		  int size = textPaint.breakText(value, true, this.containerWidth-14, null);
		  value = value.substring(0, size);
		  
		  canvas.drawText(value, 7, top+(12*i), textPaint);
		  
		  i++;	 
		 }
		
		return container;
	}

	@Override
	public Bitmap addImage(Bitmap container,Bitmap image) {
		
		final int width  = this.containerWidth;
    	
    	final int imageWidth = image.getWidth();
    	final int imageHeight= image.getHeight();
    	
        final float scale = Math.min((float) (width*0.8f) / (float) imageWidth, 
                   (float) (this.top*0.9f) / (float) imageHeight);
  
        final int scaledWidth  = (int)(scale*imageWidth); 
        final int scaledHeight = (int)(scale*imageHeight); 	
        
        final float x = (width - scaledWidth) / 2.0f;
        final float y = (this.top - scaledHeight) / 2.0f;

        final Bitmap decored = Bitmap.createScaledBitmap(image, scaledWidth, scaledHeight, true);   
        
        
        Canvas canvas = new Canvas(container);
        
        simplePaint.setAlpha(0xff);        
        canvas.drawBitmap(decored, x ,y ,simplePaint);
        
      return container;	
		
	}

	@Override
	public Bitmap makeEmptyContainer() {
		
	    Bitmap decored = Bitmap.createBitmap(this.containerWidth, this.containerHeight, Bitmap.Config.ARGB_8888);
		
		final Canvas canvas = new Canvas(decored);	
        
		final RectF rectf = this.containerF;
		
		simplePaint.setAlpha(0xcc);
		canvas.drawRoundRect(rectf, 12, 12, simplePaint);
		
		final int h = (this.containerHeight/4);
		Rect rect = new Rect(0,this.containerHeight-h,this.containerWidth,this.containerHeight);
		canvas.drawRect(rect, bottomPaint);

		canvas.drawRoundRect(rectf,12,12, borderPaint);	  
		
	  return decored;	
	}
	
	
	protected int convertDipToPixel(int dip){
		return (int)(dip/this.densityToPixel);
	}
	

}
