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
import org.cugxdy.mybatis.session.DefaultSqlSession;

public class ProduceQuery implements Business{

	@Override
	public Object process(Object obj, ResultObject result) {
		// TODO Auto-generated method stub
//		Map<String, String> map = (Map<String, String>) obj;
//		
//		System.out.println("========================进入业务处理中===========================");
//		Set<Entry<String,String>> entrySet = map.entrySet();
//		List<String> list = new ArrayList<String>();
//		for(Entry<String,String> entry : entrySet) {
//			System.out.println("key = " + entry.getKey() + " value = " + entry.getValue());
//			list.add(entry.getValue());
//		}
		
		MethodProxyFactory factory = new MethodProxyFactory(ProduceOperation.class);
		ProduceOperation operation = (ProduceOperation) factory.newInstance(new DefaultSqlSession());

		List<?> list =  operation.query();
		if(list != null && list.size() != 0) {
			result.setErrerCode("0000");
			result.setErrerText("操作正确");
			result.setList(list);
		}else {
			result.setErrerCode("0001");
			result.setErrerText("数据库操作异常!");
		}
		return null;
	}

}
