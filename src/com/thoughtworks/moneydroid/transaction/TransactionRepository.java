package com.thoughtworks.moneydroid.transaction;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.thoughtworks.moneydroid.transaction.ExpenseTracker.Expense;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker.Vendor;

public class TransactionRepository {

	private final ContentResolver contentResolver;

	public TransactionRepository(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	public void save(Transaction transaction) {
		ContentValues contentValues = populateValues(transaction);
		contentResolver.insert(ExpenseTracker.CONTENT_URI, contentValues);
		
	}

	private ContentValues populateValues(Transaction transaction) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(Expense._AMOUNT, String.valueOf(transaction.amount()));
		contentValues.put(Expense._BALANCE, String.valueOf(transaction.availableBalance()));
		contentValues.put(Expense._DATE, String.valueOf(transaction.date()));
		contentValues.put(Vendor._NAME, transaction.vendor().toString());
		
		return contentValues;
	}

}
