package com.thoughtworks.moneydroid.sms.handlers;

import com.thoughtworks.moneydroid.transaction.Transaction;

public interface Handler {
	public Transaction createTransaction();
}
