package com.brainwashstudio.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class FieldProperty extends AbstractProperty {

    private Field mField;

    public FieldProperty(DatabaseFactory factory, Class<?> examinedClass, Field field) {
        super(field.getName(), field.getAnnotations());

        mField = field;

        field.setAccessible(true);

        // TODO add @Index and @Unindex support

        // TODO add @IgnoreSave support
    }

    @Override
    public Type getType() {
        return mField.getGenericType();
    }

    @Override
    public Object get(Object model) {
        try {
            return mField.get(model);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(Object model, Object value) {
        try {
            mField.set(model, value);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
