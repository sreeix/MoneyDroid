package com.thoughtworks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.thoughtworks.moneydroid.sms.SmsFactory;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Log.i("SMS", "Received some SMS");
		Bundle bundle = intent.getExtras();

		if (bundle == null)
			return;

		new ExpenseTracker().newExpense(makeSms(bundle));

	}

	private com.thoughtworks.moneydroid.sms.MoneyDroidSmsMessage makeSms(Bundle bundle) {
		return SmsFactory.create((Object[]) bundle.get("pdus"));
	}
}