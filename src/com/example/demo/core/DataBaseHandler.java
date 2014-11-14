package com.example.demo.core;

import java.util.Calendar;
import java.util.Date;

import com.example.demo.bean.vozUserBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "vozDB";

	// Settings table name
	private static final String TABLE_SETTINGS = "settings";
	private static final String TABLE_POST = "post";

	// Commun Table Columns names
	private static final String KEY_ID = "id";

	// Doujin Table Columns names
	private static final String KEY_TITLE = "title";

	// Settings Table Columns names
	private static final String KEY_USER = "user";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_CHECKED = "checked";

	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_SETTINGS = "CREATE TABLE " + TABLE_SETTINGS + "("
				+ KEY_ID + " TEXT PRIMARY KEY," + KEY_USER + " TEXT,"
				+ KEY_PASSWORD + " TEXT" + "," + KEY_CHECKED + " INTEGER" + ")";

		String CREATE_TABLE_POST = "CREATE TABLE " + TABLE_POST + "("
				+ KEY_ID + " TEXT PRIMARY KEY," + KEY_TITLE + " TEXT"
				+ ")";

		db.execSQL(CREATE_TABLE_SETTINGS);
		db.execSQL(CREATE_TABLE_POST);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);

		// Create tables again
		onCreate(db);

	}

	public vozUserBean addSetting() {
		vozUserBean bean = new vozUserBean();
		bean.setUser("");
		bean.setPassword("");
		bean.setChecked(false);

		Calendar c = Calendar.getInstance();

		int random = (int) (Math.random() * 7 + 3);
		c.add(Calendar.DATE, random);
		Date dateMessageHelp = c.getTime();

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_USER, bean.getUser());
		values.put(KEY_PASSWORD, bean.getPassword());
		values.put(KEY_CHECKED, bean.isChecked() ? 1 : 0);

		// Inserting Row
		db.insert(TABLE_SETTINGS, null, values);
		db.close(); // Closing database connection

		return bean;
	}

	public vozUserBean getSetting() {
		vozUserBean result = null;
		// Select All Query
		String selectQuery = "SELECT " + KEY_ID + "," + KEY_USER + ","
				+ KEY_PASSWORD + "," + KEY_CHECKED + " FROM " + TABLE_SETTINGS;

		Log.i(this.getClass().toString(), selectQuery);

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			result = new vozUserBean();
			result.setUser(cursor.getString(1));
			result.setPassword(cursor.getString(2));
			result.setChecked(cursor.getInt(3) == 1);
		}

		db.close();
		// return contact list
		return result;
	}

	public void updateSetting(vozUserBean bean) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_USER, bean.getUser());
		values.put(KEY_PASSWORD, bean.getPassword());
		values.put(KEY_CHECKED, bean.isChecked() ? 1 : 0);

		// Inserting Row
		db.update(TABLE_SETTINGS, values, "1=1", new String[] {});
		db.close(); // Closing database connection
	}

}
