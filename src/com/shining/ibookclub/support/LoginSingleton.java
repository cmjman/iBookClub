package com.shining.ibookclub.support;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Application;

public class LoginSingleton {
	

	
    private static LoginSingleton loginInstance = null;  
    
  //  public static final String SERVER_URL ="http://192.168.126.50:8003/iBookClubServer/"; 
    
    public static final String SERVER_URL ="http://1.ibookclubserver.sinaapp.com/"; 
      
    
    public static HttpClient httpClient = null;  
   
    public static String loginEmail = null;  
    private static String loginPassword = null;  
   
	public static String nickname;
    private static Boolean actionResult=false;
    


  
    private LoginSingleton(String loginEmail, String loginPassword) throws Exception{  
        this.loginEmail = loginEmail;  
        this.loginPassword = loginPassword;  
        
        Login();
     
    }  
    public static LoginSingleton getInstance(String loginUsername, String loginPassword) throws Exception{  
        if(loginInstance == null){  
            loginInstance = new LoginSingleton(loginUsername, loginPassword);  
        }  
        return loginInstance;  
    }  
    
    public static Boolean isLoginSuccess(){
    	
    	return actionResult;
    }
    
private static Boolean Login(){
		
		String httpUrl=SERVER_URL+"LoginServlet";
		
		List <NameValuePair> params = new ArrayList <NameValuePair>(); 
        params.add(new BasicNameValuePair("email", loginEmail)); 
        params.add(new BasicNameValuePair("password", loginPassword)); 
        
		try{
			
			HttpUtility httpUtility=new HttpUtility(httpUrl,params);

			String strResult=httpUtility.doPost();
			System.out.println("LoginSingleton:"+strResult);
			JSONObject jsonObject = new JSONObject(strResult) ;
			actionResult=jsonObject.getBoolean("ActionResult");
			nickname=jsonObject.getString("nickname");	
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return actionResult;
	}

public static void logout(){  
    loginInstance = null;  
}  

	

}
