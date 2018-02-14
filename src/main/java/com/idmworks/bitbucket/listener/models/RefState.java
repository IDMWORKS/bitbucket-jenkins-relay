package com.idmworks.bitbucket.listener.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RefState {

	@JsonProperty("name")
	private String name;

	@JsonProperty("links")
	private Links links;

	@JsonProperty("type")
	private String type;

	@JsonProperty("target")
	private Target target;

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

	public void setTarget(Target target){
		this.target = target;
	}

	public Target getTarget(){
		return target;
	}

	@Override
 	public String toString(){
		return 
			"RefState{" +
			"name = '" + name + '\'' + 
			",links = '" + links + '\'' + 
			",type = '" + type + '\'' + 
			",target = '" + target + '\'' + 
			"}";
		}
}