package org.cugxdy.converter;

public class DoubleConverter implements Converter<Double> {

	@Override
	public Double convert(Object source) {
		// TODO Auto-generated method stub
		return Double.parseDouble(source.toString());
	}

}
