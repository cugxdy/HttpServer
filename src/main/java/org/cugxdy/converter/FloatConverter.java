package org.cugxdy.converter;

public class FloatConverter implements Converter<Float> {

	@Override
	public Float convert(Object source) {
		// TODO Auto-generated method stub
		return Float.parseFloat(source.toString());
	}

}
