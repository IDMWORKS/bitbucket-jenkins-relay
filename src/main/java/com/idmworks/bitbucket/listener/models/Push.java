package com.idmworks.bitbucket.listener.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Push{

	@JsonProperty("changes")
	private List<ChangesItem> changes;

	public void setChanges(List<ChangesItem> changes){
		this.changes = changes;
	}

	public List<ChangesItem> getChanges(){
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