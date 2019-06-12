package org.cugxdy.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import io.netty.util.CharsetUtil;

public class Message {
	
	public static Map<Object,Object> map = new HashMap<Object,Object>();
	
	static {
		Properties prop = new Properties();
		
		InputStream in = Message.class.getClassLoader().getResourceAsStream("message.properties");
		
		try {
			prop.load(in);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("加载文件失败");
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Set<Entry<Object,Object>> entrySet = prop.entrySet();
		
		for(Entry<Object,Object> entry : entrySet) {
			System.out.println("Key = " + entry.getKey() + " value = " + entry.getValue());
			map.put(entry.getKey(), new String(entry.getValue().toString().getBytes(),CharsetUtil.UTF_8));
		}
		
	}

}
