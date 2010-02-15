package com.thoughtworks.moneydroid.transaction;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

public class Purchase extends Transaction {

	private BigDecimal debitedAmount;
	private BigDecimal availableBalance;
	private Vendor vendor;
	public Date date;

	private Purchase() {
	}

	public Money availableBalance() {
		return new Money(availableBalance.doubleValue());
	}

	@Override
	public Money amount() {
		return new Money(debitedAmount.doubleValue());
	}

	public static class Builder {

		private static Purchase purchase;

		public static Builder purchaseTransaction() {
			purchase = new Purchase();
			return new Builder();
		}

		public Builder forAmount(String debitedAmount) {
			purchase.debitedAmount = new BigDecimal(debitedAmount);
			return this;
		}

		public Purchase create() {
			return purchase;
		}

		public Builder withAvailableBalance(String availableBalance) {
			purchase.availableBalance = new BigDecimal(availableBalance);
			return this;
		}

		public Builder withVendor(Vendor vendor) {
			purchase.vendor = vendor;
			return this;
		}

		public Builder on(String dateOfAvailableBalance) {
			try {
				purchase.date = java.text.DateFormat.getDateInstance().parse(dateOfAvailableBalance);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return this;
		}
	}
}
