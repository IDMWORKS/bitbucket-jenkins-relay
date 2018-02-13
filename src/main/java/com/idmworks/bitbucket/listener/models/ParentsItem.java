package com.idmworks.bitbucket.listener.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ParentsItem{

	@JsonProperty("links")
	private Links links;

	@JsonProperty("type")
	private String type;

	@JsonProperty("hash")
	private String hash;

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

	public void setHash(String hash){
		this.hash = hash;
	}

	public String getHash(){
		return hash;
	}

	@Override
 	public String toString(){
		return 
			"ParentsItem{" + 
			"links = '" + links + '\'' + 
			",type = '" + type + '\'' + 
			",hash = '" + hash + '\'' + 
			"}";
		}
}