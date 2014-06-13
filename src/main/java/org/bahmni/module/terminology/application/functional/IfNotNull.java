package org.bahmni.module.terminology.application.functional;

public class IfNotNull<T> {

    private T value;

    public IfNotNull(T value) {
        this.value = value;
    }

    public static <T> IfNotNull<T> ifNotNull(T value) {
        return new IfNotNull<>(value);
    }

    public void apply(Lambda<T> task) {
        if (null != value) {
            task.apply(value);
        }
    }
}
