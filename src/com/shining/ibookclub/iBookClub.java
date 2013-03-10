package com.shining.ibookclub;

import android.app.Application;
import android.graphics.Typeface;

public class iBookClub extends Application{
	
	public static Typeface typeFace;
	
	public void onCreate() {
		super.onCreate();
		
		typeFace =Typeface.createFromAsset(getAssets(),"fonts/typeface.otf");
	}
	
}
