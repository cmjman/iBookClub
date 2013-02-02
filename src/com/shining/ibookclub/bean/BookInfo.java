package com.shining.ibookclub.bean;

public class BookInfo{
	
	private String isbn;
	private String name;
	private String author;
	private String imageUrl;
	private String summary;
	private String publisher;
	private String price;
	
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getName(){
		
		return name;
	}
	
	public void setName(String name){
		
		this.name=name;
	}
	
	public String getAuthor(){
		
		return author;
	}
	
	public void setAuthor(String author){
		
		this.author=author;
	}
	
	public String getImageUrl(){
		
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl){
		
		this.imageUrl=imageUrl;
		
	}
	
	public String getPublisher(){
		
		return this.publisher;
	}
	
	public void setPublisher(String publisher){
		
		this.publisher=publisher;
	}
	
	public String getPrice(){
		return this.price;
	}
	
	public void setPrice(String price){
		this.price=price;
	}
	
	public  String getSummary(){
		
		return summary;
	}
	
	
	
	public void setSummary(String summary){
		
		this.summary=summary;
	}
	
}
