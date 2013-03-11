package com.shining.ibookclub;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.LoginSingleton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PostActivity extends Activity {
	
	private Button button_scan;
	
	private Button button_searchByIsbn;
	
	private Button button_lend;

	private EditText edittext_isbn;
	
	private String isbn;
	
	private SearchBookTask searchBookTask;
	
	private WebView webview_BookInfo;
	
	private static BookBean bookBean=new BookBean();
	
	private static String APIKey="003afe0642e755f700b0fa12c8b601e5";
	
	private static String URL = "https://api.douban.com/v2/book/isbn/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		
		
		
    	
    	if (android.os.Build.VERSION.SDK_INT >= 9) {
    	      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	      StrictMode.setThreadPolicy(policy);
    	    }
    	    
    	    
    	
    	
    	
    	 button_scan=(Button)findViewById(R.id.button_scan);
         button_scan.setOnClickListener(new OnClickListener(){
        	 
        	 public void onClick(View view){
        		 
        		 //条形码扫描入口，Intent指向ZXING解析核心库，点击扫描按钮后，可跳转到摄像头解析界面
        		 Intent intent = new Intent("com.shining.iBookClub.library.com.google.zxing.client.android.SCAN");
        	     intent.putExtra("SCAN_MODE", "ONE_D_MODE");
        	     startActivityForResult(intent, 0);
        	 }
         });
         button_scan.setTypeface(iBookClub.typeFace);
         
         edittext_isbn=(EditText)findViewById(R.id.edittext_isbn);
         edittext_isbn.setTypeface(iBookClub.typeFace);
         
         button_searchByIsbn=(Button)findViewById(R.id.button_searchByIsbn);
         
         button_searchByIsbn.setOnClickListener(new OnClickListener(){
        	 
        	 public void onClick(View view){
        		 if(edittext_isbn.getText()!=null){
        			   isbn=edittext_isbn.getText().toString();
        			   searchBookTask=new SearchBookTask();
        			   searchBookTask.execute((Void) null);
        			
        		 }
        		 
        	 }
         });
         
         button_searchByIsbn.setTypeface(iBookClub.typeFace);
         
         button_lend=(Button)findViewById(R.id.button_lend);
         button_lend.setTypeface(iBookClub.typeFace);
         setLend();
         
         
     	webview_BookInfo = (WebView)findViewById(R.id.webview_BookInfo);
     	webview_BookInfo.getSettings().setSupportZoom(false);
     	webview_BookInfo.getSettings().setJavaScriptCanOpenWindowsAutomatically(
				true);
     	webview_BookInfo.getSettings().setJavaScriptEnabled(true);

     	
	}
	
	public class SearchBookTask extends AsyncTask<Void, Void, Boolean> {
		
		protected Boolean doInBackground(Void... arg0) {
		
			 	bookBean=  getResultByIsbn();
			 
				Boolean result=false;
				String httpUrl=LoginSingleton.SERVER_URL+"CheckBookServlet";
				
				if(LoginSingleton.isLoginSuccess()){
					
					List <NameValuePair> params = new ArrayList <NameValuePair>(); 
			        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
			        params.add(new BasicNameValuePair("isbn",isbn));
			  
					try{
						
						HttpUtility httpUtility=new HttpUtility(httpUrl,params);
				
						String strResult=httpUtility.doPost();
						System.out.println(strResult);
						JSONObject jsonObject = new JSONObject(strResult) ;
						Boolean actionResult=jsonObject.getBoolean("ActionResult");
							
						if(actionResult){
							setHasLend();
						}
					}
					catch(Exception e){
						return false;
					}
					return result;
				}
				
			 
			 
	
			
			return true;
		}
		
		protected void onPostExecute(final Boolean success) {
			
			 LoadBookInfo();
		}
		
		protected void onCancelled() {
			
		}
		
		
	}
	
	private void deleteBookFromLibray(){
		
		DeleteBookTask deleteBookTask =new DeleteBookTask();
		deleteBookTask.execute((Void)null);
		
	}
	
	public class DeleteBookTask extends AsyncTask<Void,Void,Boolean>{
		
		protected Boolean doInBackground(Void... arg0){
			
			Boolean result=false;
			String httpUrl=LoginSingleton.SERVER_URL+"DeleteBookServlet";
			
			if(LoginSingleton.isLoginSuccess()){
				
				List <NameValuePair> params = new ArrayList <NameValuePair>(); 
		        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
		        params.add(new BasicNameValuePair("isbn",isbn));
		        
		        try{
		        	
		        	HttpUtility httpUtility=new HttpUtility(httpUrl,params);
		        	String resultStr=	httpUtility.doPost();
		        	
		        	JSONObject jsonObj=new JSONObject(resultStr);
		        	
				
		        	if(jsonObj.getString("Result")=="Success"){
		        		
		        		result=true;
		        	}
		        
				
			}
			catch(Exception e){
				return false;
			}
		        return result;
			}	
		        
			
			
			return null;
			
			
		}
		
		
	}
	
	

	 
	 
	 private BookBean getResultByIsbn(){
	

				try{	
				
				URL url = new URL(URL+isbn+"?apikey="+APIKey);      
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
				conn.setConnectTimeout(5 * 1000);      
				conn.setRequestMethod("GET");      
		
				
				InputStream inStream = conn.getInputStream(); 
		
				return getBookInfo(inStream);
				}catch (Exception e) {  
				e.printStackTrace();  
				}  
		
				return null;
	}
	 
	 
	 
	 public BookBean getBookInfo(InputStream inputStream){
		 

		
		 String str="";
		 
		 JSONObject json;

		
		 try{
	
		 
		  BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		    StringBuffer buffer = new StringBuffer();
		    String line = "";
		    while ((line = in.readLine()) != null){
		      buffer.append(line);
		    }
		    
		    str=buffer.toString();
	 
		
		 System.out.println(str);
		 
	
		 
		 json = new JSONObject(str);
		 
		
		 bookBean.setAuthor(json.getString("author"));
		 bookBean.setBookcover_url(json.getString("image"));
		 bookBean.setBookname(json.getString("title"));
		 bookBean.setIsbn(isbn);
		 bookBean.setPrice(json.getString("price"));
		 bookBean.setPublisher(json.getString("publisher"));
		 bookBean.setSummary(json.getString("summary"));
		 
			
		 
		 } catch (Exception e) {
			e.printStackTrace();
		}  
		 
		 
		 return bookBean;
		 
	 }
	 
		
	
	public void LoadBookInfo(){
		 
		webview_BookInfo.loadUrl("file:///android_asset/book_info.html");
	
     	webview_BookInfo.addJavascriptInterface(new Object() {
     		
     		
			public String getBookName() {
				return bookBean.getBookname();
			}

			public String getBookSummary() {
				return bookBean.getSummary();
			}

			public String getBookImageUrl() {
				return bookBean.getBookcover_url();
			}

			public String getBookAuthor() {
				return bookBean.getAuthor();
			}
			
     		
			
		}, "bookDetail");
     	
     	findViewById(R.id.image).setVisibility(View.GONE);
     	
	   }
		
		 private void setLend(){
				button_lend.setText("发布到图书馆");
				button_lend.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
				
						
						if(bookBean.getIsbn()!=null){
						
							Intent intent=new Intent(getBaseContext(),PostBookActivity.class);
					
							intent.putExtra("bean", bookBean);
						
							startActivity(intent);
						}else
							Toast.makeText(getBaseContext(), "请先扫描或者搜索需要发布的图书！", Toast.LENGTH_SHORT).show();
						
					}
				});
			}

			private void setHasLend() {
				button_lend.setText("已发布,点此可下架");
				button_lend.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					//	BookInfoDao.getInstance().delete(bookInfo.getIsbn());
						deleteBookFromLibray();
						setLend();
						
					}
				});
			}
	
	
		 public void onActivityResult(int requestCode, int resultCode, Intent data) {
			 	
			  if (null == data) 
				  return;
		
			  if (requestCode == 0) {
		
				   isbn=data.getStringExtra("SCAN_RESULT");
				   searchBookTask=new SearchBookTask();
				   searchBookTask.execute((Void) null);
			  }
		 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}

}
