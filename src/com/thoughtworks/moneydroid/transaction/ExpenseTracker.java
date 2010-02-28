package com.thoughtworks.moneydroid.transaction;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.thoughtworks.moneydroid.contentprovider.ExpenseTrackerContentProvider;
import com.thoughtworks.moneydroid.sms.MoneyDroidSmsMessage;

public class ExpenseTracker {

	public static final String AUTHORITY = "com.thoughtworks.moneydroid.provider.ExpenseTracker";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/expenses");

	private final Context context;

	public static final class Expense implements BaseColumns {

		private Expense() {
		}

		public static final String _AMOUNT = "amount";
		public static final String _DATE = "date";
		public static final String _BALANCE = "balance";
		public static final String _VENDOR_ID = "vendor_id";

	}

	public static final class Vendor implements BaseColumns {
		public static final String _CATEGORY_ID = "category_id";
		public static final String _NAME = "name";
	}

	public static final class Category implements BaseColumns {
		public static final String _NAME = "name";
	}

	public ExpenseTracker(Context context) {
		this.context = context;
	}

	public Transaction newExpense(MoneyDroidSmsMessage sms) {

		if (!sms.isPurchase()) {
			Log.d("MessageSPAM", "Is not a purchase");
			return new NullTransaction();
		}

		Transaction transaction = sms.getTransaction();

		ContentValues contentValues = new ContentValues();
		contentValues.put(Expense._AMOUNT, String.valueOf(transaction.amount()));
		contentValues.put(Expense._BALANCE, String.valueOf(transaction.availableBalance()));
		contentValues.put(Vendor._NAME, "Dummy Vendor");

		context.getContentResolver().insert(ExpenseTracker.CONTENT_URI, contentValues);

		return transaction;
	}

}
