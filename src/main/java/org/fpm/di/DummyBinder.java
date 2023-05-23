package org.fpm.di;

import java.util.HashMap;

public class DummyBinder implements Binder {
    private final HashMap<Class<?>, Object> dependencyInstance = new HashMap<>();
    private final HashMap<Class<?>, Class<?>> dependencyClasses = new HashMap<>();

    @Override
    public <T> void bind(Class<T> clazz) {
        dependencyInstance.put(clazz, null);
    }

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        dependencyClasses.put(clazz, implementation);
    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        dependencyInstance.put(clazz, instance);
    }

    public HashMap<Class<?>, Object> getDependencyInstance() {
        return dependencyInstance;
    }
    public HashMap<Class<?>, Class<?>> getDependencyClasses() {
        return dependencyClasses;
    }
}
