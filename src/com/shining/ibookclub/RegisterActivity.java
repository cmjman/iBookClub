package com.shining.ibookclub;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

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

public class RegisterActivity extends Activity {
	
	private Button button_submit;
	private EditText text_username;
	private EditText text_nickname;
	private EditText text_password;
	private EditText text_confirm_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		text_username=(EditText)findViewById(R.id.text_username);
		text_nickname=(EditText)findViewById(R.id.text_nickname);
		text_password=(EditText)findViewById(R.id.text_password);
		text_confirm_password=(EditText)findViewById(R.id.text_confirm_password);
		
		button_submit=(Button)findViewById(R.id.button_submit);
		button_submit.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
				if(checkPassword()){
					
					String username=text_username.getText().toString();
					String nickname=text_nickname.getText().toString();
					String password=text_password.getText().toString();
					
					UserRegisterTask registerTask=new UserRegisterTask();
					registerTask.execute(username,nickname,password);
				}else{
					Toast.makeText(getBaseContext(), "两次输入的密码不一致，请修改后重新提交!", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
	}
	
	private boolean checkPassword(){
		if(text_password.getText().toString().equals(text_confirm_password.getText().toString())
				&&(!text_password.getText().toString().isEmpty()))
			return true;
		return false;
	}
	
	private class UserRegisterTask extends AsyncTask<String, Void, Boolean> {

		
		protected Boolean doInBackground(String... str) {
			
			String httpUrl=LoginSingleton.SERVER_URL+"RegisterServlet";
			Boolean actionResult=false;
			
			List <NameValuePair> params = new ArrayList <NameValuePair>(); 
	        params.add(new BasicNameValuePair("email", str[0])); 
	        params.add(new BasicNameValuePair("nickname",str[1]));
	        params.add(new BasicNameValuePair("password", str[2])); 
	      
			try{
				HttpUtility httpUtility=new HttpUtility(httpUrl,params);
				String strResult=httpUtility.doPost();
				
				System.out.println("RegisterActivity:"+strResult);
	                 
	            JSONObject jsonObject = new JSONObject(strResult) ;
	         
	            actionResult=jsonObject.getBoolean("ActionResult");
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return actionResult;
		}
		
		protected void onPostExecute(Boolean result){
			
			if(result){
				Toast.makeText(getBaseContext(), "注册成功", Toast.LENGTH_SHORT).show();
				RegisterActivity.this.finish();
			}else{
				Toast.makeText(getBaseContext(), "注册失败", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
