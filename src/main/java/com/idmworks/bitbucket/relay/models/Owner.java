package com.idmworks.bitbucket.relay.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Owner{

	@JsonProperty("links")
	private Links links;

	@JsonProperty("type")
	private String type;

	@JsonProperty("display_name")
	private String displayName;

	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("username")
	private String username;

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

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setUuid(String uuid){
		this.uuid = uuid;
	}

	public String getUuid(){
		return uuid;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"Owner{" + 
			"links = '" + links + '\'' + 
			",type = '" + type + '\'' + 
			",display_name = '" + displayName + '\'' + 
			",uuid = '" + uuid + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}