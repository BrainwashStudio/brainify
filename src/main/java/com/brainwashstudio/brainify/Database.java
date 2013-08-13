package com.brainwashstudio.orm;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.brainwashstudio.orm.util.TypeUtils;

import java.lang.reflect.Constructor;

public class Database extends SQLiteOpenHelper {

    protected final Context mContext;

    protected DatabaseFactory mFactory;

    public Database(DatabaseFactory factory, Context context) {
        super(context, factory.getDatabaseName(), null, factory.getDatabaseVersion());
        mContext = context;
        mFactory = factory;
    }

    public DatabaseFactory getFactory() {
        return mFactory;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mFactory.createTables(db);

        DatabaseService.setDatabaseCreated(mContext);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // implement migration/upgrade logic


    }
}
