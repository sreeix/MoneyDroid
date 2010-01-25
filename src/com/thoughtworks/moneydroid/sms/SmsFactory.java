package com.thoughtworks.moneydroid.sms;

import android.telephony.gsm.SmsMessage;

public class SmsFactory {

	public static com.thoughtworks.moneydroid.sms.MoneyDroidSmsMessage create(Object[] pdus) {
		if (pdus == null || pdus.length == 0)
			return null;

		SmsMessage sms = android.telephony.gsm.SmsMessage.createFromPdu((byte[]) pdus[0]);
		return new com.thoughtworks.moneydroid.sms.MoneyDroidSmsMessage(sms);
	}
}