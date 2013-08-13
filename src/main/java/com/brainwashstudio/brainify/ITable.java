package com.brainwashstudio.orm;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

public interface ITable<T extends IModel> {
	public Class<T> getModelClass();
	
	public String getTableName();
	
	public void onCreate(SQLiteDatabase db);
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	
	public void addEntry(SQLiteDatabase db, T data);
	
	public T readEntry(SQLiteDatabase db, int id);
	
	public List<T> readAllEntries(SQLiteDatabase db);
	
	public int updateEntry(SQLiteDatabase db, T data);
	
	public void deleteEntry(SQLiteDatabase db, T data);
	
	public void deleteEntry(SQLiteDatabase db, int id);
}
