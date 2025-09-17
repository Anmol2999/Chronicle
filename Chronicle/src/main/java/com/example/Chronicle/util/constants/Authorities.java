package com.example.Chronicle.util.constants;

public enum Authorities {
	RESET_ANY_USER_PASSWORD(1L, "RESET_ANY_USER_PASSWORD"),
	ACCESS_ADMIN_DASHBOARD(2L, "ACCESS_ADMIN_DASHBOARD");

	private final Long authorityid;
	private final String authorityString;

	Authorities(Long authorityid, String authorityString) {
		this.authorityid = authorityid;
		this.authorityString = authorityString;
	}

	public Long getAuthorityId() {
		return authorityid;
	}

	public String getAuthorityString() {
		return authorityString;
	}
}
