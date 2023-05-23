package org.fpm.di;

import org.fpm.di.exception.ContainerException;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DummyContainer implements Container {
    private final Map<Class<?>, Object> dependencyInstance;
    private final Map<Class<?>, Class<?>> dependencyClasses;

    public DummyContainer(HashMap<Class<?>, Object> DependencyObjects, HashMap<Class<?>, Class<?>> DependencyClasses) {
        this.dependencyInstance = DependencyObjects;
        this.dependencyClasses = DependencyClasses;
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        if (dependencyInstance.containsKey(clazz)) {
            if (dependencyInstance.get(clazz) == null) {
                if (clazz.isAnnotationPresent(Singleton.class)) {
                    T singletonObj = createInstance(clazz);
                    dependencyInstance.put(clazz, singletonObj);
                    return singletonObj;
                }
                return createInstance(clazz);
            }
            return clazz.cast(dependencyInstance.get(clazz));
        }
        if (dependencyClasses.containsKey(clazz)) {
            return clazz.cast(getComponent(dependencyClasses.get(clazz)));
        }
        throw new ContainerException(clazz.getName()
                + " class is not in container");
    }

    private <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            List<Constructor<?>> constructorsWithInjectAnnotation = new ArrayList<>();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getAnnotation(Inject.class) != null) {
                    constructorsWithInjectAnnotation.add(constructor);
                }
            }

            if (constructorsWithInjectAnnotation.isEmpty()) {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                return constructor.newInstance();
            }

            for (Constructor<?> constructor : constructorsWithInjectAnnotation) {
                boolean canCreateInstance = true;
                Class<?>[] parameters = constructor.getParameterTypes();
                List<Object> objParameters = new ArrayList<>();

                for (Class<?> parameter : parameters) {
                    try {
                        objParameters.add(getComponent(parameter));
                    } catch (ContainerException ex) {
                        canCreateInstance = false;
                        break;
                    }
                }

                if (canCreateInstance) {
                    Constructor<T> constructorToCreate = clazz.getDeclaredConstructor(parameters);
                    return constructorToCreate.newInstance(objParameters.toArray());
                }
            }
        } catch (IllegalAccessException | InvocationTargetException
                | InstantiationException | NoSuchMethodException e) {
            throw new ContainerException("Can't create object of class "
                    + clazz.getName());
        }
        throw new ContainerException("Can't create object of class "
                + clazz.getName());
    }
}
