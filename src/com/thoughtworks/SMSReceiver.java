package com.thoughtworks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.thoughtworks.moneydroid.sms.SmsFactory;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker;
import com.thoughtworks.moneydroid.transaction.TransactionRepository;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
//		System.setProperty("log.tag.MessageSPAM", "INFO");
//		System.setProperty("log.tag.expenseTracker", "INFO");
//		Log.d("MessageSPAM", "Received some SMS");
		Bundle bundle = intent.getExtras();

//		Log.d("MessageSPAM", "wee");
		if (bundle == null){
//			Log.d("MessageSPAM", "returning cos bundle == null");
			return;
		}

//		Log.d("MessageSPAM", "Not returning");
		
		new ExpenseTracker(new TransactionRepository(context.getContentResolver())).newExpense(makeSms(bundle));

	}

	private com.thoughtworks.moneydroid.sms.MoneyDroidSmsMessage makeSms(Bundle bundle) {
		return SmsFactory.create((Object[]) bundle.get("pdus"));
	}
}