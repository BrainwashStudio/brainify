package com.brainwashstudio.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.brainwashstudio.orm.annotation.Entity;
import com.brainwashstudio.orm.annotation.Id;
import com.brainwashstudio.orm.annotation.NotNull;
import com.brainwashstudio.orm.annotation.Unique;
import com.brainwashstudio.orm.statement.CreateTable;
import com.brainwashstudio.orm.util.TypeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class DatabaseFactory {

    protected String mDatabaseName;
    protected int mDatabaseVersion;
    protected boolean mEnableQueryLogging;

    protected Collection<Class<?>> mModelClasses = new ArrayList<Class<?>>();

    public DatabaseFactory(String databaseName, int databaseVersion, boolean enableQueryLogging) {
        mDatabaseName = databaseName;
        mDatabaseVersion = databaseVersion;
        mEnableQueryLogging = enableQueryLogging;
    }

    public String getDatabaseName() {
        return mDatabaseName;
    }

    public void setDatabaseName(String databaseName) {
        mDatabaseName = databaseName;
    }

    public int getDatabaseVersion() {
        return mDatabaseVersion;
    }

    public void setDatabaseVersion(int databaseVersion) {
        mDatabaseVersion = databaseVersion;
    }

    public boolean isEnableQueryLogging() {
        return mEnableQueryLogging;
    }

    public void setEnableQueryLogging(boolean enableQueryLogging) {
        mEnableQueryLogging = enableQueryLogging;
    }

    public <T> T construct(Class<T> type) {
        Constructor<T> constructor = TypeUtils.getNoArgConstructor(type);
        return TypeUtils.newInstance(constructor);
    }

    @SuppressWarnings("unchecked")
    public <T extends Collection<?>> T constructCollection(Class<T> type, int size) {
        if((Class<?>) type == List.class || (Class<?>) type == Collection.class) {
            return (T) new ArrayList<Object>(size);
        } else if((Class<?>) type == Set.class) {
            return (T) new HashSet<Object>((int)(size * 1.5));
        } else if((Class<?>) type == SortedSet.class) {
            return (T) new TreeSet<Object>();
        } else {
            return construct(type);
        }
    }

    public Database begin(Context context) {
        return new Database(this, context);
    }

    public void register(Class modelClass) {
        if(!modelClass.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Model has to be annotated with @Entity!");
        }

        mModelClasses.add(modelClass);
    }

    public void createTables(SQLiteDatabase db) {
        for(Class<?> modelClass : mModelClasses) {
            createTable(db, modelClass);
        }
    }

    public void createTable(SQLiteDatabase db, Class<?> modelClass) {
        String tableName = deCamelize(modelClass.getSimpleName());
        CreateTable createTable = new CreateTable();
        createTable.setTableName(tableName);
        Field[] fields = modelClass.getDeclaredFields();
        boolean idDeclared = false;
        for(Field field : fields) {
            if(!TypeUtils.isOfInterest(field)) {
                continue;
            }

            ColumnDef columnDef = new ColumnDef();
            columnDef.setName(deCamelize(field.getName()));

            Class<?> fieldType = field.getType();
            Class<? extends TypeName> affinityClass = TypeUtils.affinityFromType(fieldType);

            TypeName affinity = construct(affinityClass);
            columnDef.setTypeName(affinity);

            if(field.isAnnotationPresent(Id.class)) {
                if(idDeclared) {
                    throw new IllegalStateException("Entity can only have one field with @Id annotation!");
                }

                idDeclared = true;

                Id id = field.getAnnotation(Id.class);

                ColumnConstraint.PrimaryKey primaryKey = new ColumnConstraint.PrimaryKey();
                primaryKey.setAutoIncrement(id.autoIncrement());
                primaryKey.setName(columnDef.getName() + "_primary");
                columnDef.addColumnConstraint(primaryKey);
            }
            if(field.isAnnotationPresent(NotNull.class)) {
                ColumnConstraint.NotNull notNull = new ColumnConstraint.NotNull();
                notNull.setName(columnDef.getName() + "_notnull");
                columnDef.addColumnConstraint(notNull);
            }
            if(field.isAnnotationPresent(Unique.class)) {
                ColumnConstraint.Unique unique = new ColumnConstraint.Unique();
                unique.setName(columnDef.getName() + "_unique");
                columnDef.addColumnConstraint(unique);
            }

            createTable.addColumnDef(columnDef);
        }

        createTable.run(db);
    }

    public String deCamelize(String camelCase) {
        camelCase = camelCase.replaceAll("(.)([A-Z][a-z]+)", "$1_$2");
        camelCase = camelCase.replaceAll("([a-z0-9])([A-Z])", "$1_$2");
        return camelCase.toLowerCase();
    }

}
