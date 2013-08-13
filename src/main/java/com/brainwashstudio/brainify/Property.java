package com.brainwashstudio.orm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface Property {

    public String getName();

    public <A extends Annotation> A getAnnotation(Class<A> annotationClass);

    public Type getType();

    public void set(Object model, Object value);

    public Object get(Object model);

}
