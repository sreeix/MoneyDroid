package com.thoughtworks.moneydroid.sms;

import android.telephony.gsm.SmsMessage;

import com.thoughtworks.moneydroid.sms.handlers.PurchaseSmsHandler;
import com.thoughtworks.moneydroid.transaction.NullTransaction;
import com.thoughtworks.moneydroid.transaction.Transaction;

public final class MoneyDroidSmsMessage {

	private final SmsMessage sms;

	private enum SMS_SENDER {
		DM_STANDARD_CHARTERED("DM-StanChrt"), TA_STANDARD_CHARTERED("TA-StanChrt"),HACK_FOR_TESTING("1234"), MY_NUMBER("9880551181"), MY_NUMBER_WITH_COUNTRY_CODE("+919880551181");

		private final String name;

		SMS_SENDER(String name) {
			this.name = name;
		}

		public boolean isSenderOf(SmsMessage sms) {
			return name.equals(sms.getOriginatingAddress());
		}
	}

	public MoneyDroidSmsMessage(android.telephony.gsm.SmsMessage sms) {
		this.sms = sms;
	}

	public final boolean isFromMyBank() {
		return SMS_SENDER.DM_STANDARD_CHARTERED.isSenderOf(sms) || SMS_SENDER.TA_STANDARD_CHARTERED.isSenderOf(sms) || SMS_SENDER.HACK_FOR_TESTING.isSenderOf(sms) || SMS_SENDER.MY_NUMBER.isSenderOf(sms) || SMS_SENDER.MY_NUMBER_WITH_COUNTRY_CODE.isSenderOf(sms);
	}

	public final boolean isAWithdrawal() {
		return isFromMyBank() && sms.getMessageBody().startsWith("You have withdrawn");
	}

	public final boolean isNotAWithdrawal() {
		return !isAWithdrawal();
	}

	public Transaction getTransaction() {
		if (isPurchase())
			return new PurchaseSmsHandler(this).createTransaction();
		
		if (isAWithdrawal())
			return new Withdrawal();

		return new NullTransaction();
	}

	public boolean isPurchase() {
		return isFromMyBank() && sms.getMessageBody().startsWith("You have done a debit purchase");
	}

	public String getMessage() {
		return sms.getMessageBody();
	}
}