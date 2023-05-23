package org.fpm.di;

public class DummyEnvironment implements Environment {

    @Override
    public Container configure(Configuration configuration) {
        DummyBinder binder = new DummyBinder();
        configuration.configure(binder);
        return new DummyContainer(binder.getDependencyInstance(), binder.getDependencyClasses());
    }
}
