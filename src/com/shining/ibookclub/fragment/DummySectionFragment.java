package com.shining.ibookclub.fragment;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shining.ibookclub.*;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.dao.BookInfoDao;
import com.shining.ibookclub.dao.MyBookDao;
import com.shining.ibookclub.support.LoginSingleton;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
	
	public static final Object lock = new Object();  
	
	private int SEC_NUMBER_INTEGER;
	
	private SearchBookTask searchBookTask;
	
	private WebView webview_BookForBorrow;
	
	private WebView webview_BookInfo;
	
	private WebView webview_MyBook;
	
	private TextView text_nickname;
	
	private GridView grid_mybook;
	
	
	private Handler handler = new Handler();
	
	private Button button_scan;
	
	private Button button_searchByIsbn;
	
	private Button button_lend;
	
	private EditText edittext_isbn;
	
	private String isbn;
	
	//private BookInfoDao bookInfoDao;
	
	private Cursor cursor;
	
	private static BookBean bookBean=new BookBean();
	
	private static String APIKey="003afe0642e755f700b0fa12c8b601e5";
	
//	private static String URL = "http://api.douban.com/book/subject/isbn/";
	
	private static String URL = "https://api.douban.com/v2/book/isbn/";
	
	private static String nickname;
	
	
	ArrayList<BookBean> bookList=new ArrayList<BookBean>();
	
