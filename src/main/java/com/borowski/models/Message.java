package com.borowski.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;

import com.borowski.models.embeddable.SoftDeletable;

@FilterDef(
		name = "filterMessageNotDeleted",
		defaultCondition = "deleted = 0")

@Entity
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update message set deleted = 1, deleted_at = now() where id = ?")
@Filter(name = "filterMessageNotDeleted")
public class Message extends EntityMetadata {
	@Column(length = 4000)
	@NotBlank(message = "Message cannot be blank")
	@Size(max = 4000, message = "Message cannot be longer that 4000 characters")
	private String message;
	@ManyToOne
	@JoinColumn(name = "TODO_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_TODO_ID"))
	//TODO: make not nullable, automatically insert the current task
	private Task task;
	@ManyToOne
	@JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MESSAGE_USER_ID"))
	//TODO: make not nullable when logging in is implemented
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
