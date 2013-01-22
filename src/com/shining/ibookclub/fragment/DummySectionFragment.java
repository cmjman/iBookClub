package com.shining.ibookclub.fragment;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import com.shining.ibookclub.*;
import com.shining.ibookclub.bean.BookInfo;
import com.shining.ibookclub.dao.BookInfoDao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public  class DummySectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	private int SEC_NUMBER_INTEGER;
	
	private WebView webview_BookForBorrow;
	
	private WebView webview_BookInfo;
	
	private Handler handler = new Handler();
	
	private Button button_scan;
	
	private Button button_lend;
	
	private String isbn;
	
	private static BookInfo bookInfo;
	
	private static String APIKey="003afe0642e755f700b0fa12c8b601e5";
	
	private static String URL = "http://api.douban.com/book/subject/isbn/";
	
	private static String PATH_COVER = Environment.getExternalStorageDirectory() + "/iBookClubData/";   
	

	public DummySectionFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		SEC_NUMBER_INTEGER=getArguments().getInt(ARG_SECTION_NUMBER);
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText(Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));
	
		if(SEC_NUMBER_INTEGER==1){
			
			
			return inflater.inflate(R.layout.fragment_borrow, container, false);
		}
		else if(SEC_NUMBER_INTEGER==2){
			return inflater.inflate(R.layout.fragment_lend, container,false);
		}
		
	//	if()
		
		return textView;
	}
	
	 public void onActivityCreated(Bundle savedInstanceState){
			
		    super.onCreate(savedInstanceState);
		   
		    if(SEC_NUMBER_INTEGER==1){
		    	
		    	webview_BookForBorrow=(WebView)getActivity().findViewById(R.id.webview_BookForBorrow);
		    	
		    	webview_BookForBorrow.getSettings().setSupportZoom(false);
		    	webview_BookForBorrow.getSettings().setJavaScriptCanOpenWindowsAutomatically(
						true);
		    	webview_BookForBorrow.getSettings().setJavaScriptEnabled(true);

		    	webview_BookForBorrow.loadUrl("file:///android_asset/book_list_borrow.html");
		    	
		    	webview_BookForBorrow.addJavascriptInterface(new Object() {
					public String getBookResult() {
						return BookInfoDao.getInstance().list().toString();
					}

			
				

					public void getDetail(final String isbn) {
						
						handler.post(new Runnable() {
							@Override
							public void run() {
								Intent intent = new Intent();
							//	intent.setClass(getActivity(),
						//				SearchBookActivity.class);
							//	intent.putExtra("ISBN", isbn);
							//	startActivity(intent);
							}
						});
					}
					
				

			
				}, "bookShelfControl");
		    }
		    else if(SEC_NUMBER_INTEGER==2){
		    	
		    	/*
		    	
		    	if (android.os.Build.VERSION.SDK_INT >= 9) {
		    	      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    	      StrictMode.setThreadPolicy(policy);
		    	    }
		    	    
		    	    */
		    	
		    	 button_scan=(Button)getActivity().findViewById(R.id.button_scan);
		         button_scan.setOnClickListener(new OnClickListener(){
		        	 
		        	 public void onClick(View view){
		        		 
		        		 Intent intent = new Intent("com.shining.iBookClub.library.com.google.zxing.client.android.SCAN");
		        	     intent.putExtra("SCAN_MODE", "ONE_D_MODE");
		        	     startActivityForResult(intent, 0);
		        	 }
		         });
		         
		    //     button_lend=(Button)getActivity().findViewById(R.id.button_lend);
		    //     setLend();
		         
		         
		     	webview_BookInfo = (WebView)getActivity().findViewById(R.id.webview_BookInfo);
		     	webview_BookInfo.getSettings().setSupportZoom(false);
		     	webview_BookInfo.getSettings().setJavaScriptCanOpenWindowsAutomatically(
						true);
		     	webview_BookInfo.getSettings().setJavaScriptEnabled(true);

		     


		    	
		    }
		   
		    
	 }
	 
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 	
		  if (null == data) 
			  return;
	
		if (requestCode == 0) {
		
		
		
	
	   
	   isbn=data.getStringExtra("SCAN_RESULT");
	   
	//   System.out.println(isbn);
	  
	 //  textview_isbn.setText("ISBN:"+isbn);
	   
	//   Intent intent = new Intent();
	//	intent.setClass(this, SearchBookActivity.class);
		
		//intent.putExtra("ISBN", isbn);
		//this.startActivity(intent);
	   
	//	getBookInfoRun.run();
	   
	   final Object object = new Object();  
	   
	   new Thread(){  
			  
			
			  public void run() {  
				  try {
					//  bookInfo = getResultByIsbn(isbn);
				//	  synchronized (bookInfo){};   
					 
					  synchronized (object){
					  
						  bookInfo=  getResultByIsbn();
						  
						  object.notify();
					  }
				
					  } catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}
				  
				
					   
					  
				//	  handler1.sendEmptyMessage(0);
			  }  
			    }.start();  
			    
	   
		if(bookInfo==null)
			System.out.println("1234");
		
	   
		webview_BookInfo.loadUrl("file:///android_asset/book_info.html");
		
		synchronized (object){
			
			try {
				object.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

     	webview_BookInfo.addJavascriptInterface(new Object() {
     		
     		
     		
     		
			public String getBookName() {
				return bookInfo.getName();
			}

			public String getBookSummary() {
				return bookInfo.getSummary();
			}

			public String getBookImageUrl() {
				return bookInfo.getImageUrl();
			}

			public String getBookAuthor() {
				return bookInfo.getAuthor();
			}
			
     		
     		
		}, "searchResult");
		}
     	
		}
	   
	 }
	 
	 // Thread getBookInfoRun = 
	/*
	 
	 
	  private Handler handler1 =new Handler(){
			
		   public void handleMessage(Message msg){
			   
			   
			   super.handleMessage(msg);
			   
			
		   }
		 };
	*/
	 
	 
		 private void setLend(){
				button_lend.setText("发布到图书馆");
				button_lend.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						BookInfoDao.getInstance().create(bookInfo);
						setHasLend();
					}
				});
			}

			private void setHasLend() {
				button_lend.setText("已发布");
				button_lend.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						BookInfoDao.getInstance().delete(bookInfo.getIsbn());
						setLend();
					}
				});
			}
		 
		 private void checkNetworkInfo(){
			  
				ConnectivityManager conMan = (ConnectivityManager)getActivity(). getSystemService(Context.CONNECTIVITY_SERVICE);
				State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
				State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			 
				if(mobile==State.CONNECTED||mobile==State.CONNECTING) return;
				if(wifi==State.CONNECTED||wifi==State.CONNECTING) return;
				
				Toast.makeText(getActivity(), "无可用网络，请打开网络连接！", Toast.LENGTH_SHORT).show();  
				
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			 
			  }
		 
		 
		 private BookInfo getResultByIsbn(){
				//	BookInfo dbook=null;
					
	
					
					checkNetworkInfo();
					
		
			
					
					try{	
					
					URL url = new URL(URL+isbn+"?apikey="+APIKey);      
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
					conn.setConnectTimeout(5 * 1000);      
					conn.setRequestMethod("GET");      
				
					
					/////////////////////
					
					InputStream inStream = conn.getInputStream(); 
					
					/////////////////////
				
					
				//	dbook = getBookInfo(inStream);
					return getBookInfo(inStream);
					}catch (Exception e) {  
					e.printStackTrace();  
					}  
					
				//	if(dbook==null)
					//	System.out.println("1111");
					
				//	return dbook;
					return null;
					
					
					}

			
			public BookInfo getBookInfo(InputStream inputStream)/* throws XmlPullParserException, IOException*/ {
				
			//	BookInfo bookInfo1 = new BookInfo();
				
				try{
				
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(inputStream, "UTF-8");
				
				bookInfo=new BookInfo();
				bookInfo.setIsbn(isbn);
				
			
				
				
					for (int i = parser.getEventType(); i != XmlPullParser.END_DOCUMENT; i = parser.next()) {
					
					if (i == XmlPullParser.START_TAG
							&& parser.getName().equals("attribute")
							&& parser.getAttributeValue(0).equals("title")) {
						bookInfo.setName(parser.nextText());
						Log.v("SearchBook", "title>>" + bookInfo.getName());
						continue;
					}
					if (i == XmlPullParser.START_TAG
							&& parser.getName().equals("attribute")
							&& parser.getAttributeValue(0).equals("author")) {
						bookInfo.setAuthor(parser.nextText());
						Log.v("SearchBook", "author>>" + bookInfo.getAuthor());
						continue;
					}
					if (i == XmlPullParser.START_TAG && parser.getName().equals("link")) {
						if (parser.getAttributeValue(1).equals("image")) {
							bookInfo.setImageUrl(parser.getAttributeValue(0));
							Log.v("SearchBook", "image>>" + bookInfo.getImageUrl());
						}
						continue;
					}
					if (i == XmlPullParser.START_TAG
							&& parser.getName().equals("summary")) {
						bookInfo.setSummary(parser.nextText());
						Log.v("SearchBook", "summary>>" + bookInfo.getSummary());
						continue;
					}
					
				}
				Log.v("SearchBook", ">>>>> parse end.");
				
			//	if(bookInfo==null)
				//	System.out.println("5678");
				
				}catch(Exception e){
					e.printStackTrace();
				}
			
			

				return bookInfo;
			}



			public byte[] downImage(String imageUrl) {

					try{
					 URL url = new URL(imageUrl);      
					 HttpURLConnection conn = (HttpURLConnection) url.openConnection();      
					 conn.setConnectTimeout(5 * 1000);      
					 conn.setRequestMethod("GET");      
					 InputStream inStream = conn.getInputStream();      
					 
					 if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){      
					        
						 return readStream(inStream);      
					 }      
					 
					}
					catch(Exception e){
						e.printStackTrace();
					}
					    
					 return null;      
					}
					
					public static byte[] readStream(InputStream inStream) throws Exception{      
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();      
					byte[] buffer = new byte[1024];      
					int len = 0;      
					while( (len=inStream.read(buffer)) != -1){      
					    outStream.write(buffer, 0, len);      
					}      
					outStream.close();      
					inStream.close();      
					return outStream.toByteArray();      
					}    
					
					public void saveFile(Bitmap bm, String fileName) throws IOException {   
					File dirFile = new File(PATH_COVER);   
					if(!dirFile.exists()){   
					    dirFile.mkdir();   
					}   
					File captureFile = new File(PATH_COVER + fileName);   
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(captureFile));   
					bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);   
					bos.flush();   
					bos.close();   
					}   
	 
	
	 
	 public void onResume() {  
		  
	        super.onResume();  
	        
	      //  webview_BookForBorrow.reload();
	    }  
	 
}
