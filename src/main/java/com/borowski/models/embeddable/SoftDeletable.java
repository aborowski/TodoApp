package com.borowski.models.embeddable;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SoftDeletable {
	@Column(columnDefinition = "tinyint default 0 ")
	private boolean deleted;
	private ZonedDateTime deletedAt;

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public ZonedDateTime getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(ZonedDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}

	@Override
	public String toString() {
		return "SoftDeletable [deleted=" + deleted + ", deletedAt=" + deletedAt + "]";
	}
}
