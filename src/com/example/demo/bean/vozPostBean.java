package com.example.demo.bean;

public class vozPostBean {
	private String title;
	private String url;
	private boolean addedInFavorite, completed;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isAddedInFavorite() {
		return addedInFavorite;
	}
	public void setAddedInFavorite(boolean addedInFavorite) {
		this.addedInFavorite = addedInFavorite;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	
}
