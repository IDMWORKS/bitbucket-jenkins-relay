package com.idmworks.bitbucket.listener.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Target{

	@JsonProperty("date")
	private String date;

	@JsonProperty("author")
	private Author author;

	@JsonProperty("links")
	private Links links;

	@JsonProperty("message")
	private String message;

	@JsonProperty("type")
	private String type;

	@JsonProperty("hash")
	private String hash;

	@JsonProperty("parents")
	private List<ParentsItem> parents;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setAuthor(Author author){
		this.author = author;
	}

	public Author getAuthor(){
		return author;
	}

	public void setLinks(Links links){
		this.links = links;
	}

	public Links getLinks(){
		return links;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
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

	public void setParents(List<ParentsItem> parents){
		this.parents = parents;
	}

	public List<ParentsItem> getParents(){
		return parents;
	}

	@Override
 	public String toString(){
		return 
			"Target{" + 
			"date = '" + date + '\'' + 
			",author = '" + author + '\'' + 
			",links = '" + links + '\'' + 
			",message = '" + message + '\'' + 
			",type = '" + type + '\'' + 
			",hash = '" + hash + '\'' + 
			",parents = '" + parents + '\'' + 
			"}";
		}
}