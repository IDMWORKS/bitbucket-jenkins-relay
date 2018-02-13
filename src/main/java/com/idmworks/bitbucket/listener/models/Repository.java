package com.idmworks.bitbucket.listener.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Repository{

	@JsonProperty("owner")
	private Owner owner;

	@JsonProperty("is_private")
	private boolean isPrivate;

	@JsonProperty("website")
	private String website;

	@JsonProperty("full_name")
	private String fullName;

	@JsonProperty("name")
	private String name;

	@JsonProperty("project")
	private Project project;

	@JsonProperty("links")
	private Links links;

	@JsonProperty("scm")
	private String scm;

	@JsonProperty("type")
	private String type;

	@JsonProperty("uuid")
	private String uuid;

	public void setOwner(Owner owner){
		this.owner = owner;
	}

	public Owner getOwner(){
		return owner;
	}

	public void setIsPrivate(boolean isPrivate){
		this.isPrivate = isPrivate;
	}

	public boolean isIsPrivate(){
		return isPrivate;
	}

	public void setWebsite(String website){
		this.website = website;
	}

	public String getWebsite(){
		return website;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setProject(Project project){
		this.project = project;
	}

	public Project getProject(){
		return project;
	}

	public void setLinks(Links links){
		this.links = links;
	}

	public Links getLinks(){
		return links;
	}

	public void setScm(String scm){
		this.scm = scm;
	}

	public String getScm(){
		return scm;
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

	@Override
 	public String toString(){
		return 
			"Repository{" + 
			"owner = '" + owner + '\'' + 
			",is_private = '" + isPrivate + '\'' + 
			",website = '" + website + '\'' + 
			",full_name = '" + fullName + '\'' + 
			",name = '" + name + '\'' + 
			",project = '" + project + '\'' + 
			",links = '" + links + '\'' + 
			",scm = '" + scm + '\'' + 
			",type = '" + type + '\'' + 
			",uuid = '" + uuid + '\'' + 
			"}";
		}
}