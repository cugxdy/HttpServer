package org.cugxdy.mybatis.parameter;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.cugxdy.mybatis.session.Configuation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class StatementHandler {
	private Logger log = LoggerFactory.getLogger(StatementHandler.class);
	
	private String sql; // sql执行语句
	
	private String type;
	private ResultSetHandler resultHandler = new ResultSetHandler();

	public StatementHandler(String sql ,String type) {
		// TODO Auto-generated constructor stub
		this.sql = sql;
		this.type = type;
	}
	
	public Object executeParam(Object[] objects) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(this.sql);
			ParamHander handler = new ParamHander(statement);
			handler.setParameters(objects[0]);
			if(statement.execute()) {
				return resultHandler.parse(statement.getResultSet());
			}else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("StatementHandler---executeParam: throws exception");
		}
		return null;
	}
	

	public Object execute(Object[] objects) {
		// TODO Auto-generated method stub
		Connection connection = null;
		Statement statement= null;
		try {
			connection = this.getConnection();
			statement = (Statement) connection.createStatement();
			statement.execute(sql);
			return resultHandler.parse(statement.getResultSet());
		} catch (Exception e) {
			// TODO: handle exception
			log.error("StatementHandler---execute: throws exception");
		}finally {
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				statement.cancel();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	private Connection getConnection() {
		// TODO Auto-generated method stub
		String url = Configuation.getUrl();
		String driver = Configuation.getDriver();
		String userName = Configuation.getUserName();
		String passWord = Configuation.getPassword();
		
		try {
			Class.forName(driver);
			return (Connection) DriverManager.getConnection(url,userName,passWord);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("异常抛出");
		}
		
		return null;
	}

	public Object executeUpate(Object[] args) {
		// TODO Auto-generated method stub
//		System.out.println("StatementHandler--------executeUpate");
//		System.out.println(args[0]);
//		if(args[0] instanceof List) {
//			System.out.println("输入参数为List");
//		}
//
//		System.out.println("========================================");
		
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(sql);
			ParamHander handler = new ParamHander(statement);
			handler.setParameters(args[0]);
			
			statement.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
					
		}finally {
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				statement.cancel();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
