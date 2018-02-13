package com.idmworks.bitbucket.listener.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ChangesItem{

	@JsonProperty("new")
	private RefState newState;

	@JsonProperty("forced")
	private boolean forced;

	@JsonProperty("oldState")
	private RefState oldState;

	@JsonProperty("created")
	private boolean created;

	@JsonProperty("truncated")
	private boolean truncated;

	@JsonProperty("commits")
	private List<CommitsItem> commits;

	@JsonProperty("closed")
	private boolean closed;

	@JsonProperty("links")
	private Links links;

	public void setNewState(RefState newState){
		this.newState = newState;
	}

	public RefState getNewState(){
		return newState;
	}

	public void setForced(boolean forced){
		this.forced = forced;
	}

	public boolean isForced(){
		return forced;
	}

	public void setOldState(RefState oldState){
		this.oldState = oldState;
	}

	public RefState getOldState(){
		return oldState;
	}

	public void setCreated(boolean created){
		this.created = created;
	}

	public boolean isCreated(){
		return created;
	}

	public void setTruncated(boolean truncated){
		this.truncated = truncated;
	}

	public boolean isTruncated(){
		return truncated;
	}

	public void setCommits(List<CommitsItem> commits){
		this.commits = commits;
	}

	public List<CommitsItem> getCommits(){
		return commits;
	}

	public void setClosed(boolean closed){
		this.closed = closed;
	}

	public boolean isClosed(){
		return closed;
	}

	public void setLinks(Links links){
		this.links = links;
	}

	public Links getLinks(){
		return links;
	}

	@Override
 	public String toString(){
		return 
			"ChangesItem{" + 
			"new = '" + newState + '\'' +
			",forced = '" + forced + '\'' + 
			",oldState = '" + oldState + '\'' +
			",created = '" + created + '\'' + 
			",truncated = '" + truncated + '\'' + 
			",commits = '" + commits + '\'' + 
			",closed = '" + closed + '\'' + 
			",links = '" + links + '\'' + 
			"}";
		}
}