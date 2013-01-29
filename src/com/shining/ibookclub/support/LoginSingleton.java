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

public class LoginSingleton {
	
	public static final String SERVER_URL ="http://192.168.1.103:8003/iBookClubServer/"; 
	
    private static LoginSingleton loginInstance = null;  
    

      
    
    public static HttpClient httpClient = null;  
      
   
    private static String loginEmail = null;  
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
		
		HttpPost httpRequest =new HttpPost(httpUrl);
		List <NameValuePair> params = new ArrayList <NameValuePair>(); 
        params.add(new BasicNameValuePair("email", loginEmail)); 
        params.add(new BasicNameValuePair("password", loginPassword)); 
      
		
		try{
	
			HttpClient httpclient=new DefaultHttpClient();

			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 		
			HttpResponse httpResponse=httpclient.execute(httpRequest);
		
			if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				
				
				List<Cookie> cookies = ((AbstractHttpClient) httpclient).getCookieStore().getCookies();    
				if (cookies.isEmpty()) {    
				//	Log.i(TAG, "-------Cookie NONE---------");    
				} 
				else {                   
					for (int i = 0; i < cookies.size(); i++ ){    
				
						Cookie cookie = cookies.get(i);    
					//	Log.d(TAG, cookies.get(i).getName() "=" cookies.get(i).getValue() );    
					}
				}  

				
				String strResult=EntityUtils.toString(httpResponse.getEntity());
			//	System.out.println(strResult);
				JSONObject jsonObject = new JSONObject(strResult) ;
				actionResult=jsonObject.getBoolean("ActionResult");
				nickname=jsonObject.getString("nickname");
				//System.out.println(actionResult+nickname);
				
				 
				  
			}
		}
		catch(Exception e){
			return false;
		}
		return actionResult;
	}

public static void logout(){  
    loginInstance = null;  
}  

	

}
