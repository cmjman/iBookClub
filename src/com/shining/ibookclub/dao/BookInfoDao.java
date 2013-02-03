package com.shining.ibookclub.dao;





import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shining.ibookclub.bean.BookBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class BookInfoDao {

	private static BookInfoDao dao;

	private SQLiteDatabase database;

	public BookInfoDao(Context context) {
		this.database = context.openOrCreateDatabase("book",
				Context.MODE_PRIVATE, null);
		this.createTables();
	}

	private void dropTables() {
		database.execSQL("drop table favorite_books;");
	}

	private void createTables() {
		database.execSQL("create table if not exists favorite_books("
				+ " _id integer primary key autoincrement," + " name text,"
				+ "isbn text," + "image text"+");");
	}

	public void deleteAll() {
		SQLiteStatement statement = database
				.compileStatement("delete from favorite_books");
		statement.execute();
		statement.close();
	}

	public void create(BookBean bookInfo) {
		
		/*
		SQLiteStatement statement = database
				.compileStatement("insert into favorite_books(name,isbn) values(?,?)");
		statement.bindString(1, bookInfo.getName());
		statement.bindString(2, bookInfo.getIsbn());
		statement.execute();
		statement.close();
		*/
		
		 ContentValues cv = new ContentValues();  
		 
		 cv.put("name", bookInfo.getBookname());
		 cv.put("isbn",bookInfo.getIsbn());
		 cv.put("image", bookInfo.getBookcover_url());
		
		 database.insert("favorite_books", null, cv);
	}

	public JSONArray list() {
		JSONArray array = new JSONArray();

		Cursor cursor = database.rawQuery("select name,isbn,image from favorite_books",
				new String[] {});

		while (cursor.moveToNext()) {
			JSONObject object = new JSONObject();
			try {
				object.put("name", cursor.getString(0));
				object.put("isbn", cursor.getString(1));
				object.put("image",cursor.getString(2));
				
			//	System.out.println(cursor.getString(2));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			array.put(object);
		}

		cursor.close();

		return array;
	}
	
	public Cursor query(String sql,String[] args){
		
		Cursor cursor=database.rawQuery(sql, args);
		return cursor;
	}

	public void delete(String isbn) {
		SQLiteStatement statement = database
				.compileStatement("delete from favorite_books where isbn=?");
		statement.bindString(1, isbn);
		statement.execute();
		statement.close();
	}

	public BookBean get(String isbn) {
		BookBean bookInfo = null;
		Cursor cursor = database.rawQuery(
				"select name from favorite_books where isbn=" + isbn,
				new String[] {});
		if (cursor.moveToFirst()) {
			bookInfo = new BookBean();
			bookInfo.setBookname(cursor.getString(0));
			bookInfo.setIsbn(isbn);
		}
		cursor.close();
		return bookInfo;
	}

	public static void initBookInfoDao(Context context) {
		if (dao == null) {
			dao = new BookInfoDao(context);
		}
	}

	public static BookInfoDao getInstance() {
		return dao;
	}

}
