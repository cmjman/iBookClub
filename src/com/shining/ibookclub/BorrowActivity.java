package com.shining.ibookclub;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.dao.MyBookDao;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.LoginSingleton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Button;

public class BorrowActivity extends Activity {
	
	private WebView webview_MyBook;
	
	private Button button_editInfo;
	
	private static BookBean bookBean=new BookBean();
	
	private Handler handler = new Handler();
	
	ArrayList<BookBean> bookList=new ArrayList<BookBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow);
		
		
		
    	webview_MyBook=(WebView)findViewById(R.id.webview_MyBook);
    	
       	webview_MyBook.getSettings().setSupportZoom(false);
    	webview_MyBook.getSettings().setJavaScriptCanOpenWindowsAutomatically(
				true);
    	webview_MyBook.getSettings().setJavaScriptEnabled(true);
    	
    	GetMyBookInfo getMyBook=new GetMyBookInfo();
    	getMyBook.execute((Void)null);
    	
    	webview_MyBook.loadUrl("file:///android_asset/mybook.html");
		
    	
    	webview_MyBook.addJavascriptInterface(new Object() {
			public String getMyBook() {
				return MyBookDao.getInstance().list().toString();
			}

	
		

			public void getDetail(final String isbn) {
				
				handler.post(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setClass(getBaseContext(),
								BookDetailActivity.class);
						intent.putExtra("ISBN", isbn);
						startActivity(intent);
					}
				});
			}
	
		}, "mybook");

    	
    	
    	button_editInfo=(Button)findViewById(R.id.button_editInfo);
    	button_editInfo.setTypeface(iBookClub.typeFace);
	}
	
	 public class GetMyBookInfo  extends AsyncTask<Void, Void, Boolean> {

			
			protected Boolean doInBackground(Void... arg0) {
		
				String httpUrl=LoginSingleton.SERVER_URL+"GetBookServlet";
				
				if(LoginSingleton.isLoginSuccess()){
					
				
					List <NameValuePair> params = new ArrayList <NameValuePair>(); 
			        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
			
			        
					try{
				
						HttpUtility httpUtility=new HttpUtility(httpUrl,params);
						
						String strResult=httpUtility.doPost();
						System.out.println("GetMyBook:"+strResult);
						Gson gson = new Gson();
						bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
						
					}
					catch(Exception e){
						return false;
					}
		
				}
				return true;
			}
			
			protected void onPostExecute(final Boolean success) {
				
				MyBookDao.getInstance().deleteAll();
				
				for(BookBean book:bookList){
			
					MyBookDao.getInstance().create(book);
				}
				
			}
		 }

		 
		 
		
				


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.borrow, menu);
		return true;
	}

}
