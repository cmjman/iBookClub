package com.shining.ibookclub.support;


import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;



public class HttpUtility {
	
	HttpClient httpClient;
	HttpPost postRequest;
	HttpResponse httpResponse;
	
	List <NameValuePair> params;
	String url;
	String result;

	
	public HttpUtility(String url,List <NameValuePair> params){
		
		this.url=url;
		this.params=params;
	}
	
	public String doPost(){
		
		
		try{
			
			postRequest =new HttpPost(url);
			
			httpClient=new DefaultHttpClient();
			
			
			if(params!=null)
	
				postRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 	
			
			postRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5" );

			
			
			httpResponse=httpClient.execute(postRequest);
			
			if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				
				result=new String(EntityUtils.toString(httpResponse.getEntity()).getBytes("ISO-8859-1"),"UTF-8");
				
				System.out.println("HttpUtility："+result);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
		
	}
	
}
