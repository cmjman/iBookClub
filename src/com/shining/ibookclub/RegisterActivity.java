package com.shining.ibookclub;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.shining.ibookclub.support.Encryption;
import com.shining.ibookclub.support.FinalConstants;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.LoginSingleton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	private Button button_submit;
	private EditText text_username;
	private EditText text_nickname;
	private EditText text_password;
	private EditText text_confirm_password;
	private EditText text_age;
	
	final private static String SALT="shinlock";
	
	private String gender;
	
	private String age;
	
	private RadioGroup radioGroup;

	private Spinner spinner_interest;
	
	private ArrayAdapter<String> adapter_interest;  
	
	private String[] str_interest={
			"文学","流行","文化","生活","经管","科技"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		text_username=(EditText)findViewById(R.id.text_username);
		text_nickname=(EditText)findViewById(R.id.text_nickname);
		text_password=(EditText)findViewById(R.id.text_password);
		text_confirm_password=(EditText)findViewById(R.id.text_confirm_password);
		text_age=(EditText)findViewById(R.id.text_age);
		
		button_submit=(Button)findViewById(R.id.button_submit);
		button_submit.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
				String username=text_username.getText().toString();
				String nickname=text_nickname.getText().toString();
				String password=text_password.getText().toString();
				String interest=spinner_interest.getSelectedItem().toString();
				String age=text_age.getText().toString();
		
				int r=checkPassword();
				
				if(r==0){

					UserRegisterTask registerTask=new UserRegisterTask();
					registerTask.execute(username,nickname,password,interest,gender,age);
					
				}else if (r==2){
					Toast.makeText(getBaseContext(), "两次输入的密码不一致，请修改后重新提交!", Toast.LENGTH_SHORT).show();
				}else if (r==1){
					Toast.makeText(getBaseContext(), "请将表单填写完整！", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
		
		spinner_interest=(Spinner)findViewById(R.id.spinner_interest);
		adapter_interest=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,str_interest); 
		spinner_interest.setAdapter(adapter_interest);
		
		radioGroup=(RadioGroup)findViewById(R.id.radio_sex);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				System.out.println("checkedId:"+checkedId);
				
				if(checkedId==R.id.male)
					gender="male";
				else
					gender="female";
				
				System.out.println(gender);
			}
			
		});
	}
	
	private int checkPassword(){
		
		int result;
		
		if(text_password.getText().toString().equals(text_confirm_password.getText().toString())
				&&(!text_password.getText().toString().isEmpty()))
			result=0;
		else if(text_username.getText().toString().isEmpty()||text_nickname.getText().toString().isEmpty())
			result=1;
		else
			result=2;
		
		return result;
	}
	
	private class UserRegisterTask extends AsyncTask<String, Void, Boolean> {

		
		protected Boolean doInBackground(String... str) {
			
			String httpUrl=FinalConstants.SERVER_URL+"register.action";
			Boolean actionResult=false;
			
			Encryption encryption=new Encryption(str[2]+SALT);
			String password_encrypted=encryption.encrypt();
			
			
			List <NameValuePair> params = new ArrayList <NameValuePair>(); 
	        params.add(new BasicNameValuePair("email", str[0])); 
	        params.add(new BasicNameValuePair("nickname",str[1]));
	        params.add(new BasicNameValuePair("password", password_encrypted)); 
	        params.add(new BasicNameValuePair("interest",str[3]));
	        params.add(new BasicNameValuePair("gender",str[4]));
	        params.add(new BasicNameValuePair("age",str[5]));
	        
	      
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
