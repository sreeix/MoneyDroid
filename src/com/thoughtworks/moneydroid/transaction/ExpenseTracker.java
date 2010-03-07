package com.thoughtworks.moneydroid.transaction;

import android.net.Uri;
import android.provider.BaseColumns;

import com.thoughtworks.moneydroid.sms.MoneyDroidSmsMessage;

public class ExpenseTracker {

	public static final String AUTHORITY = "com.thoughtworks.moneydroid.provider.ExpenseTracker";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/expenses");

	private TransactionRepository transactionRepository;

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

	public ExpenseTracker(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public Transaction newExpense(MoneyDroidSmsMessage sms) {

		if (!sms.isPurchase()) {
			return new NullTransaction();
		}

		Transaction transaction = sms.getTransaction();
		transactionRepository.save(transaction);

		return transaction;
	}
}