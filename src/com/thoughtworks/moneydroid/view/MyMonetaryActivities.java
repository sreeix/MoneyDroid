package com.thoughtworks.moneydroid.view;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.thoughtworks.R;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker.Expense;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker.Vendor;

public class MyMonetaryActivities extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/*
		 * TextView myMonetaryViews = (TextView)
		 * findViewById(R.id.my_monetary_view);
		 * 
		 * String[] projections = new String[] { Expense._DATE, Expense._AMOUNT,
		 * Expense._BALANCE, Expense._VENDOR_ID, Vendor._NAME }; Cursor cursor =
		 * managedQuery(ExpenseTracker.CONTENT_URI, projections, null, null,
		 * Expense._DATE + " DESC");
		 * 
		 * if (cursor.moveToFirst()) {
		 * myMonetaryViews.append(String.format("found %d transactions:"
		 * ,cursor.getCount()));
		 * myMonetaryViews.append("-----------------------------------------");
		 * do { myMonetaryViews.append(String.format("Date:%s|",
		 * cursor.getString(cursor.getColumnIndex(Expense._DATE))));
		 * myMonetaryViews.append(String.format(" Amount:%s\n",
		 * cursor.getString(cursor.getColumnIndex(Expense._AMOUNT))));
		 * myMonetaryViews.append(String.format(" Vendor:%s\n",
		 * cursor.getString(cursor.getColumnIndex(Vendor._NAME)))); } while
		 * (cursor.moveToNext()); }
		 */
		showAsList();
	}

	private void showAsList() {
		String[] projections = new String[] { Expense._EXPENSE_ID_WITH_TABLE, Expense._DATE, Expense._AMOUNT, Expense._BALANCE, Expense._VENDOR_ID, Vendor._NAME };
		Cursor cursor = managedQuery(ExpenseTracker.CONTENT_URI, projections, null, null, Expense._DATE + " DESC");

		// ListView listView =
		// (ListView)findViewById(R.id.my_monetary_list_view);

		final String[] COUNTRIES = new String[] { "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
				"Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina",
				"Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cote d'Ivoire", "Cambodia",
				"Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros",
				"Congo", "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
				"East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland",
				"Former Yugoslav Republic of Macedonia", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
				"Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary",
				"Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
				"Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
				"Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar",
				"Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea",
				"Northern Marianas", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico",
				"Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon",
				"Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
				"South Africa", "South Georgia and the South Sandwich Islands", "South Korea", "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden",
				"Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
				"Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States",
				"United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara", "Yemen", "Yugoslavia",
				"Zambia", "Zimbabwe" };
		// setListAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, COUNTRIES));
		// listView.addHeaderView(textView);
		// listView.addTouchables(arrayList);

		setListAdapter(new ResourceCursorAdapter(this, android.R.layout.simple_list_item_1, cursor) {

			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				view.addTouchables(expenses(context, cursor));
			}

			private ArrayList<View> expenses(Context context, Cursor cursor) {
				ArrayList<View> arrayList = new ArrayList<View>();
				TextView textView = new TextView(context);
				textView.setText("Foo Bar");
				arrayList.add(textView);
				while (cursor.moveToNext()) {
					arrayList.add(oneExpense(context, cursor.getString(cursor.getColumnIndex(Vendor._NAME))));
				}
				return arrayList;
			}

			private TextView oneExpense(Context context, String vendor) {
				TextView textView = new TextView(context);
				textView.setText(vendor);
				return textView;
			}
		});
		getListView().setTextFilterEnabled(true);
	}
}