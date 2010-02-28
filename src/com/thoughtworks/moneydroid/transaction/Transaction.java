package com.thoughtworks.moneydroid.transaction;

public abstract class Transaction {

	public abstract Money amount();

	public abstract Money availableBalance(); 

}
