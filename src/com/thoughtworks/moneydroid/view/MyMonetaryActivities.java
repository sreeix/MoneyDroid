package com.thoughtworks.moneydroid.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.thoughtworks.R;

public class MyMonetaryActivities extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TextView myMonetaryViews = (TextView)findViewById(R.id.my_monetary_view);
		myMonetaryViews.append("Hey I changed the text");
		
	}
}