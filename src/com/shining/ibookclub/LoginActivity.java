package com.shining.ibookclub;

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
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.shining.ibookclub.fragment.DummySectionFragment;
import com.shining.ibookclub.support.LoginSingleton;




import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	


	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	
	//private AutoCompleteTextView mUserNameAuto;  
//	private EditText mPasswordEt;  
    private CheckBox mRemPwd;  
    private CheckBox mShowPwd;  
    private CheckBox mAutoLogin;
    private SharedPreferences mPasswordSp;  
    private SharedPreferences mAutoLoginSp;

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		
		mAutoLoginSp = this.getSharedPreferences("userInfo", MODE_PRIVATE); 
		
		mRemPwd=(CheckBox)findViewById(R.id.checkBox_remPwd);
		mShowPwd=(CheckBox)findViewById(R.id.checkBox_showPwd);
		mAutoLogin=(CheckBox)findViewById(R.id.checkBox_autoLogin);
	
		
		mAutoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener(){

	
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(mAutoLogin.isChecked()){
					mAutoLoginSp.edit().putBoolean("AUTO_ISCHECK", true).commit();
				}else{
					mAutoLoginSp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
				
			}
			
			
		});
		
	//	mAutoLoginSp.edit().putBoolean("AUTO_ISCHECK", false).commit();
		
		mShowPwd.setOnClickListener(new OnClickListener() {//显示密码事件操作  
            /*  
             * 明文显示密码 ：  
             * 明文显示：android.text.method.HideReturnsTransformationMethod ；  
             * 密文显示：android.text.method.PasswordTransformationMethod ；  
             */ 
            @Override 
            public void onClick(View v) {  
               
                if (mShowPwd.isChecked()) {// 被选中，则显示明文  
                    // 将文本框的内容设置成明文显示  
                    mPasswordView.setTransformationMethod(HideReturnsTransformationMethod  
                            .getInstance());  
                } else {  
                    // 将文本框内容设置成密文的方式显示  
                    mPasswordView.setTransformationMethod(PasswordTransformationMethod  
                            .getInstance());  
                }  
            }  
        });  
		
	
    
//	    mUserNameAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);  

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		mEmailView.setText(mEmail);
		
		savePassword();
		
		
		

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		
		
		

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		findViewById(R.id.register_button).setOnClickListener(	
				new View.OnClickListener() {
					
					public void onClick(View view) {
						Intent intent=new Intent(getBaseContext(),RegisterActivity.class);
						
						startActivity(intent);
					}
				});
		
		if(mAutoLoginSp.getBoolean("AUTO_ISCHECK", true)){
			
			mEmailView.setText(mAutoLoginSp.getString("email",""));
			mPasswordView.setText(mAutoLoginSp.getString("password", ""));
		//	System.out.println(mEmail+mPassword);
		//	mAuthTask = new UserLoginTask();
		//	mAuthTask.execute((Void) null);
			attemptLogin();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if(mEmail.length()==11 && mEmail.startsWith("1")||mEmail.contains("@")){
			
			
		}
		else{
			mEmailView.setError(getString(R.string.error_invalid_username));
			focusView = mEmailView;
			cancel = true;
		}
		
		if (mRemPwd.isChecked()) {  //选择记住密码功能
            mPasswordSp.edit().putString(mEmail, mPassword).commit();//记住密码，把密码信息放入SharedPreferences文件中  
        }  
		
		if(mAutoLogin.isChecked()){
    	
    			mAutoLoginSp
    		    .edit()
    		    .putString("email", mEmail)
    		    .putString("password", mPassword)
    		    .putBoolean("AUTO_ISCHECK", true)
    		    .commit();
    		}
		
	

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			checkNetworkInfo();
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private void savePassword() {
 
        mPasswordSp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);  
        mRemPwd.setChecked(true);// 默认为记住密码  
        mEmailView.setThreshold(1);// 输入1个字母就开始自动提示  
        // 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0x81  
        // 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0x91  
  //      mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT  
     //           | InputType.TYPE_TEXT_VARIATION_PASSWORD);  
        mEmailView.addTextChangedListener(new TextWatcher() {  
 
            @Override 
            public void onTextChanged(CharSequence s, int start, int before,  
                    int count) {  
                // TODO Auto-generated method stub  
                String[] allUserName = new String[mPasswordSp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对  
                allUserName = mPasswordSp.getAll().keySet().toArray(new String[0]);  
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(  
                        LoginActivity.this,  
                        android.R.layout.simple_dropdown_item_1line,  
                        allUserName);  
                mEmailView.setAdapter(adapter);// 设置数据适配器  
            }  
 
            @Override 
            public void beforeTextChanged(CharSequence s, int start, int count,  
                    int after) {  
                
 
            }  
 
            @Override 
            public void afterTextChanged(Editable s) {  
          
                // 自动输入密码  
            	mPasswordView.setText(mPasswordSp.getString(mEmailView.getText().toString(),  
                        ""));  
 
            }  
        });  
        
    	
    		
    }  
	
	private void checkNetworkInfo(){
		  
		ConnectivityManager conMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile=null;
		if(conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!=null)
			mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
	 
		if(mobile==State.CONNECTED||mobile==State.CONNECTING) return;
		if(wifi==State.CONNECTED||wifi==State.CONNECTING) return;
		
		Toast.makeText(getBaseContext(), "无可用网络，请打开网络连接！", Toast.LENGTH_SHORT).show();  
		
		startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
	 
	  }


	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
		

		//	Weibo weibo = new Weibo("XXX@sina.com","XXX");
		//	weibo.setHttpConnectionTimeout(5000);
			
			
			
			try {
				LoginSingleton.getInstance(mEmail, mPassword);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
			if(LoginSingleton.isLoginSuccess()){
				
				 Bundle bundle = new Bundle();
				  bundle.putString("nickname",LoginSingleton.nickname);
				   Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				   intent.putExtras(bundle);
				//   setResult(0,intent);
				   finish();
				   startActivity(intent);
				   return true;
			}
			//
			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
			}
			
			
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
		//		Toast.makeText(LoginActivity.this, "密码错误!", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
