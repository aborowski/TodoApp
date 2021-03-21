package com.borowski.models;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;

import com.borowski.models.embeddable.SoftDeletable;

@FilterDef(
		name = "filterUserNotDeleted",
		defaultCondition = "deleted = 0")
		

@Entity
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update user set deleted = 1, deleted_at = now() where id = ?")
@Filter(name = "filterUserNotDeleted")
public class User extends EntityMetadata {
	@Column(columnDefinition = "varchar(50) not null unique")
	private String username;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private SoftDeletable softDeletable;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getName() {
		return Stream.of(this.getFirstName(), this.getMiddleName(), this.getLastName())
				.filter(s -> s != null && !s.isEmpty())
				.collect(Collectors.joining(" "));
	}
	
	public void setName(String name) {
		String[] splitName = name.split(" ");
		try {
			this.setFirstName(splitName[0]);
			this.setLastName(splitName[1]);
		}
		catch(IndexOutOfBoundsException iobe) {
			
		}
		
		this.setFirstName(splitName[0]);
		if(splitName.length >= 2) {
			this.setLastName(splitName[splitName.length - 1]);
			if(splitName.length > 2) {
				this.setMiddleName(StringUtils.join(splitName, " ", 1, splitName.length - 1));
			}
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
