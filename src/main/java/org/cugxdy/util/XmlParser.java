package org.cugxdy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlParser {

	// document对象
	private Document doc;
	// xml文件读取器
	private SAXReader reader = new SAXReader();;
	// 服务配置路径
	private String path = "service.xml";
	
	public XmlParser() throws DocumentException {
		InputStream in = XmlParser.class.getClassLoader().getResourceAsStream(path);
		if(in != null) {
			this.doc = this.reader.read(in);
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("配置文件路径有问题");
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> parseXml() {
		
		Map<String,String> map = new HashMap<String,String>();

		List<Object> list = this.doc.selectNodes("//service");
		
		Iterator<Object> itor = list.iterator();
		while(itor.hasNext()) {
			Element ele = (Element) itor.next();
			String id = null;
			if(ele.attributeValue("id") == null || "".equals(ele.attributeValue("id")) ) {
				System.out.println("id属性不能为空");
				continue;
			}else {
				id = ele.attributeValue("id");
			}
			
			// 获取action元素节点值
			Element actionElement = ele.element("action");
			String action = null;
			if(actionElement == null) {
				System.out.println("action元素不能为空");
				continue;
			}else {
				action = actionElement.getStringValue();
			}
			if( id != null && action != null) {
				map.put(id, action);
			}
		}
		return map;
	}
	
}
