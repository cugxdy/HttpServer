package org.cugxdy.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter<Date> {

	@Override
	public Date convert(Object source) {
		// TODO Auto-generated method stub
		SimpleDateFormat fotmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			return fotmat.parse((String) source);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;	
	}

}
