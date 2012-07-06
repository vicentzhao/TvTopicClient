package com.rushfusion.tvtopicclient.domain;

import java.io.Serializable;

public class Program implements Serializable {
     private int topics;
     private String key;
     private  long created_at;
     private String director;
     private int posts;
     private String actor;
     private String title;
     private int id;
     private String imagePath;
     private boolean favorite;
     private String description;
     private int be_favorited;
     private int valid;
     
	public int getBe_favorited() {
		return be_favorited;
	}
	public void setBe_favorited(int be_favorited) {
		this.be_favorited = be_favorited;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public Program() {
		super();
	}
	public int getTopics() {
		return topics;
	}
	public void setTopics(int topics) {
		this.topics = topics;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public int getPosts() {
		return posts;
	}
	public void setPosts(int posts) {
		this.posts = posts;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
