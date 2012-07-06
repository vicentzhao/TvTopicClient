package com.rushfusion.tvtopicclient.util;

import org.json.JSONObject;

import com.rushfusion.tvtopicclient.domain.Program;

public class JSONObject2Program {

	public Program getProgram(JSONObject jsProgram) throws Exception {
		Program program = new Program();
		String pro_title;
		if(!jsProgram.isNull("image")){
		String program_path = jsProgram.getString("image");
		program.setImagePath(program_path);
		 }
		if(!jsProgram.isNull("description")){
		String program_desc = jsProgram.getString("description");
		program.setDescription(program_desc);
		}
		if(!jsProgram.isNull("topics")){
		int program_topics = jsProgram.getInt("topics");
		program.setTopics(program_topics);
		}
		if(!jsProgram.isNull("keyword")){
		String keyword = jsProgram.getString("keyword");
		program.setKey(keyword);
		}
		if(!jsProgram.isNull("created_at")){
		long program_created_at = jsProgram.getLong("created_at");
		program.setCreated_at(program_created_at);
		}
		if(!jsProgram.isNull("be_favorited")){
		int pro_be_favorited = jsProgram.getInt("be_favorited");
		program.setBe_favorited(pro_be_favorited);
		}
		if(!jsProgram.isNull("dircetor")){
		String pro_director = jsProgram.getString("dircetor");
		program.setDirector(pro_director);
		}
		if(!jsProgram.isNull("posts")){
		int pro_posts = jsProgram.getInt("posts");
		program.setPosts(pro_posts);
		}
		if(!jsProgram.isNull("actor")){
		String actor = jsProgram.getString("actor");
		program.setActor(actor);
		}
		if(!jsProgram.isNull("id")){
		int pro_id = jsProgram.getInt("id");
		program.setId(pro_id);
		}
		if(!jsProgram.isNull("favorite")){
		boolean pro_favorite = jsProgram.getBoolean("favorite");
		program.setFavorite(pro_favorite);
		}
		if(!jsProgram.isNull("valid")){
		int pro_valid = jsProgram.getInt("valid");
		program.setValid(pro_valid);
		}
		if (!jsProgram.isNull("title")) {
			pro_title = jsProgram.getString("title");
			program.setTitle(pro_title);
		}
		
		return program;
	}

}
