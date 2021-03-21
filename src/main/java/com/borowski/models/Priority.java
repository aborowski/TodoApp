package com.borowski.models;

public enum Priority {
	LOW {
		@Override
		public Priority previous() {
			return this;
		}
	}, 
	MEDIUM, 
	HIGH, 
	CRITICAL {
		@Override
		public Priority next() {
			return this;
		}
	};
	
	public Priority next() {
		return values()[ordinal() + 1];
	}
	
	public Priority previous() {
		return values()[ordinal() - 1];
	}
}
