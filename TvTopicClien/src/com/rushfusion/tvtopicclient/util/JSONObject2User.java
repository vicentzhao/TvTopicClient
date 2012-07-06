package com.rushfusion.tvtopicclient.util;

import org.json.JSONObject;

import com.rushfusion.tvtopicclient.domain.User;

public class JSONObject2User {
	public  User getUser(JSONObject jsUser) throws Exception{
		User user = new User();
		if(!jsUser.isNull("image")){
			String	userImagePath = jsUser.getString("image");
			user.setImage(userImagePath);
		}
		if(!jsUser.isNull("name")){
			String 	userName = jsUser.getString("name");
			user.setName(userName);
		}
		if(!jsUser.isNull("favorite-programs")){
			int  favorite_programs =jsUser.getInt("favorite-programs");
			user.setFavorite_programs(favorite_programs);
		}
		if(!jsUser.isNull("mycomments")){
			int  mycomments =jsUser.getInt("mycomments");
			user.setMycomments(mycomments);
		}
		if(!jsUser.isNull("followings")){
			int followings =jsUser.getInt("followings");
			user.setFollowings(followings);
		}
		if(!jsUser.isNull("favorite-posts")){
			int  favorite_posts =jsUser.getInt("favorite-posts");
			user.setFavorite_posts(favorite_posts);
		}
		if(!jsUser.isNull("topics")){
			int topics =jsUser.getInt("topics");
			user.setTopics(topics);
		}
		if(!jsUser.isNull("updated-at")){
			long updated_at =jsUser.getLong("updated-at");
			user.setUpdated_at(updated_at);
		}
		if(!jsUser.isNull("commentmys")){
			int commentmys  =jsUser.getInt("commentmys");
			user.setCommentmys(commentmys);
		}
		if(!jsUser.isNull("mentions")){
			int mentions =jsUser.getInt("mentions");
			user.setMentions(mentions);
		}
		if(!jsUser.isNull("following")){
			boolean following =jsUser.getBoolean("following");
			user.setFollowing(following);
		}
		if(!jsUser.isNull("posts")){
			int posts =jsUser.getInt("posts");
			user.setPosts(posts);
		}
		if(!jsUser.isNull("myreposts")){
			int myreposts =jsUser.getInt("myreposts");
			user.setMyreposts(myreposts);
		}
		if(!jsUser.isNull("followers")){
			int followers =jsUser.getInt("followers");
			user.setFollowers(followers);
		}
		if(!jsUser.isNull("favorite-topics")){
			int favorite_topics =jsUser.getInt("favorite-topics");
			user.setFavorite_topics(favorite_topics);
		}
		if(!jsUser.isNull("password")){
		String password=jsUser.getString("password");
		user.setPassword(password);
		}
		if(!jsUser.isNull("created-at")){
			long created_at=jsUser.getLong("created-at");
			user.setCreated_at(created_at);
		}
		if(!jsUser.isNull("id")){
			int id=jsUser.getInt("id");
			user.setId(id);
		}
		if(!jsUser.isNull("valid")){
			int valid=jsUser.getInt("valid");
			user.setValid(valid);
		}
		if(!jsUser.isNull("email")){
		String email =jsUser.getString("email");
		user.setEmail(email);
	}
		return user;                     
	}
}
