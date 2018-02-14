package com.idmworks.bitbucket.listener.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Links{

	@JsonProperty("patch")
	private Link patch;

	@JsonProperty("comments")
	private Link comments;

	@JsonProperty("approve")
	private Link approve;

	@JsonProperty("self")
	private Link self;

	@JsonProperty("statuses")
	private Link statuses;

	@JsonProperty("html")
	private Link html;

	@JsonProperty("diff")
	private Link diff;

	public void setPatch(Link patch){
		this.patch = patch;
	}

	public Link getPatch(){
		return patch;
	}

	public void setComments(Link comments){
		this.comments = comments;
	}

	public Link getComments(){
		return comments;
	}

	public void setApprove(Link approve){
		this.approve = approve;
	}

	public Link getApprove(){
		return approve;
	}

	public void setSelf(Link self){
		this.self = self;
	}

	public Link getSelf(){
		return self;
	}

	public void setStatuses(Link statuses){
		this.statuses = statuses;
	}

	public Link getStatuses(){
		return statuses;
	}

	public void setHtml(Link html){
		this.html = html;
	}

	public Link getHtml(){
		return html;
	}

	public void setDiff(Link diff){
		this.diff = diff;
	}

	public Link getDiff(){
		return diff;
	}

	@Override
 	public String toString(){
		return 
			"Links{" + 
			"patch = '" + patch + '\'' + 
			",comments = '" + comments + '\'' + 
			",approve = '" + approve + '\'' + 
			",self = '" + self + '\'' + 
			",statuses = '" + statuses + '\'' + 
			",html = '" + html + '\'' + 
			",diff = '" + diff + '\'' + 
			"}";
		}
}