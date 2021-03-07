package com.borowski.models;

public class Todo {
	private int id;
	private String title;
	private String description;
	private Priority priority;

	public Todo(String title, String description, Priority priority) {
		super();
		this.title = title;
		this.description = description;
		this.priority = priority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
		return "Todo [id=" + id + ", title=" + title + ", description=" + description + ", priority=" + priority + "]";
	}

}
