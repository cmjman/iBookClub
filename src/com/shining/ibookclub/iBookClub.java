package com.shining.ibookclub;

import android.app.Application;
import android.graphics.Typeface;

public class iBookClub extends Application{
	
	public static final String SERVER_URL ="http://192.168.126.50:8004/iBookClubServer/"; 
	   
//  public static final String SERVER_URL ="http://1.ibookclubserver.sinaapp.com/"; 

	public static final String Douban_API_KEY="003afe0642e755f700b0fa12c8b601e5"; 
	
	public static final String Douban_ISBN_URL="https://api.douban.com/v2/book/isbn/";
	
	public static final String Douban_SEARCH_URL="https://api.douban.com/v2/book/search?q=";
	
	public static final String Douban_BOOK_URL="http://book.douban.com/isbn/";
	
	public static final String APP_CACHE_URI="sdcard/iBookclubCache/";
	
	public static Typeface typeFace;
	
	public void onCreate() {
		super.onCreate();
		
		typeFace =Typeface.createFromAsset(getAssets(),"fonts/typeface.otf");
	}
	
}
