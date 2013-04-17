package com.shining.ibookclub.support;

import java.security.MessageDigest;

public class Encryption {

	private String input;
	
	private final static String[] hexDigits = {"0", "1", "2", "3", "4",  
	        "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};  
	
	public Encryption(String input){
		
		this.input=input;
	}
	
	public String encrypt(){
		
		String result=null;
		MessageDigest md=null;
		
		try{
			
			md=MessageDigest.getInstance("MD5");
			result=byteArrayToHexString(md.digest(input.getBytes()));
				
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}
	

	 private static String byteArrayToHexString(byte[] b){  
		  
	        StringBuffer resultSb = new StringBuffer();  
	        for (int i = 0; i < b.length; i++){  
	            resultSb.append(byteToHexString(b[i]));  
	        }  
	        return resultSb.toString();  
	 }  
	      
	 
	  private static String byteToHexString(byte b){  
	        int n = b;  
	        if (n < 0)  
	            n = 256 + n;  
	        int d1 = n / 16;  
	        int d2 = n % 16;  
	        return hexDigits[d1] + hexDigits[d2];  
	 }  
}
