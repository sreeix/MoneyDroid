package com.thoughtworks.moneydroid.transaction;

import java.util.Date;

public class NullTransaction extends Transaction {

	@Override
	public Money amount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Money availableBalance() {
		return new Money(-1234);
	}

	@Override
	public Date date() {
		return new Date();
	}

}
