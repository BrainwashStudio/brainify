package com.brainwashstudio.orm;

import com.brainwashstudio.orm.util.TypeUtils;

import java.lang.annotation.Annotation;

public abstract class AbstractProperty implements Property {

    private String mName;
    private Annotation[] mAnnotations;

    public AbstractProperty(String name, Annotation[] annotations) {
        mName = name;
        mAnnotations = annotations;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return TypeUtils.getAnnotation(annotationClass, mAnnotations);
    }


}
