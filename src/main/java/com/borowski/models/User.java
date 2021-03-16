package com.borowski.models;

import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.borowski.models.embeddable.SoftDeletable;

@Entity
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "update user set deleted = 1, deleted_at = now() where id = ?")
@Where(clause = "deleted_at IS NULL")
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

}
