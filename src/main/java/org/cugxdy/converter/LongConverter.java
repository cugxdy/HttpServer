package org.cugxdy.converter;

public class LongConverter implements Converter<Long> {

	@Override
	public Long convert(Object source) {
		// TODO Auto-generated method stub
		return Long.parseLong(source.toString());
	}

}
