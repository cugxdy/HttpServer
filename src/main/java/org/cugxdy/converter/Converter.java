package org.cugxdy.converter;

public interface Converter<T> {
	
	T convert(Object source);
	
}
