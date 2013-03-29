package com.example.myapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
@Athresh
**/
public class DBHelper extends SQLiteOpenHelper {

	static final String dbName = "ItemsDB";
	static final String ItemsTable = "tItems";
	static final String colID = "ItemsID";
	
	static final String colHandle = "Name";
	static final String colText = "TweetText";
	static final String colImgUrl = "ImgUrl";
	static final String colLocation = "Location";

	public DBHelper(Context context) {
		super(context, dbName, null, 33);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + ItemsTable + " (" + colID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + colHandle
				+ " TEXT, " + colText + " TEXT, "
				+ colImgUrl + " TEXT, " + colLocation + " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + ItemsTable);
		onCreate(db);

	}
	
	public void refreshDB() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + ItemsTable);
		onCreate(db);
	}
	
	void saveItem(Item newItem) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put(colHandle, newItem.getHd());
		cv.put(colText, newItem.getText());
		cv.put(colImgUrl, newItem.getImgUrl());
		cv.put(colLocation, newItem.getLtn());

		db.insert(ItemsTable, null, cv);
		db.close();

	}

	List<Item> getAllItems() {
		List<Item> allItems = new ArrayList<Item>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + ItemsTable, null);

		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			Item Item = cursorToItem(cur);
			allItems.add(Item);
			cur.moveToNext();
		}

		cur.close();
		return allItems;

	}

	List<Item> getFilteredItems(String filter) {
		List<Item> allItems = new ArrayList<Item>();

		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * FROM " + ItemsTable + " Where "
				+ colLocation + " like '%" + filter + "%'";

		Cursor cur = db.rawQuery(query, null);

		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			Item Item = cursorToItem(cur);
			allItems.add(Item);
			cur.moveToNext();
		}

		cur.close();
		return allItems;

	}

	private Item cursorToItem(Cursor cursor) {
		Item Item = new Item();
		Item.setHd(cursor.getString(1));
		Item.setText(cursor.getString(2));
		Item.setImgUrl(cursor.getString(3));
		Item.setLtn(cursor.getString(4));
		return Item;
	}

	void deletItem(String handle) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(ItemsTable, colHandle + "=?",	new String[] { handle });
		db.close();
	}
}
