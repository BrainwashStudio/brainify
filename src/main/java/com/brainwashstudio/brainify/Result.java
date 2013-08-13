package com.brainwashstudio.orm;

public class Result<T> {

    public T now() {
        return null;
    }

    public void async() {
        async(null);
    }

    public void async(OnResultReadyListener<T> listener) {

    }

    public interface OnResultReadyListener<T> {
        public void onResultReady(T result);
    }

}
