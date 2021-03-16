package com.borowski.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.borowski.models.embeddable.SoftDeletable;

@Entity
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update todo set deleted = 1, deleted_at = now() where id = ?")
@Where(clause = "deleted_at IS NULL")
public class Todo extends EntityMetadata {
	private String title;
	private String description;
	private Priority priority;
	@ManyToOne()
	@JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_USER_ID"))
	private User owner;
	@ManyToMany
	@JoinTable(name = "TODO_USER", foreignKey = @ForeignKey(name = "FK_TODO_ID"), inverseForeignKey = @ForeignKey(name = "FK_WATCHER_ID"))
	private List<User> watchers;
	private SoftDeletable softDeletable;
	
	public SoftDeletable getSoftDeletable() {
		return softDeletable;
	}

	public void setSoftDeletable(SoftDeletable softDeletable) {
		this.softDeletable = softDeletable;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<User> getWatchers() {
		return watchers;
	}

	public void setWatchers(List<User> watchers) {
		this.watchers = watchers;
	}

	@Override
	public String toString() {
		return super.toString() + " - Todo [title=" + title + ", description=" + description + ", priority=" + priority + ", owner=" + owner
				+ ", watchers=" + watchers + ", softDeletable=" + softDeletable + "]";
	}
	
	
}
