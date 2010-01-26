package com.thoughtworks.moneydroid.transaction;

public class Purchase extends Transaction {

	private final String debitedAmount;
	private final String availableBalance;

	public Purchase(String debitedAmount, String availableBalance) {
		this.debitedAmount = debitedAmount;
		this.availableBalance = availableBalance;
	}

	@Override
	public String amount() {
		return debitedAmount;
	}

}
