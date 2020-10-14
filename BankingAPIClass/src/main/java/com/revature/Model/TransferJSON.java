package com.revature.Model;

public class TransferJSON {

	private int sourceAccountId;
	private int targetAccountId;
	private double amount;
	
	public TransferJSON() {
		// TODO Auto-generated constructor stub
	}

	public int getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(int sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTargetAccountId() {
		return targetAccountId;
	}

	public void setTargetAccountId(int targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

}
