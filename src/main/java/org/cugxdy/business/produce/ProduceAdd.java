package org.cugxdy.business.produce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.cugxdy.business.Business;
import org.cugxdy.business.ResultObject;
import org.cugxdy.mybatis.binding.MethodProxyFactory;
import org.cugxdy.mybatis.mapping.ProduceOperation;
import org.cugxdy.mybatis.mapping.UserOperation;
import org.cugxdy.mybatis.session.DefaultSqlSession;

public class ProduceAdd implements Business {

	@SuppressWarnings("unchecked")
	@Override
	public Object process(Object obj, ResultObject result) {
		// TODO Auto-generated method stub
		Map<String, String> map = (Map<String, String>) obj;
		
		System.out.println("========================进入业务处理中===========================");
		Set<Entry<String,String>> entrySet = map.entrySet();
		List<String> list = new ArrayList<String>();
		for(Entry<String,String> entry : entrySet) {
			System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
			list.add(entry.getValue());
		}
		
		System.out.println(list.size());
		System.out.println("=============================================================");
		MethodProxyFactory factory = new MethodProxyFactory(ProduceOperation.class);
		ProduceOperation operation = (ProduceOperation) factory.newInstance(new DefaultSqlSession());

		boolean resuleObject = operation.insert(list);
		
		if(resuleObject) {
			result.setErrerCode("0000");
			result.setErrerText("业务处理");
		}else {
			result.setErrerCode("0001");
			result.setErrerText("业务处理");
		}

		System.out.println("=============================================================");
		return result;
	}

}
