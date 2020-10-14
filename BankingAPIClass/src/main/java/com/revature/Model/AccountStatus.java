package com.revature.Model;

public class AccountStatus {
	
	private int statusId; // primary key 0 = open, 1 = closed
	private String status; // not null, unique 0 = open, 1 = closed
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
