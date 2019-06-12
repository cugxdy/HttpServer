package org.cugxdy.converter;

public class IntegerConverter implements Converter<Integer> {

	@Override
	public Integer convert(Object source) {
		// TODO Auto-generated method stub
		return Integer.parseInt(source.toString());
	}

}
