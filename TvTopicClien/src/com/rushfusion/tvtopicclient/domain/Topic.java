package com.rushfusion.tvtopicclient.domain;

import java.io.Serializable;

public class Topic implements Serializable{
   private Program program;
   private long created_at;
   private String topic_name;
   private User user;
   private int be_favorited;
   private int createdby ;
   private int posts;
   private int id;
   private boolean favorite;
   private int valid;
   private int programid;
public Program getProgram() {
	return program;
}
public void setProgram(Program program) {
	this.program = program;
}
public long getCreated_at() {
	return created_at;
}
public void setCreated_at(long created_at) {
	this.created_at = created_at;
}
public String getTopic_name() {
	return topic_name;
}
public void setTopic_name(String topic_name) {
	this.topic_name = topic_name;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public int getBe_favorited() {
	return be_favorited;
}
public void setBe_favorited(int be_favorited) {
	this.be_favorited = be_favorited;
}
public int getCreatedby() {
	return createdby;
}
public void setCreatedby(int createdby) {
	this.createdby = createdby;
}
public int getPosts() {
	return posts;
}
public void setPosts(int posts) {
	this.posts = posts;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public boolean isFavorite() {
	return favorite;
}
public void setFavorite(boolean favorite) {
	this.favorite = favorite;
}
public int getValid() {
	return valid;
}
public void setValid(int valid) {
	this.valid = valid;
}
public int getProgramid() {
	return programid;
}
public void setProgramid(int programid) {
	this.programid = programid;
}
   
}
