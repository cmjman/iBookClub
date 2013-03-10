package com.shining.ibookclub.fragment;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;




import org.apache.http.NameValuePair;

import org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shining.ibookclub.*;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.dao.BookInfoDao;
import com.shining.ibookclub.dao.MyBookDao;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.KeywordsFlow;
import com.shining.ibookclub.support.LoginSingleton;


import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
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
	
	private SearchView searchView;
	
	private KeywordsFlow keywordsFlow; 
	
	
	private Handler handler = new Handler();
	
	private Button button_scan;
	
	private Button button_searchByIsbn;
	
	private Button button_buyBook;
	
	private Button button_editInfo;
	
	private ImageButton button_findNearbyBook;
	
	private Button button_lend;
	
	private EditText edittext_isbn;
	
	
	private String isbn;
	
	private String keyword; 
	
	 public static final String[] keywords = { 
		 "谁是谷歌想要的人才","看见","Java并发编程实战",
		 "观念的水位","打造Facebook","知日·妖怪","白夜行",
		 "逃离德黑兰","百年孤独","全世界人民都知道","青春",
		 "我所理解的生活","浪潮之巅","黑客与画家","编程珠玑",
		 "平凡的世界","追风筝的人","你好，旧时光","活着",
		 "不能承受的生命之轻","云图","1Q84","动物农场"
	 };  
	
	
	 private Button btnIn, btnOut; 
	
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
		textView.setTypeface(iBookClub.typeFace);
	
		if(SEC_NUMBER_INTEGER==1){
			
			
			return inflater.inflate(R.layout.fragment_borrow, container, false);
		}
		else if(SEC_NUMBER_INTEGER==2){
			return inflater.inflate(R.layout.fragment_lend, container,false);
		}
		else if(SEC_NUMBER_INTEGER==3){
			return inflater.inflate(R.layout.fragment_info, container,false);
		}
		
		return textView;
	}
	
	 public void onActivityCreated(Bundle savedInstanceState){
			
		    super.onCreate(savedInstanceState);
		   
		    if(SEC_NUMBER_INTEGER==1){
		    	
		    	
		    //	GetPublicBookInfo getPublicBook=new GetPublicBookInfo();
		    //	getPublicBook.execute((Void)null);
		    	
		    	searchView=(SearchView)getActivity().findViewById(R.id.searchView);
		    	
		    	searchView.setOnQueryTextListener(new OnQueryTextListener(){

					@Override
					public boolean onQueryTextChange(String arg0) {
						
						return false;
					}

					@Override
					public boolean onQueryTextSubmit(String arg0) {
						
						keyword=searchView.getQuery().toString();
						
						SearchPublicBookTask search=new SearchPublicBookTask();
						search.execute((Void) null);
						return true;
					}
		    	});
		    	
		      	button_findNearbyBook=(ImageButton)getActivity().findViewById(R.id.button_findNearbyBook);
		    	
		    	button_findNearbyBook.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
					
						Intent intent=new Intent(getActivity(),FindNearbyBookActivity.class);
						startActivity(intent);
					}
		    	});
		    	
		    	
		    	
		    	  keywordsFlow = (KeywordsFlow)getActivity().findViewById(R.id.keyWordsFlow);  
		    	  keywordsFlow.setDuration(800l);  
		    
		    	   keywordsFlow.rubKeywords(); 
	    		   feedKeywordsFlow(keywordsFlow, keywords);  
		    	  
		    	   if(Math.random()>0.5){
		    		   keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
		    	   }
		    	   else{
		    		   keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT); 
		    	   }
		    	
		    	
		    	webview_BookForBorrow=(WebView)getActivity().findViewById(R.id.webview_BookForBorrow);
		    	
		    	webview_BookForBorrow.setVisibility(View.GONE);
		    	
		    	
		    	button_buyBook=(Button)getActivity().findViewById(R.id.button_buybook);
		    	
		    	button_buyBook.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						
						Intent intent=new Intent(getActivity(),BuyBookActivity.class);
						startActivity(intent);
						
					}
		    	});
		    	
		    	button_buyBook.setTypeface(iBookClub.typeFace);
		    	
		
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
		        		 
		        		 //条形码扫描入口，Intent指向ZXING解析核心库，点击扫描按钮后，可跳转到摄像头解析界面
		        		 Intent intent = new Intent("com.shining.iBookClub.library.com.google.zxing.client.android.SCAN");
		        	     intent.putExtra("SCAN_MODE", "ONE_D_MODE");
		        	     startActivityForResult(intent, 0);
		        	 }
		         });
		         button_scan.setTypeface(iBookClub.typeFace);
		         
		         edittext_isbn=(EditText)getActivity().findViewById(R.id.edittext_isbn);
		         edittext_isbn.setTypeface(iBookClub.typeFace);
		         
		         button_searchByIsbn=(Button)getActivity().findViewById(R.id.button_searchByIsbn);
		         
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
		         
		         button_lend=(Button)getActivity().findViewById(R.id.button_lend);
		         button_lend.setTypeface(iBookClub.typeFace);
		         setLend();
		         
		         
		     	webview_BookInfo = (WebView)getActivity().findViewById(R.id.webview_BookInfo);
		     	webview_BookInfo.getSettings().setSupportZoom(false);
		     	webview_BookInfo.getSettings().setJavaScriptCanOpenWindowsAutomatically(
						true);
		     	webview_BookInfo.getSettings().setJavaScriptEnabled(true);

		     


		    	
		    }
		    else if(SEC_NUMBER_INTEGER==3){
		    	
		    	text_nickname=(TextView)getActivity().findViewById(R.id.text_nickname);
		    	text_nickname.setTypeface(iBookClub.typeFace);
		    	
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
		    		text_nickname.setText(nickname);
		    	}
		    	
		    	button_editInfo=(Button)getActivity().findViewById(R.id.button_editInfo);
		    	button_editInfo.setTypeface(iBookClub.typeFace);
		    	
		    }
		   
	     
	 
	}
	 
	 private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {  
		    Random random = new Random();  
		    for (int i = 0; i < KeywordsFlow.MAX; i++) {  
		        int ran = random.nextInt(arr.length);  
		        String tmp = arr[ran];  
		        keywordsFlow.feedKeyword(tmp);  
		    }  
		}  

	 

	public class SearchPublicBookTask extends AsyncTask<Void,Void,Boolean>{
	 
	 protected Boolean doInBackground(Void... arg0){
		 
		 Boolean result=false;
		 String httpUrl=LoginSingleton.SERVER_URL+"SearchBookServlet";
		 
		 if(LoginSingleton.isLoginSuccess()){
		 
			 List <NameValuePair> params = new ArrayList <NameValuePair>(); 
			 params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail)); 
			 params.add(new BasicNameValuePair("keyword",keyword));
			
			 try{
				
				HttpUtility httpUtility=new HttpUtility(httpUrl,params);
				String strResult=httpUtility.doPost();
						
				System.out.println("SearchPublicBook:"+strResult);
				Gson gson = new Gson();
				bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
				result=true;
			 }
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return result;
	}
		 
	 
	 
	 protected void onPostExecute(final Boolean success){
	
		BookInfoDao.getInstance().deleteAll();
		
		
			
			for(BookBean book:bookList){
					
				BookInfoDao.getInstance().create(book);
			}
			
		    LoadSearchResult();
		    
		    webview_BookForBorrow.reload();
	 
		
	}
}
	private void LoadSearchResult(){
		
		webview_BookForBorrow.setVisibility(View.VISIBLE);
		
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
    	
    	getActivity().findViewById(R.id.keyWordsFlow).setVisibility(View.GONE);
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

	 //处理解析得到的返回值，即相应的ISBN编号
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
     	
     	getActivity().findViewById(R.id.image).setVisibility(View.GONE);
     	
	   }
		
		 private void setLend(){
				button_lend.setText("发布到图书馆");
				button_lend.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
				
						
						if(bookBean.getIsbn()!=null){
						
							Intent intent=new Intent(getActivity(),PostBookActivity.class);
					
							intent.putExtra("bean", bookBean);
						
							startActivity(intent);
						}else
							Toast.makeText(getActivity(), "请先扫描或者搜索需要发布的图书！", Toast.LENGTH_SHORT).show();
						
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
	 
	 public void onResume() {  
		  
	        super.onResume();  
	        if(SEC_NUMBER_INTEGER==1){
	        	webview_BookForBorrow.reload();
	        }else if(SEC_NUMBER_INTEGER==2){
	        	
	        }else if(SEC_NUMBER_INTEGER==3){
	        	
	        	Bundle bundle=getActivity().getIntent().getExtras();
		    	if(bundle!=null){
		    		nickname=bundle.getString("nickname");
		    		text_nickname.setText(nickname);
		    	}
	        }
	    }  
	 

	 
}
