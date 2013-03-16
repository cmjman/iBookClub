package com.shining.ibookclub;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.LoginSingleton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
	
	public class RecordBorrowTask extends  AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... nickname) {
			
			Boolean actionResult=false;
			
			String httpUrl=LoginSingleton.SERVER_URL+"recordBook.action";
			
			if(LoginSingleton.isLoginSuccess()){
				
				HttpPost httpRequest =new HttpPost(httpUrl);
				List <NameValuePair> params = new ArrayList <NameValuePair>(); 
		        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
		        params.add(new BasicNameValuePair("isbn", isbn)); 
		        params.add(new BasicNameValuePair("nickname",nickname[0]));
		  
				try{
					
					HttpUtility httpUtility=new HttpUtility(httpUrl,params);
			
					String strResult=httpUtility.doPost();
				
		            JSONObject jsonObject = new JSONObject(strResult) ;
		         
		            actionResult=jsonObject.getBoolean("ActionResult");
				
					
			
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		
			
			
			return actionResult;
		}
		
		 protected void onPostExecute(final Boolean success){
			 
			 if(success){
				 Toast.makeText(getBaseContext(), "借阅成功！", Toast.LENGTH_SHORT).show();
				 BorrowBookActivity.this.finish();
			 }else{
				 Toast.makeText(getBaseContext(), "借阅失败！", Toast.LENGTH_SHORT).show();
			 }
			 
		 }
	}
	
	public class BorrowBookTask extends AsyncTask<Void, Void, Boolean> {
		
		private String[] owner_nickname=new String[10];

		protected Boolean doInBackground(Void... arg0) {
			
			Boolean result=false;
			String httpUrl=LoginSingleton.SERVER_URL+"borrowBook.action";
			
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
			
						owner_nickname[i]=new String();
						
					
						owner_nickname[i]=ownerTable.get(it.next());
					
						System.out.println(owner_nickname[i]);
						
					}
					
			
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
			return true;
			
		}
		
		 protected void onPostExecute(final Boolean success){
				
			
			if(success){
			 
			 listItemAdapter = new ArrayAdapter<String>(BorrowBookActivity.this,android.R.layout.simple_list_item_1,owner_nickname);
			 listOwner.setAdapter(listItemAdapter);
			 listOwner.setOnItemClickListener(new OnItemClickListener() {
		            
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
						
					new AlertDialog.Builder(getBaseContext())
						.setIcon(R.drawable.ic_launcher)
						.setTitle("确定借阅")
						.setPositiveButton("确定", new OnClickListener(){

						
							public void onClick(DialogInterface arg0, int arg1) {
								
								RecordBorrowTask recordTask=new RecordBorrowTask();
								
								recordTask.execute(owner_nickname[position]);
								
							}
							
						})
						.setNegativeButton("取消	", new OnClickListener(){

						
							public void onClick(DialogInterface arg0, int arg1) {
								
								
							}
							
						}).create();
				}
				
		      });
			 
			}
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
