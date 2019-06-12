package org.cugxdy.converter;

public class StringConverter implements Converter<String> {

	@Override
	public String convert(Object source) {
		// TODO Auto-generated method stub
		return source.toString();
	}

}
