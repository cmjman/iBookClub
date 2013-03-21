package com.shining.ibookclub;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.support.FinalConstants;
import com.shining.ibookclub.support.LazyAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BuyBookActivity extends Activity {
	
	private String keyword;
	
	private ArrayList<BookBean> list=new ArrayList<BookBean>();
	
	private BookBean bookBean;
	
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_book);
		
		keyword=getIntent().getStringExtra("keyword");
		
		
		listView=(ListView)findViewById(R.id.listView_buybook);
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String url=FinalConstants.Douban_BOOK_URL+list.get(position).getIsbn();
				
				Intent intent=new Intent(getBaseContext(),BuyBookDetailActivity.class);
				intent.putExtra("url", url);
				startActivity(intent);
			}	
		});
		
		SearchBookTask searchBookTask=new SearchBookTask();
		searchBookTask.execute(keyword);
	}
	
	class SearchBookTask extends AsyncTask<String,Void,Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			
			Boolean result=false;
			
		
			try{	
				
				URL url = new URL(FinalConstants.Douban_SEARCH_URL+params[0]+"&apikey="+FinalConstants.Douban_API_KEY);      
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
				conn.setConnectTimeout(5 * 1000);      
				conn.setRequestMethod("GET");      
		
				
				
				InputStream inStream = conn.getInputStream(); 
				
				 String returnStr="";
				 
				
				 
				 JSONObject json;

				 BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
				    
				 StringBuilder sb = new StringBuilder();
				    
				 String line = "";
				    
				 while ((line = in.readLine()) != null){
				      
					 sb.append(line);
					 returnStr=sb.toString();
				 }
				 
				if(returnStr!=null && returnStr!="" && returnStr.startsWith("{")){
					

					json=new JSONObject(returnStr);
		
					 JSONArray jsonArray=json.getJSONArray("books");
					 
		
					 
					 for(int i=0;i<jsonArray.length();i++){

						JSONObject json_temp= jsonArray.getJSONObject(i);
						 
						 bookBean=new BookBean();
						 bookBean.setAuthor(json_temp.getString("author"));
						 bookBean.setBookcover_url(json_temp.getString("image"));
						 bookBean.setBookname(json_temp.getString("title"));
						 bookBean.setIsbn(json_temp.getString("isbn13"));
						 bookBean.setPrice(json_temp.getString("price"));
						 bookBean.setPublisher(json_temp.getString("publisher"));
						 bookBean.setSummary(json_temp.getString("summary"));
						 
						 list.add(bookBean);
					 }
					 result=true;
					
				}
				 
				 
				 
					
				
				 
				
		
				}catch (Exception e) {  
				e.printStackTrace();  
				}  
			
			
			return result;
		}
		
		protected void onPostExecute(final Boolean success) {
			
			if(success){
				
				LazyAdapter adapter=new LazyAdapter(BuyBookActivity.this, list);
				
				listView.setAdapter(adapter);
				
			}
				 
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy_book, menu);
		return true;
	}

}
