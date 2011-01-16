package com.amir.ebay;

/* 
 * http://android-journey.blogspot.com
 * @author Amir Sadrinia
 */
import java.util.HashMap;

import com.ebay.xml.EbayItem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowDetails extends Activity implements OnClickListener{

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.showdetails);
         
         final ImageView imageView = (ImageView)findViewById(R.id.image);
         final TextView view       = (TextView)findViewById(R.id.viewBox); 
         final Button button       = (Button)findViewById(R.id.backBtn);
         button.setOnClickListener(this);
         
         Intent intent = getIntent();
         final HashMap<String, String> item = (HashMap<String,String>)intent.getSerializableExtra("Item");
         final Bitmap pic    = (Bitmap)intent.getParcelableExtra("Pic");
         
         imageView.setImageBitmap(pic);
         StringBuilder builder = new StringBuilder();
          
         final String title      = item.get(EbayItem.TITLE);
         final String price      = item.get("Current Price");
         final String location   = item.get("Location");
         final String shipping   = item.get("Shipping Cost");
         final String time       = item.get("Time Left");
         final String ShippingTo = item.get("Shipping To");
         final String listing    = item.get("Listing Type");
         final String empty  = " - ";
         
         builder.append("TITLE : "+(title == null ? empty : title)+"\n");
         builder.append("PRICE : "+(price == null ? empty : price)+"\n");
         builder.append("LOCATION : "+(location == null ? empty : location)+"\n");
         builder.append("SHIPPING : "+(shipping == null ? empty : shipping)+"\n");
         builder.append("TIME LEFT : "+(time == null ? empty : EbayItem.convertTime(time))+"\n");
         builder.append("SHIPPING TO : "+(ShippingTo == null ? empty : ShippingTo)+"\n");
         builder.append("LISTING TYPE : "+(listing == null ? empty : listing));
         
         view.setText("\n"+builder.toString());
         button.setBackgroundResource(ResourceHelper.getButtonSelector());
         overridePendingTransition(R.anim.appear, R.anim.disapear);
       
	}

	@Override
	public void onClick(View v) {
		this.finish();
	}
	
}
