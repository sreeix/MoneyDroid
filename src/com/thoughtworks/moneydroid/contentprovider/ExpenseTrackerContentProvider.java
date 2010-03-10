package com.thoughtworks.moneydroid.contentprovider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.thoughtworks.moneydroid.transaction.ExpenseTracker;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker.Category;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker.Expense;
import com.thoughtworks.moneydroid.transaction.ExpenseTracker.Vendor;

public class ExpenseTrackerContentProvider extends ContentProvider {

	private static final String DATABASE_NAME = "expense_tracker.db";
	private static final String EXPENSES_TABLE_NAME = "transactions";
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private final static int EXPENSES = 1;
	private static final int VENDORS = 2;
	private DatabaseHelper databaseHelper;

	static {
		uriMatcher.addURI(ExpenseTracker.AUTHORITY, "expenses", EXPENSES);
		uriMatcher.addURI(ExpenseTracker.AUTHORITY, "vendors", VENDORS);
	}

	private static final String VENDORS_TABLE_NAME = "vendors";

	public ExpenseTrackerContentProvider() {
		databaseHelper = new DatabaseHelper(getContext());
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		private static final String CATEGORIES_TABLE_NAME = "categories";

		private DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createCategoriesTable(db);
			createVendorsTable(db);
			createExpensesTable(db);
		}

		private void createCategoriesTable(SQLiteDatabase db) {
			db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT)", CATEGORIES_TABLE_NAME, Category._ID, Category._NAME));
		}

		private void createExpensesTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + EXPENSES_TABLE_NAME + "(" + Expense._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Expense._VENDOR_ID + " INTEGER," + Expense._AMOUNT + " REAL,"
					+ Expense._DATE + " INTEGER," + Expense._BALANCE + " REAL);");
		}

		private void createVendorsTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + VENDORS_TABLE_NAME + " (" + Vendor._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Vendor._CATEGORY_ID + " INTEGER NULL references categories (" + Category._ID
					+ ")," + Vendor._NAME + " TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			// Log.w("ExpenseTracker",
			// String.format("Upgrading database from %d to %d", oldVersion,
			// newVersion));
			db.execSQL(String.format("DROP DATABASE IF EXISTS %s", DATABASE_NAME));
			onCreate(db);
		}

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (uriMatcher.match(uri) != EXPENSES) {
			throw new IllegalArgumentException(String.format("Unknown Argument %s", uri));
		}

		// Log.d("MessageSPAM", "preparing to create new expense");
		return notifyObserversAboutTheNewExpense(uri, insertNewExpense(uri, initialValues, findVendor(initialValues)));
	}

	private long findVendor(ContentValues initialValues) {
		HashMap<String, String> projectionMap = new HashMap<String, String>();
		projectionMap.put(Vendor._ID, Vendor._ID);
		projectionMap.put(Vendor._CATEGORY_ID, Vendor._CATEGORY_ID);
		projectionMap.put(Vendor._NAME, Vendor._NAME);

		SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
		sqLiteQueryBuilder.setTables(VENDORS_TABLE_NAME);
		sqLiteQueryBuilder.appendWhere(String.format("%s = '%s'", Vendor._NAME, (String) initialValues.get(Vendor._NAME)));

		SQLiteDatabase readableDatabase = databaseHelper.getWritableDatabase();
		Cursor cursor = sqLiteQueryBuilder.query(readableDatabase, null, null, null, null, null, null);

		if (cursor.getCount() > 1)
			throw new ExpenseTrackerException(String.format("Found %d vendors with name %s", cursor.getCount(), (String) initialValues.get(Vendor._NAME)));

		// if (Log.isLoggable("expenseTracker", Log.DEBUG))
		// Log.d("expenseTracker", String.format("found %d vendors",
		// cursor.getCount()));

		if (cursor.getCount() == 0)
			return insertNewVendor(initialValues);

		cursor.moveToFirst();

		int vendorIdColumnIndex = cursor.getColumnIndex(Vendor._ID);
		long vendorId = cursor.getLong(vendorIdColumnIndex);

		/*
		 * if (Log.isLoggable("expenseTracker", Log.DEBUG))
		 * Log.d("expenseTracker",
		 * String.format("Created new vendor with id %d", vendorId));
		 */
		return vendorId;
	}

	private long insertNewVendor(ContentValues initialValues) {

		SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		String vendorName = initialValues.getAsString(Vendor._NAME);
		contentValues.put(Vendor._NAME, vendorName);

		long insertedVendorId = writableDatabase.insert(VENDORS_TABLE_NAME, null, contentValues);
		if (insertedVendorId <= 0)
			throw new SQLException(String.format("Failed to insert vendor %s", vendorName));

		return insertedVendorId;

	}

	private Uri notifyObserversAboutTheNewExpense(Uri uri, long newExpenseRowId) {
		Uri contentUri = ContentUris.withAppendedId(uri, newExpenseRowId);
		getContext().getContentResolver().notifyChange(contentUri, null);
		return contentUri;
	}

	private long insertNewExpense(Uri uri, ContentValues initialValues, long vendorId) {
		SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(Expense._VENDOR_ID, vendorId);
		contentValues.put(Expense._AMOUNT, initialValues.getAsFloat(Expense._AMOUNT));
		contentValues.put(Expense._BALANCE, initialValues.getAsFloat(Expense._BALANCE));
		contentValues.put(Expense._DATE, initialValues.getAsString(Expense._DATE));

		long rowId = writableDatabase.insert(EXPENSES_TABLE_NAME, "null", contentValues);
		// Log.d("MessageSPAM", String.format("Inserted new expense with id %d",
		// rowId));

		if (rowId <= 0)
			throw new SQLException(String.format("Failed to insert row into %s", uri));

		return rowId;
	}

	@Override
	public boolean onCreate() {
		databaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
		sqLiteQueryBuilder.setTables(EXPENSES_TABLE_NAME);

		SQLiteDatabase readableDatabase = databaseHelper.getReadableDatabase();
/*		HashMap<String, String> projectionMap = new HashMap<String, String>();
		projectionMap.put(Expense._ID, Expense._ID);
		projectionMap.put(Expense._AMOUNT, Expense._AMOUNT);
		projectionMap.put(Expense._BALANCE, Expense._BALANCE);
*/
		Cursor cursor = sqLiteQueryBuilder.query(readableDatabase, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), ExpenseTracker.CONTENT_URI);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
