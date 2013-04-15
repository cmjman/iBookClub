package com.shining.ibookclub.bean;

import java.io.Serializable;

public class BookBean implements Serializable,Bean{
	
	private String isbn;
	private String name;
	private String publisher;
	private String author;
	private String bookcover;
	private String summary;
	private String price;
	
	private String timestamp;
	
	public void setIsbn(String isbn){
		this.isbn=isbn;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setPublisher(String publisher){
		this.publisher=publisher;
	}
	
	public void setAuthor(String author){
		this.author=author;
	}
	
	public void setBookcover(String bookcover){
		this.bookcover=bookcover;
	}
	
	public void setSummary(String summary){
		this.summary=summary;
	}
	
	public void setPrice(String price){
		this.price=price;
	}
	
	public String getIsbn(){
		
		return this.isbn;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPublisher(){
		return this.publisher;
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public String getBookcover(){
		return this.bookcover;
	}
	
	public String getSummary(){
		return this.summary;
	}
	
	public String getPrice(){
		return this.price;
	}

	@Override
	public String getMessage() {
		
		return this.name;
	}

	@Override
	public String getAvatar() {

		return this.bookcover;
	}
	
	public void setTimeStamp(String timestamp){
		
		this.timestamp=timestamp;
	}

	
	public String getTimeStamp() {
	
		return this.timestamp;
	}
	
}
