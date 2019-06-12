package org.cugxdy.mybatis.parameter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.ResultSetMetaData;

public class ResultSetHandler {

	public Object parse(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub

		System.out.println("ResultSetHandler");
		
		List<Map<Object,Object>> list = new LinkedList<Map<Object,Object>>();
		List<String> columns = new ArrayList<String>();
		ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
		
		int count = metaData.getColumnCount();

		while(resultSet.next()) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			System.out.println(count);
			for(int i = 1; i <= count ; i++) {
				String key = metaData.getColumnName(i);
				Object value = resultSet.getObject(key);
				System.out.println("key = " + key + " value = " + value);
				map.put(key, value);
			}
			list.add(map);
		}
		
		System.out.println(list.size());
		
		return list;
	}

}
