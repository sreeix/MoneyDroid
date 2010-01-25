package com.thoughtworks.moneydroid.sms;

import android.telephony.gsm.SmsMessage;

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

	public final boolean isAWithdrawalTransactionMessage() {
		return isFromMyBank() && sms.getDisplayMessageBody().startsWith("You have withdrawn");
	}
}