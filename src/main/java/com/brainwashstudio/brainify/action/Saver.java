package com.brainwashstudio.orm.action;

import android.database.sqlite.SQLiteDatabase;
import com.brainwashstudio.orm.Database;
import com.brainwashstudio.orm.Key;
import com.brainwashstudio.orm.Result;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class Saver {

    protected Database mDatabase;

    public Saver(Database database) {
        mDatabase = database;
    }

    public <E> Result<Map<Key<E>, E>> entities(E... entities) {
        return entities(Arrays.asList(entities));
    }

    public <E> Result<Map<Key<E>, E>> entities(Iterable<E> entities) {
        SQLiteDatabase sqLiteDatabase = mDatabase.getWritableDatabase();



    return null;
//        return mDatabase.createWriteEngine().<E>save(entities);
    }

    public <E> Result<Key<E>> entity(E entity) {
        Result<Map<Key<E>, E>> base = entities(Collections.singleton(entity));

        return null;
    }

}
