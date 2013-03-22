package com.shining.ibookclub;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.shining.ibookclub.support.FinalConstants;
import com.shining.ibookclub.support.LazyAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class BuyBookDetailActivity extends Activity {

	private String url;
	
	private String htmlContent;
	
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
    	
    	HtmlParser htmlParser=new HtmlParser();
    	htmlParser.execute(url);
	}
	
	class HtmlParser extends AsyncTask<String,Void,Boolean>{
		
		
		@Override
		protected Boolean doInBackground(String... params) {
				
			Boolean	result=false;
				
			try {  
				     
				URL url = new URL(params[0]);  
				        
				URLConnection ucon = url.openConnection();  
				InputStream instr = ucon.getInputStream();  

				BufferedReader br = new BufferedReader(new InputStreamReader(instr));  
				StringBuilder sb=new StringBuilder();
				String line=""; 
				while ((line = br.readLine()) != null) {  
				     
					sb.append(line);
				}  
				
				htmlContent=sb.toString();  
				result=true;
				    
			} catch (Exception e) {  
				      
				e.printStackTrace();
				    
			}
				return result;
			}  
		
		protected void onPostExecute(final Boolean success) {
			
			if(success){
				
				
				Document document = Jsoup.parse(htmlContent);  
				
				Elements elements= document.getElementsByClass("noline");
				
				String html="<html><body><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>" +elements.html()+"</body></html>";

				if(elements.isEmpty()){
					
					Toast.makeText(getBaseContext(), "网店暂无该书出售！", Toast.LENGTH_SHORT);
				}
				else{
					
					File file=new File(FinalConstants.APP_CACHE_URI+"buybook.html");
					
					FileOutputStream out;
					try {
						out = new FileOutputStream(file);
						out.write(html.getBytes());
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
				
				}
		
				webview.loadUrl("file:///sdcard/iBookClubCache/buybook.html");
			}
		}
				 
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy_book_detail, menu);
		return true;
	}

}
