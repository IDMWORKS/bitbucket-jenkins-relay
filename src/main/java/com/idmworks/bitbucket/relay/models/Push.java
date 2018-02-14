package com.idmworks.bitbucket.relay.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;
import java.util.List;

@Generated("com.robohorse.robopojogenerator")
public class Push{

	@JsonProperty("changes")
	private List<Change> changes;

	public void setChanges(List<Change> changes){
		this.changes = changes;
	}

	public List<Change> getChanges(){
		return changes;
	}

	@Override
 	public String toString(){
		return 
			"Push{" + 
			"changes = '" + changes + '\'' + 
			"}";
		}
}