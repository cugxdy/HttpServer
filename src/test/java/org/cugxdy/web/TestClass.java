package org.cugxdy.web;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cugxdy.business.Message;
import org.cugxdy.util.XmlParser;
import org.dom4j.DocumentException;
import org.junit.Test;

public class TestClass {
	
	@Test
	public void test() throws DocumentException {
		
		Message.map.size();
		XmlParser xml = new XmlParser();
		
		Map<String,String> map = xml.parseXml();

		Set<Entry<String,String>> entrySet = map.entrySet();
		
		for(Entry<String,String> entry : entrySet) {
			System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
		}
		
	}

}
