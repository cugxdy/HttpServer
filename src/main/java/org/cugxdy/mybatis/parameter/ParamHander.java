package org.cugxdy.mybatis.parameter;

import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class ParamHander {

	private PreparedStatement statement;
	
	public ParamHander(PreparedStatement statement) {
		// TODO Auto-generated constructor stub
		this.statement = statement;
	}

	public void setParameters(Object object) {
		// TODO Auto-generated method stub
		if(object == null) {
			return;
		}
        try {
        	if(object instanceof List) {
                // PreparedStatement的序号是从1开始的
        		List<?> list = (List<?>) object;
                for (int i = 0; i <list.size(); i++) {
                    int k =i+1;
                    if (list.get(i) instanceof Integer) {
                    	statement.setInt(k, (Integer) list.get(i));
                    } else if (list.get(i) instanceof Long) {
                    	statement.setLong(k, (Long) list.get(i));
                    } else if (list.get(i) instanceof String) {
                    	System.out.println("将数据插入进sql语句中");
                    	statement.setString(k , String.valueOf(list.get(i)));
                    } else if (list.get(i) instanceof Boolean) {
                    	statement.setBoolean(k, (Boolean) list.get(i));
                    } else {
                    	statement.setString(k, String.valueOf(list.get(i)));
                    }
                }
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
