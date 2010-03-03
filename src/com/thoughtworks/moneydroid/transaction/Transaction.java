package com.thoughtworks.moneydroid.transaction;

import java.util.Date;

public abstract class Transaction {

	public abstract Money amount();

	public abstract Money availableBalance();

	public abstract Date date();

}