package com.borowski.models;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update todo set deleted = 1, deleted_at = now() where id = ?")
@Filter(name = "filterTaskNotDeleted")
public class Message extends EntityMetadata {
	private String message;
	@ManyToOne
	@JoinColumn(name = "TODO_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_TODO_ID"))
	private Task task;
	@ManyToOne
	@JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_USER_ID"))
	private User owner;
	private SoftDeletable softDeletable;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public SoftDeletable getSoftDeletable() {
		return softDeletable;
	}

	public void setSoftDeletable(SoftDeletable softDeletable) {
		this.softDeletable = softDeletable;
	}

	@Override
	public String toString() {
		return super.toString() + " - Message [message=" + message + ", task=" + task + ", owner=" + owner + "]";
	}
	
	/**
	 * Replaces all non-metadata fields with param's data. Doesn't overwrite with null values;
	 * @param updateData - Desired data.
	 * @return this
	 */
	public Message updateFields(Message updateData) {
		return updateFields(updateData, false);
	}
	
	/**
	 * Replaces non-metadata fields with param's data.
	 * @param updateData - Desired data.
	 * @param fillNulls - Should null values be written too.
	 * @return this
	 */
	public Message updateFields(Message updateData, boolean fillNulls) {
		if(fillNulls) {
			this.message = updateData.getMessage();
			this.task = updateData.getTask();
			this.owner = updateData.getOwner();
			this.softDeletable = updateData.getSoftDeletable() == null ? new SoftDeletable() : updateData.getSoftDeletable();
		} else {
			if(updateData.getMessage() != null)
				this.message = updateData.getMessage();
			if(updateData.getTask() != null)
				this.task = updateData.getTask();
			if(updateData.owner != null)
				this.owner = updateData.getOwner();
			if(updateData.getSoftDeletable() != null)
				this.softDeletable = updateData.getSoftDeletable();
		}
		
		return this;
	}
}
