package com.idmworks.bitbucket.listener.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Webhook {

	@JsonProperty("actor")
	private Actor actor;

	@JsonProperty("repository")
	private Repository repository;

	@JsonProperty("push")
	private Push push;

	public void setActor(Actor actor){
		this.actor = actor;
	}

	public Actor getActor(){
		return actor;
	}

	public void setRepository(Repository repository){
		this.repository = repository;
	}

	public Repository getRepository(){
		return repository;
	}

	public void setPush(Push push){
		this.push = push;
	}

	public Push getPush(){
		return push;
	}

	@Override
 	public String toString(){
		return 
			"Webhook{" +
			"actor = '" + actor + '\'' + 
			",repository = '" + repository + '\'' + 
			",push = '" + push + '\'' + 
			"}";
		}
}