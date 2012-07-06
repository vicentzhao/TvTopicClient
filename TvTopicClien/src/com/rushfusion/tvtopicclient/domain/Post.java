package com.rushfusion.tvtopicclient.domain;

import java.io.Serializable;

public class Post implements Serializable {

	private String c;
	private User user;
	private int fs;
	private Topic topic;
	private int v;
	private Long ct;
	private int p;
	private int t;
	private int u;
	private int id;
	private boolean favorite;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getFs() {
		return fs;
	}

	public void setFs(int fs) {
		this.fs = fs;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public long getCt() {
		return ct;
	}

	public void setCt(long ct) {
		this.ct = ct;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public int getU() {
		return u;
	}

	public void setU(int u) {
		this.u = u;
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

}
