package com.shining.ibookclub;

import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BuyBookDetailActivity extends Activity {

	private String url;
	
	private WebView webview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_book_detail);

		webview=(WebView)findViewById(R.id.webview_buybook);
		
		webview.getSettings().setSupportZoom(true);
		
    	webview.setWebViewClient(new WebViewClient(){
          
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
    
            	view.loadUrl(url);   
    
            	return true;
            }
        });
		
		
    	url=getIntent().getStringExtra("url");
		webview.loadUrl(url);
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy_book_detail, menu);
		return true;
	}

}
