package com.shining.ibookclub.bean;

public class BookInfo{
	
	private String isbn;
	private String name;
	private String author;
	private String imageUrl;
	private String summary;
	
	
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
	
	public String getSummary(){
		
		return summary;
	}
	
	public void setSummary(String summary){
		
		this.summary=summary;
	}
	
}
