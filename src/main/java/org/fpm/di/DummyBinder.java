package org.fpm.di;

import org.fpm.di.exception.BinderException;

import java.util.HashMap;

public class DummyBinder implements Binder {
    private final HashMap<Class<?>, Object> dependencyInstance = new HashMap<>();
    private final HashMap<Class<?>, Class<?>> dependencyClasses = new HashMap<>();

    @Override
    public <T> void bind(Class<T> clazz) {
        isInBinder(clazz);
        dependencyInstance.put(clazz, null);
    }

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        isInBinder(clazz);
        dependencyClasses.put(clazz, implementation);
    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        isInBinder(clazz);
        dependencyInstance.put(clazz, instance);
    }

    private void isInBinder (Class<?> clazz) {
        if (isContainInBinder(clazz)) throw new BinderException(String.format("Class %s is already bind", clazz));
    }

    private boolean isContainInBinder(Class<?> clazz) {
        return dependencyClasses.containsKey(clazz)
                || dependencyInstance.containsKey(clazz);
    }

    public HashMap<Class<?>, Object> getDependencyInstance() {
        return dependencyInstance;
    }
    public HashMap<Class<?>, Class<?>> getDependencyClasses() {
        return dependencyClasses;
    }
}
