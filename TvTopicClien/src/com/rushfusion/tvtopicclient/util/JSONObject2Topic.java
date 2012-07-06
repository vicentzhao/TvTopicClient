package com.rushfusion.tvtopicclient.util;

import org.json.JSONObject;

import com.rushfusion.tvtopicclient.domain.Program;
import com.rushfusion.tvtopicclient.domain.Topic;
import com.rushfusion.tvtopicclient.domain.User;

public class JSONObject2Topic {

	public Topic getTopic(JSONObject jsTopic) throws Exception {
		Topic topic = new Topic();
		if(!jsTopic.isNull("created_at")){
			long topicCreated_at = jsTopic.getLong("created_at");
			topic.setCreated_at(topicCreated_at);
		}
		if(!jsTopic.isNull("name")){
		String topicName = jsTopic.getString("name");
		topic.setTopic_name(topicName);
		}
		if(!jsTopic.isNull("be_favorited")){
		int topic_be_favorited = jsTopic.getInt("be_favorited");
		topic.setBe_favorited(topic_be_favorited);
		}
		if(!jsTopic.isNull("createdby")){
		int topic_createdby = jsTopic.getInt("createdby");
		topic.setCreatedby(topic_createdby);
		}
		if(!jsTopic.isNull("posts")){
		int topic_posts = jsTopic.getInt("posts");
		topic.setPosts(topic_posts);
		}
		if(!jsTopic.isNull("id")){
		int topicId = jsTopic.getInt("id");
		topic.setId(topicId);
		}
		if(!jsTopic.isNull("favorite")){
		boolean topic_favorite = jsTopic.getBoolean("favorite");
		topic.setFavorite(topic_favorite);
		}
		if(!jsTopic.isNull("valid")){
		int topic_valid = jsTopic.getInt("valid");
		topic.setValid(topic_valid);
		}
		if(!jsTopic.isNull("programid")){
		int topic_programid = jsTopic.getInt("programid");
		topic.setProgramid(topic_programid);
		}
		if(!jsTopic.isNull("program")){
		JSONObject jsProgram = jsTopic.getJSONObject("program");
		JSONObject2Program jp  =new JSONObject2Program();
		Program program = jp.getProgram(jsProgram);
		topic.setProgram(program);
		}
		if(!jsTopic.isNull("user")){
		JSONObject jsUser =jsTopic.getJSONObject("user");
		JSONObject2User ju =new JSONObject2User();
		User user = ju.getUser(jsUser);
		topic.setUser(user);
		}
		return topic;
	}

}
