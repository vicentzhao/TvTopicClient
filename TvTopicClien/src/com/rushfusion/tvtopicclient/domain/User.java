package com.rushfusion.tvtopicclient.domain;

import java.io.Serializable;

public class User implements Serializable{

	private int favorite_programs;
	private int mycomments;
	private int followings;
	private int favorite_posts;
	private int topics;
	private long updated_at;
	private int commentmys;
	private int mentions;
	private String name;
	private Boolean following;
	private int posts;
	private int myreposts;
	private int followers;
	private int favorite_topics;
	private long created_at;
	private int id;
	private String image;
	private String password;
	private String email;
	private int valid;
	public int getFavorite_programs() {
		return favorite_programs;
	}
	public void setFavorite_programs(int favorite_programs) {
		this.favorite_programs = favorite_programs;
	}
	public int getMycomments() {
		return mycomments;
	}
	public void setMycomments(int mycomments) {
		this.mycomments = mycomments;
	}
	public int getFollowings() {
		return followings;
	}
	public void setFollowings(int followings) {
		this.followings = followings;
	}
	public int getFavorite_posts() {
		return favorite_posts;
	}
	public void setFavorite_posts(int favorite_posts) {
		this.favorite_posts = favorite_posts;
	}
	public int getTopics() {
		return topics;
	}
	public void setTopics(int topics) {
		this.topics = topics;
	}
	public long getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(long updated_at) {
		this.updated_at = updated_at;
	}
	public int getCommentmys() {
		return commentmys;
	}
	public void setCommentmys(int commentmys) {
		this.commentmys = commentmys;
	}
	public int getMentions() {
		return mentions;
	}
	public void setMentions(int mentions) {
		this.mentions = mentions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getFollowing() {
		return following;
	}
	public void setFollowing(Boolean following) {
		this.following = following;
	}
	public int getPosts() {
		return posts;
	}
	public void setPosts(int posts) {
		this.posts = posts;
	}
	public int getMyreposts() {
		return myreposts;
	}
	public void setMyreposts(int myreposts) {
		this.myreposts = myreposts;
	}
	public int getFollowers() {
		return followers;
	}
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	public int getFavorite_topics() {
		return favorite_topics;
	}
	public void setFavorite_topics(int favorite1_topics) {
		this.favorite_topics = favorite1_topics;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}

}
