package com.shining.ibookclub.bean;

public class BookBean{
	
	private String isbn;
	private String bookname;
	private String publisher;
	private String author;
	private String bookcover_url;
	private String summary;
	private String price;
	
	public void setIsbn(String isbn){
		this.isbn=isbn;
	}
	
	public void setBookname(String bookname){
		this.bookname=bookname;
	}
	
	public void setPublisher(String publisher){
		this.publisher=publisher;
	}
	
	public void setAuthor(String author){
		this.author=author;
	}
	
	public void setBookcover_url(String bookcover_url){
		this.bookcover_url=bookcover_url;
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
	
	public String getBookname(){
		return this.bookname;
	}
	
	public String getPublisher(){
		return this.publisher;
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public String getBookcover_url(){
		return this.bookcover_url;
	}
	
	public String getSummary(){
		return this.summary;
	}
	
	public String getPrice(){
		return this.price;
	}
	
}
