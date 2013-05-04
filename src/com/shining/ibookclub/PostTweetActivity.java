package com.shining.ibookclub;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PostTweetActivity extends Activity {
	
	private Button button_post_tweet;
	
	private EditText edittext_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_tweet);
		
		edittext_content=(EditText)findViewById(R.id.edittext_content);
		
		button_post_tweet=(Button)findViewById(R.id.button_post_tweet);
		
		button_post_tweet.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			
				PostTweetTask postTweetTask=new PostTweetTask();
				postTweetTask.execute();
				
				Toast.makeText(getBaseContext(), "发布成功！", Toast.LENGTH_SHORT).show();
				PostTweetActivity.this.finish();
			}	
		});
	}
	
	private class PostTweetTask extends AsyncTask<Void,Void,Boolean>{

		@Override
		protected Boolean doInBackground(Void... p) {
		
			String httpUrl=iBookClub.SERVER_URL+"postTweet.action";
			Boolean result=false;
			
			if(LoginSingleton.isLoginSuccess()){
				
			
				List <NameValuePair> params = new ArrayList <NameValuePair>(); 
		        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
		        params.add(new BasicNameValuePair("message",edittext_content.getText().toString()));
				try{
			
					HttpUtility httpUtility=new HttpUtility(httpUrl,params);
					
					String strResult=httpUtility.doPost();
					System.out.println("PostTweet:"+strResult);
					
					JSONObject jsonObject = new JSONObject(strResult) ;
					result=jsonObject.getBoolean("ActionResult");
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
			
			
			
			}
			return result;
		}
			
		protected void onPostExecute(final Boolean success) {
				
			if(success){
			
				Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
			
				PostTweetActivity.this.finish();
			}else{
				Toast.makeText(getApplicationContext(), "发布失败！", Toast.LENGTH_SHORT).show();
			}
		
				
	}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_tweet, menu);
		return true;
	}

}