//	private static String PATH_COVER = Environment.getExternalStorageDirectory() + "/iBookClubData/";   
	

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
		else if(SEC_NUMBER_INTEGER==3){
			return inflater.inflate(R.layout.fragment_info, container,false);
		}
		
	//	if()
		
		return textView;
	}
	
	 public void onActivityCreated(Bundle savedInstanceState){
			
		    super.onCreate(savedInstanceState);
		   
		    if(SEC_NUMBER_INTEGER==1){
		    	
		    	
		    	GetPublicBookInfo getPublicBook=new GetPublicBookInfo();
		    	getPublicBook.execute((Void)null);
		    	
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
								intent.setClass(getActivity(),
										BookDetailActivity.class);
								intent.putExtra("ISBN", isbn);
								startActivity(intent);
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
		         
		         edittext_isbn=(EditText)getActivity().findViewById(R.id.edittext_isbn);
		         
		         button_searchByIsbn=(Button)getActivity().findViewById(R.id.button_searchByIsbn);
		         
		         button_searchByIsbn.setOnClickListener(new OnClickListener(){
		        	 
		        	 public void onClick(View view){
		        		 if(edittext_isbn.getText()!=null){
		        			 isbn=edittext_isbn.getText().toString();
		        		//	  LoadBookInfo();  
		        		//	  searchBookThread.start();  
		        			   searchBookTask=new SearchBookTask();
		        			   searchBookTask.execute((Void) null);
		        			
		        		 }
		        		 
		        	 }
		         });
		         
		         button_lend=(Button)getActivity().findViewById(R.id.button_lend);
		         setLend();
		         
		         
		     	webview_BookInfo = (WebView)getActivity().findViewById(R.id.webview_BookInfo);
		     	webview_BookInfo.getSettings().setSupportZoom(false);
		     	webview_BookInfo.getSettings().setJavaScriptCanOpenWindowsAutomatically(
						true);
		     	webview_BookInfo.getSettings().setJavaScriptEnabled(true);

		     


		    	
		    }
		    else if(SEC_NUMBER_INTEGER==3){
		    	
		    	text_nickname=(TextView)getActivity().findViewById(R.id.text_nickname);
		    	
		    	webview_MyBook=(WebView)getActivity().findViewById(R.id.webview_MyBook);
		    	
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
								intent.setClass(getActivity(),
										BookDetailActivity.class);
								intent.putExtra("ISBN", isbn);
								startActivity(intent);
							}
						});
					}
					
				

			
				}, "mybook");
		    
		    	
		    	
		    
		    	 
		    	Bundle bundle=getActivity().getIntent().getExtras();
		    	if(bundle!=null){
		    		nickname=bundle.getString("nickname");
		    		text_nickname.setText("Welcome!"+nickname);
		    	}
		    	
		    }
		   
		    
	 }
	 
	 
	 
	 public class GetPublicBookInfo extends AsyncTask<Void, Void, Boolean> {

			
			protected Boolean doInBackground(Void... arg0) {
				
				Boolean result=false;
				String httpUrl=LoginSingleton.SERVER_URL+"GetBookServlet";
				
				try{
					
					HttpPost httpRequest =new HttpPost(httpUrl);
					
					HttpClient httpclient=new DefaultHttpClient();
			
		
					HttpResponse httpResponse=httpclient.execute(httpRequest);
					
				
					if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
						
						

						
						String strResult=new String(EntityUtils.toString(httpResponse.getEntity()).getBytes("ISO-8859-1"),"UTF-8");
			
						Gson gson = new Gson();
						bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
					
						
						
						 
						  
					}
				}
				catch(Exception e){
					return false;
				}
				return result;
				
			}
			
			protected void onPostExeute(final Boolean success){
				LoadPublicBook();
			}
		}
		
	 
	 public class GetMyBookInfo  extends AsyncTask<Void, Void, Boolean> {

	
		protected Boolean doInBackground(Void... arg0) {
			
			
			Boolean result=false;
			String httpUrl=LoginSingleton.SERVER_URL+"GetBookServlet";
			
			if(LoginSingleton.isLoginSuccess()){
				
				HttpPost httpRequest =new HttpPost(httpUrl);
				List <NameValuePair> params = new ArrayList <NameValuePair>(); 
		        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
		
		        
				try{
			
					HttpClient httpclient=new DefaultHttpClient();
			
					httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 	
		
					HttpResponse httpResponse=httpclient.execute(httpRequest);
					
				
					if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
						
						

						
						String strResult=new String(EntityUtils.toString(httpResponse.getEntity()).getBytes("ISO-8859-1"),"UTF-8");
						System.out.println("GetMyBook:"+strResult);
						Gson gson = new Gson();
						bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
					
						
						
						 
						  
					}
				}
				catch(Exception e){
					return false;
				}
				return result;
			}
			
			return null;
			
		
		}
		
		protected void onPostExecute(final Boolean success) {
			
			 LoadMyBook();
		}
		
		 
		 
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
     	
	   }
		
	//}
     	
	
	 

	 
	 
		 private void setLend(){
				button_lend.setText("发布到图书馆");
				button_lend.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					//TODO 缺少判空操作，可能导致空指针崩溃，待修正,下同
						//	BookInfoDao.getInstance().create(bookInfo);
							
							postBookToLibrary();
						
							setHasLend();
						
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
			
		private void postBookToLibrary(){
			
			PostBookTask postBookTask=new PostBookTask();
			postBookTask.execute((Void)null);
			
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
					
					HttpPost httpRequest =new HttpPost(httpUrl);
					List <NameValuePair> params = new ArrayList <NameValuePair>(); 
			        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
			        params.add(new BasicNameValuePair("isbn",isbn));
			        
			        try{
						
						HttpClient httpclient=new DefaultHttpClient();
				
						httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 	
			
						HttpResponse httpResponse=httpclient.execute(httpRequest);
						
					
						if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
							return true;
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
		
		public class PostBookTask extends AsyncTask<Void, Void, Boolean> {

			@Override
			protected Boolean doInBackground(Void... arg0) {
			
				Boolean result=false;
				String httpUrl=LoginSingleton.SERVER_URL+"AddBookServlet";
				
				if(LoginSingleton.isLoginSuccess()){
					
					HttpPost httpRequest =new HttpPost(httpUrl);
					List <NameValuePair> params = new ArrayList <NameValuePair>(); 
			        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
			        Gson gsonBookInfo=new Gson();
					params.add(new BasicNameValuePair("bookbean",gsonBookInfo.toJson(bookBean)));
			  
					try{
				
						HttpClient httpclient=new DefaultHttpClient();
				
						httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 	
			
						HttpResponse httpResponse=httpclient.execute(httpRequest);
						
					
						if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
							
							

							
							String strResult=new String(EntityUtils.toString(httpResponse.getEntity()).getBytes("ISO-8859-1"),"UTF-8");
							System.out.println("客户端接收到的数据："+strResult);
				
							Gson gson = new Gson();
							bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
						
					
							
							 
							  
						}
					}
					catch(Exception e){
						return false;
					}
					return result;
				}
				
				return null;
			}
			
			protected void onPostExecute(final Boolean success) {
				
				 LoadMyBook();
			}
			
			
			
		}
		
		
		public void LoadPublicBook(){
			
			BookInfoDao.getInstance().deleteAll();
			
			for(BookBean book:bookList){
				
				BookInfoDao.getInstance().create(book);
			}
		}
		
		public void LoadMyBook(){
			
			//TODO 写入数据到本地缓存SQLITE并刷新
			MyBookDao.getInstance().deleteAll();
			
			for(BookBean book:bookList){
			
			//	isbn=book_isbn;
			//   searchBookTask=new SearchBookTask();
			//   searchBookTask.execute((Void) null);
			//	System.out.println(book.getBookname());
				
			 //  BookInfoDao.getInstance().create(book);
				MyBookDao.getInstance().create(book);
			}
			
			//TODO 需补充具体sql语句
	    //	String sql="select * from my_books";
	  //  	cursor=MyBookDao.getInstance().query(sql, null);
	    	
	   // 	gridAdapter=new GridAdapter(getActivity(), R.layout.gridview_mybook, cursor, 
		//			new String[]{"bookname"}, new int[]{R.id.book_name}, "_id", R.id.book_cover);
			
	    	
	    	
	    
			
	    	
	 

	    
	     	
		   
			
			
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
		 
		 
		 private BookBean getResultByIsbn(){
				//	BookInfo dbook=null;
					
	
					
			//		checkNetworkInfo();
					
		
				
					
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
		 
		 
		 
		 public BookBean getBookInfo(InputStream inputStream){
			 
		//	 Long t1=System.currentTimeMillis();
			
			 String str="";
			 
			 JSONObject json;

			 byte[] b = new byte[1024];  
			
			 try {
				 int i = 0;  
				while ((i = inputStream.read(b)) != -1) {  
					    
					 str+=new String(b);  
					    
					 b = new byte[1024];   
				}
			
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
			 
		//	 System.out.println(System.currentTimeMillis()-t1);
			 
			 return bookBean;
			 
		 }
		 
		 /*
			
			public BookBean getBookInfo(InputStream inputStream) {
				
			//	BookInfo bookInfo1 = new BookInfo();
				
				Long t1=System.currentTimeMillis();
			
				
				try{
				
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(inputStream, "UTF-8");
				
				
				
				bookBean=new BookBean();
				bookBean.setIsbn(isbn);
				
			
				
				
					for (int i = parser.getEventType(); i != XmlPullParser.END_DOCUMENT; i = parser.next()) {
					
					if (i == XmlPullParser.START_TAG
							&& parser.getName().equals("attribute")
							&& parser.getAttributeValue(0).equals("title")) {
						bookBean.setBookname(parser.nextText());
					//	Log.v("SearchBook", "title>>" + bookInfo.getName());
						continue;
					}
					if (i == XmlPullParser.START_TAG
							&& parser.getName().equals("attribute")
							&& parser.getAttributeValue(0).equals("author")) {
						bookBean.setAuthor(parser.nextText());
					//	Log.v("SearchBook", "author>>" + bookInfo.getAuthor());
						continue;
					}
					if (i == XmlPullParser.START_TAG
							&& parser.getName().equals("attribute")
							&& parser.getAttributeValue(0).equals("publisher")) {
						bookBean.setPublisher(parser.nextText());
						Log.v("SearchBook", "author>>" + bookBean.getPublisher());
						continue;
					}
					if (i == XmlPullParser.START_TAG
							&& parser.getName().equals("attribute")
							&& parser.getAttributeValue(0).equals("price")) {
						bookBean.setPrice(parser.nextText());
						Log.v("SearchBook", "author>>" + bookBean.getPrice());
						continue;
					}
					if (i == XmlPullParser.START_TAG && parser.getName().equals("link")) {
						if (parser.getAttributeValue(1).equals("image")) {
							bookBean.setBookcover_url(parser.getAttributeValue(0));
						//	Log.v("SearchBook", "image>>" + bookInfo.getImageUrl());
						}
						continue;
					}
					if (i == XmlPullParser.START_TAG
							&& parser.getName().equals("summary")) {
						bookBean.setSummary(parser.nextText());
						//Log.v("SearchBook", "summary>>" + bookInfo.getSummary());
						continue;
					}
					
				}
				Log.v("SearchBook", ">>>>> parse end.");
				
			//	if(bookInfo==null)
				//	System.out.println("5678");
				
				
			//	notify();
				 
			
				
				}catch(Exception e){
					e.printStackTrace();
				}
				
				System.out.println(System.currentTimeMillis()-t1);

				return bookBean;
			}*/
			
			/*


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
	 
	*/
			
			
	public class SearchBookTask extends AsyncTask<Void, Void, Boolean> {
				
				
		protected Boolean doInBackground(Void... params) {
		
			 bookBean=  getResultByIsbn();
			 
			 
				Boolean result=false;
				String httpUrl=LoginSingleton.SERVER_URL+"CheckBookServlet";
				
				if(LoginSingleton.isLoginSuccess()){
					
					HttpPost httpRequest =new HttpPost(httpUrl);
					List <NameValuePair> params1 = new ArrayList <NameValuePair>(); 
			        params1.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
			        params1.add(new BasicNameValuePair("isbn",isbn));
			  
					try{
				
						HttpClient httpclient=new DefaultHttpClient();
				
						httpRequest.setEntity(new UrlEncodedFormEntity(params1, HTTP.UTF_8)); 	
			
						HttpResponse httpResponse=httpclient.execute(httpRequest);
						
					
						if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
							
							

							String strResult=EntityUtils.toString(httpResponse.getEntity());
								System.out.println(strResult);
								JSONObject jsonObject = new JSONObject(strResult) ;
								Boolean actionResult=jsonObject.getBoolean("ActionResult");
							
								if(actionResult){
									 setHasLend();
								}else{
									setLend();
								}
					
							
							 
							  
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
	 
	 public void onResume() {  
		  
	        super.onResume();  
	        if(SEC_NUMBER_INTEGER==1){
	        	webview_BookForBorrow.reload();
	        }
	    }  
	 
}
