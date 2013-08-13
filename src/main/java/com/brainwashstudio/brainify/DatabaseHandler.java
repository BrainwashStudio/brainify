package com.brainwashstudio.orm;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.brainwashstudio.orm.annotation.Entity;
import com.brainwashstudio.orm.table.LevelTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "databaseHandler";
	public static final String TABLE_SINGLEPLAYER_LEVELS = "singleplayer_levels"; 
	
	public static final HashMap<Class, ITable> TABLES = new HashMap<Class, ITable>(); 



	static {
		registerTable(new LevelTable());
	}

	public static void registerTable(ITable table) {
		Class cls = table.getModelClass();
		
		TABLES.put(cls, table);
	}

    public static void register(Class modelClass) {
        if(!modelClass.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Model has to be annotated with @Entity!"); // TODO make our own exceptions
        }
    }
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		for(Entry<Class, ITable> entry: TABLES.entrySet()) {
			entry.getValue().onCreate(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(Entry<Class, ITable> entry: TABLES.entrySet()) {
			entry.getValue().onUpgrade(db, oldVersion, newVersion);
		}
	}
	
	public <T extends IModel> ITable<T> findTable(T data) {
		return findTable((Class<T>) data.getClass());
	}
	
	public <T extends IModel> ITable<T> findTable(Class<T> cls) {
		if(TABLES.containsKey(cls)) {
			return TABLES.get(cls);
		} else {
			throw new RuntimeException("Table not registered!");
		}
	}
	
	public <T extends IModel> void addEntry(T data) {
		SQLiteDatabase db = getWritableDatabase();
		ITable<T> table = findTable(data);
		table.addEntry(db, data);
		db.close();
	}
	
	public <T extends IModel> T readEntry(Class<T> cls, int id) {
		SQLiteDatabase db = getReadableDatabase();
		ITable<T> table = findTable(cls);
		T data = table.readEntry(db, id);
		db.close();
		return data;
	}
	
	public <T extends IModel> List<T> readAllEntries(Class<T> cls) {
		SQLiteDatabase db = getReadableDatabase();
		ITable<T> table = findTable(cls);
		List<T> data = table.readAllEntries(db);
		db.close();
		return data;
	}
	
	public <T extends IModel> int updateEntry(T data) {
		SQLiteDatabase db = getWritableDatabase();
		ITable<T> table = findTable(data);
		int output = table.updateEntry(db, data);
		db.close();
		return output;
	}
	
	public <T extends IModel> void deleteEntry(T data) {
		SQLiteDatabase db = getWritableDatabase();
		ITable<T> table = findTable(data);
		table.deleteEntry(db, data);
		db.close();
	}
	
	public <T extends IModel> void deleteEntry(Class<T> cls, int id) {
		SQLiteDatabase db = getWritableDatabase();
		ITable<T> table = findTable(cls);
		table.deleteEntry(db, id);
		db.close();
	}

}
