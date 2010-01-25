package com.thoughtworks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Log.i("SMS", "Received some SMS");
		Bundle bundle = intent.getExtras();

		if (bundle == null)
			return;

		final String str = makeSms(bundle);
		Log.i("SMSAPP", str);
	
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();

	}

	private String makeSms(Bundle bundle) {
		Object[] pdus = (Object[]) bundle.get("pdus");
		StringBuilder str = new StringBuilder();
		
		for (int i = 0; i < pdus.length; i++) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
			str.append("SMS from " + sms.getOriginatingAddress()).append(" :").append(sms.getMessageBody().toString()).append("\n");
		}
		return str.toString();
	}

}
