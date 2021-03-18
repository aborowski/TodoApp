package com.borowski.models;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Message extends EntityMetadata {
	private String message;
	@ManyToOne
	@JoinColumn(name = "TODO_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_TODO_ID"))
	private Task task;
	@ManyToOne
	@JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_USER_ID"))
	private User owner;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
