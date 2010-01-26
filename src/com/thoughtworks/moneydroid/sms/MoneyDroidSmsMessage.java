package com.thoughtworks.moneydroid.sms;

import android.telephony.gsm.SmsMessage;

import com.thoughtworks.moneydroid.sms.handlers.PurchaseSmsHandler;
import com.thoughtworks.moneydroid.transaction.NullTransaction;
import com.thoughtworks.moneydroid.transaction.Transaction;

public final class MoneyDroidSmsMessage {

	private final SmsMessage sms;
	
	private enum SMS_SENDER{
		STANDARD_CHARTERED("DM-StanChrt");
		
		private final String name;

		SMS_SENDER(String name){
			this.name = name;
		}

		public boolean isSenderOf(SmsMessage sms) {
			return name.equals(sms.getDisplayOriginatingAddress());
		}
	}

	public MoneyDroidSmsMessage(android.telephony.gsm.SmsMessage sms) {
		this.sms = sms;
	}
	
	public final boolean isFromMyBank() {
		return SMS_SENDER.STANDARD_CHARTERED.isSenderOf(sms);
	}

	public final boolean isAWithdrawal() {
		return isFromMyBank() && sms.getDisplayMessageBody().startsWith("You have withdrawn");
	}

	public final boolean isNotAWithdrawal() {
		return !isAWithdrawal();
	}

	public Transaction getTransaction() {
		if(isAWithdrawal())
			return new Withdrawal();
		
		if(isPurchase())
			return new PurchaseSmsHandler(this).createTransaction();
		
		return new NullTransaction();
	}

	public boolean isPurchase() {
		return isFromMyBank() && sms.getDisplayMessageBody().startsWith("You have done a debit purchase");
	}

	public String getMessage() {
		return sms.getDisplayMessageBody();
	}
}