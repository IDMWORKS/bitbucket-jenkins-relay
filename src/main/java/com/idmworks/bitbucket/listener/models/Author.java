package com.idmworks.bitbucket.listener.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Author{

	@JsonProperty("raw")
	private String raw;

	@JsonProperty("type")
	private String type;

	@JsonProperty("user")
	private User user;

	public void setRaw(String raw){
		this.raw = raw;
	}

	public String getRaw(){
		return raw;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"Author{" + 
			"raw = '" + raw + '\'' + 
			",type = '" + type + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}