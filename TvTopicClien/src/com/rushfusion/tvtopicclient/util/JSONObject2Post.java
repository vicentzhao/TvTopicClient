package com.rushfusion.tvtopicclient.util;

import org.json.JSONObject;

import com.rushfusion.tvtopicclient.domain.Post;
import com.rushfusion.tvtopicclient.domain.Topic;
import com.rushfusion.tvtopicclient.domain.User;

public class JSONObject2Post {
	public Post getPost(JSONObject jsPost) throws Exception {
		Post post = new Post();
		if(!jsPost.isNull("c")){
		String c = jsPost.getString("c");
		post.setC(c);
		}
		if(!jsPost.isNull("user")){
		JSONObject jsUser = jsPost.getJSONObject("user");
		JSONObject2User ju = new JSONObject2User();
		User user = ju.getUser(jsUser);
		post.setUser(user);
		}
		if(!jsPost.isNull("fs")){
		int fs = jsPost.getInt("fs");
		post.setFs(fs);
		}
		if(!jsPost.isNull("topic")){
		JSONObject jsTopic = jsPost.getJSONObject("topic");
		JSONObject2Topic jt = new JSONObject2Topic();
		Topic topic = jt.getTopic(jsTopic);
		post.setTopic(topic);
		}
		if(!jsPost.isNull("p")){
		int p = jsPost.getInt("p");
		post.setP(p);
		}
		if(!jsPost.isNull("ct")){
		long ct = jsPost.getLong("ct");
		post.setCt(ct);
		}
		if(!jsPost.isNull("t")){
		int t = jsPost.getInt("t");
		post.setT(t);
		}
		if(!jsPost.isNull("u")){
		int u = jsPost.getInt("u");
		post.setU(u);
		}
		if(!jsPost.isNull("v")){
		int v = jsPost.getInt("v");
		post.setV(v);
		}
		if(!jsPost.isNull("id")){
		int id = jsPost.getInt("id");
		post.setId(id);
		}
		if(!jsPost.isNull("favorite")){
		boolean favorite = jsPost.getBoolean("favorite");
		post.setFavorite(favorite);
		}
		return post;
	}
}
