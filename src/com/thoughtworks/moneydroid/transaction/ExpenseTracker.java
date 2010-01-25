package com.thoughtworks.moneydroid.transaction;

import com.thoughtworks.moneydroid.sms.MoneyDroidSmsMessage;

public class ExpenseTracker {

	public Transaction newExpense(MoneyDroidSmsMessage sms) {

		if(sms.isNotAWithdrawal())
			return new NullTransaction();
		
		return new NullTransaction();
	}

}
