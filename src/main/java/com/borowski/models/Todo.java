package com.borowski.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Todo extends EntityMetadata {
	private String title;
	private String description;
	private Priority priority;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Todo [" +  super.toString() + title + ", description=" + description + ", priority=" + priority + "]";
	}

}
