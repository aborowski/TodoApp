package com.borowski.models;

import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import com.borowski.models.embeddable.SoftDeletable;

@FilterDef(
		name = "filterUserNotDeleted",
		parameters = @ParamDef(name = "deleted", type = "boolean"),
		defaultCondition = "deleted = :deleted")
		

@Entity
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update user set deleted = 1, deleted_at = now() where id = ?")
@Filter(name = "filterUserNotDeleted")
public class User extends EntityMetadata {
	private String username;
	private String email;
	private SoftDeletable softDeletable;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SoftDeletable getSoftDeletable() {
		return softDeletable;
	}

	public void setSoftDeletable(SoftDeletable softDeletable) {
		this.softDeletable = softDeletable;
	}

	@Override
	public String toString() {
		return super.toString() + " - User [username=" + username + ", email=" + email + ", softDeletable=" + softDeletable + "]";
	}

	/**
	 * Replaces all non-metadata fields with param's data. Doesn't overwrite with null values;
	 * @param updateData - Desired data.
	 * @return this
	 */
	public User updateFields(User updateData) {
		return updateFields(updateData, false);
	}
	
	/**
	 * Replaces non-metadata fields with param's data.
	 * @param updateData - Desired data.
	 * @param fillNulls - Should null values be written too.
	 * @return this
	 */
	public User updateFields(User updateData, boolean fillNulls) {
		if(fillNulls) {
			this.username = updateData.getUsername();
			this.email = updateData.getEmail();
			this.softDeletable = updateData.getSoftDeletable() == null ? new SoftDeletable() : updateData.getSoftDeletable();
			
		} else {
			if(updateData.getUsername() != null)
				this.username = updateData.getUsername();
			if(updateData.getEmail() != null)
				this.email = updateData.getEmail();
			if(updateData.getSoftDeletable() != null)
				this.softDeletable = updateData.getSoftDeletable();
		}
		
		return this;
	}
}
