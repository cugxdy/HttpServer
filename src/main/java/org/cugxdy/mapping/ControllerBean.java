package org.cugxdy.mapping;

public class ControllerBean {

	private Class<?> clazz;
	private boolean singleton;
	
    public ControllerBean(Class<?> clazz, boolean singleton) {
        this.clazz = clazz;
        this.singleton = singleton;
    }

	public final Class<?> getClazz() {
		return clazz;
	}

	public final void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public final boolean isSingleton() {
		return singleton;
	}

	public final void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
}
