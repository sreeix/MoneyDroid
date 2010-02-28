package com.thoughtworks.moneydroid.sms.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.moneydroid.sms.MoneyDroidSmsMessage;
import com.thoughtworks.moneydroid.transaction.NullTransaction;
import com.thoughtworks.moneydroid.transaction.Purchase;
import com.thoughtworks.moneydroid.transaction.Transaction;

public class PurchaseSmsHandler implements Handler {

	private static final String DEBIT_PURCHASE_TEMPLATE = "You have done a debit purchase of INR ([0-9,.]*) at ([A-Za-z ]*).Available balance as on ([0-9A-Za-z: ]*) is INR ([0-9,.]*)";
	
	private final MoneyDroidSmsMessage moneyDroidSmsMessage;

	public PurchaseSmsHandler(MoneyDroidSmsMessage moneyDroidSmsMessage) {
		this.moneyDroidSmsMessage = moneyDroidSmsMessage;
	}

	public Transaction createTransaction() {
		if (moneyDroidSmsMessage.isPurchase()) {
			return createPurchase(moneyDroidSmsMessage);
		}

		return new NullTransaction();
	}

	private Transaction createPurchase(MoneyDroidSmsMessage moneyDroidSmsMessage) {
		
		Pattern pattern = Pattern.compile(DEBIT_PURCHASE_TEMPLATE);
		Matcher matcher = pattern.matcher(moneyDroidSmsMessage.getMessage());
		if(!matcher.find())
			throw new InvalidSmsFormatException("Invalid format for debit purchase sms");
		
		String debitedAmount = matcher.group(1);
		String vendor = matcher.group(2);
		String dateOfAvailableBalance = matcher.group(3);
		String availableBalance = matcher.group(4);
		
		return Purchase.Builder.purchaseTransaction().forAmount(debitedAmount).withAvailableBalance(availableBalance).on(dateOfAvailableBalance.trim()).create();
	}
}
