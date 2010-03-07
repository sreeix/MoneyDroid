package com.thoughtworks.moneydroid.transaction;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.moneydroid.contentprovider.ExpenseTrackerException;

public class Purchase extends Transaction {

	private Number debitedAmount;
	private Number availableBalance;
	private Vendor vendor;
	public Date date;

	private Purchase() {
	}

	@Override
	public Money availableBalance() {
		return new Money(availableBalance.doubleValue());
	}

	@Override
	public Money amount() {
		return new Money(debitedAmount.doubleValue());
	}

	@Override
	public Date date() {
		return date;
	}

	@Override
	public Vendor vendor() {
		return vendor;
	}

	public static class Builder {

		private static Purchase purchase;
		private NumberFormat decimalFormat = new DecimalFormat("###,###");

		public static Builder purchaseTransaction() {
			purchase = new Purchase();
			return new Builder();
		}

		public Builder forAmount(String debitedAmount) {
			try {
				purchase.debitedAmount = decimalFormat.parse(debitedAmount);
				return this;
			} catch (ParseException e) {
				throw new ExpenseTrackerException(String.format("Failed to parse debited amount %s", debitedAmount));
			}
		}

		public Purchase create() {
			return purchase;
		}

		public Builder withAvailableBalance(String availableBalance) {
			try {
				purchase.availableBalance = decimalFormat.parse(availableBalance);
				return this;
			} catch (ParseException e) {
				throw new ExpenseTrackerException(String.format("Failed to parse available balance %s", availableBalance));
			}
		}

		public Builder withVendor(Vendor vendor) {
			purchase.vendor = vendor;
			return this;
		}

		public Builder on(String dateOfAvailableBalance) {
			try {
				purchase.date = new SimpleDateFormat("MMM dd yyyy hh:mma").parse(dateOfAvailableBalance.trim());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return this;
		}

		public Builder fromVendor(String vendor) {
			return withVendor(new Vendor(vendor));
		}
	}

}
