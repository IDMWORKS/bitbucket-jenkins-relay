package com.idmworks.bitbucket.listener.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Project{

	@JsonProperty("name")
	private String name;

	@JsonProperty("links")
	private Links links;

	@JsonProperty("type")
	private String type;

	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("key")
	private String key;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLinks(Links links){
		this.links = links;
	}

	public Links getLinks(){
		return links;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setUuid(String uuid){
		this.uuid = uuid;
	}

	public String getUuid(){
		return uuid;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	@Override
 	public String toString(){
		return 
			"Project{" + 
			"name = '" + name + '\'' + 
			",links = '" + links + '\'' + 
			",type = '" + type + '\'' + 
			",uuid = '" + uuid + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}