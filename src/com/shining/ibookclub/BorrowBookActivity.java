package com.shining.ibookclub;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.fragment.DummySectionFragment.SearchBookTask;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.LoginSingleton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BorrowBookActivity extends Activity {
	
	private String isbn;
	
	private ListView listOwner;
	
	private Hashtable<Integer,String> ownerTable;
	
	private  ArrayList<Hashtable<Integer, String>> listItem;
	
	
	private ArrayAdapter<String> listItemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_book);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		isbn=getIntent().getStringExtra("isbn");
		
		System.out.println("isbn:"+isbn+"传递成功");
		
		new BorrowBookTask().execute((Void) null);
		
		listOwner=(ListView)findViewById(R.id.list_owner);
		
		listItem= new ArrayList<Hashtable<Integer, String>>();  
		
	
		
		
		
	
		
	}
	
	public class BorrowBookTask extends AsyncTask<Void, Void, Boolean> {
		
	
		String[] owner_nickname={
				 "123",
				 "321"
		 };
		
		protected Boolean doInBackground(Void... arg0) {
			
			Boolean result=false;
			String httpUrl=LoginSingleton.SERVER_URL+"BorrowBookServlet";
			
			if(LoginSingleton.isLoginSuccess()){
				
				HttpPost httpRequest =new HttpPost(httpUrl);
				List <NameValuePair> params = new ArrayList <NameValuePair>(); 
		        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
		        params.add(new BasicNameValuePair("isbn",isbn));
		  
				try{
					
					HttpUtility httpUtility=new HttpUtility(httpUrl,params);
			
					String strResult=httpUtility.doPost();
					
					System.out.println("BorrowBookTask:"+strResult);
					
					Gson gson = new Gson();
					
					ownerTable = gson.fromJson(strResult, new TypeToken<Hashtable<Integer,String>>(){}.getType());
					Iterator it=ownerTable.keySet().iterator();
					
					
					for(int i=0;it.hasNext();i++){
						
						
						
				//		owner_nickname[i]=new String();
						
					
				//		owner_nickname[i]=ownerTable.get(it.next());
					
						
						
					}
					
			
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
			return true;
			
		}
		
		 protected void onPostExeute(final Boolean success){
				
			
			// owner_nickname;
			
			 
			 listItemAdapter = new ArrayAdapter<String>(BorrowBookActivity.this,android.R.layout.simple_list_item_1,owner_nickname);
			 listOwner.setAdapter(listItemAdapter);
		 }
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_borrow_book, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		  
		 super.onOptionsItemSelected(item);
		 
		 switch(item.getItemId())
	     {
	        case android.R.id.home:
	     //   	startActivity(new Intent(BorrowBookActivity.this,BookDetailActivity.class));
	        	//TODO new activity会导致空ISBN
	        	break;
	     }
		 
		 return true;
	}
}
