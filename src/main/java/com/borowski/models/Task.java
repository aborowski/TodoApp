package com.borowski.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;

import com.borowski.models.embeddable.SoftDeletable;

@FilterDef(
		name = "filterTaskNotDeleted",
		defaultCondition = "deleted = 0")

@Entity
@Table(name = "TODO")
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update todo set deleted = 1, deleted_at = now() where id = ?")
@Filter(name = "filterTaskNotDeleted")
public class Task extends EntityMetadata {
	@Column(nullable = false, length = 255)
	@NotBlank(message = "Title cannot be blank")
	@Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters long")
	private String title;
	@Column(length = 4000)
	@Size(max = 4000, message = "Description cannot be longer than 4000 characters")
	private String description;
	//TODO: validation
	private Priority priority;
	@ManyToOne
	@JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_USER_ID"))
	//TODO: make not nullable when logging in is implemented
	private User owner;
	@ManyToMany
	@JoinTable(name = "TODO_USER", foreignKey = @ForeignKey(name = "FK_TODO_ID"), inverseForeignKey = @ForeignKey(name = "FK_WATCHER_ID"))
	//TODO: automatically add present use when loggin in is implemented
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
				+ ", softDeletable=" + softDeletable + "]";
	}
	
	/**
	 * Replaces all non-metadata fields with param's data. Doesn't overwrite with null values;
	 * @param updateData - Desired data.
	 * @return this
	 */
	public Task updateFields(Task updateData) {
		return updateFields(updateData, false);
	}
	
	/**
	 * Replaces non-metadata fields with param's data.
	 * @param updateData - Desired data.
	 * @param fillNulls - Should null values be written too.
	 * @return this
	 */
	public Task updateFields(Task updateData, boolean fillNulls) {
		if(fillNulls) {
			this.description = updateData.getDescription();
			this.title = updateData.getTitle();
			this.owner = updateData.getOwner();
			this.priority = updateData.getPriority();
			this.softDeletable = updateData.getSoftDeletable() == null ? new SoftDeletable() : updateData.getSoftDeletable();
			this.watchers = updateData.getWatchers();
		} else {
			if(updateData.getDescription() != null)
				this.description = updateData.getDescription();
			if(updateData.getTitle() != null)
				this.title = updateData.getTitle();
			if(updateData.owner != null)
				this.owner = updateData.getOwner();
			if(updateData.getPriority() != null)
				this.priority = updateData.getPriority();
			if(updateData.getSoftDeletable() != null)
				this.softDeletable = updateData.getSoftDeletable();
			if(updateData.getWatchers() != null)
				this.watchers = updateData.getWatchers();
		}
		
		return this;
	}
}
