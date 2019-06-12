package org.cugxdy.mybatis.dao;

public class User {
	private String userName;
	private String passWord;
	
	public final String getUserName() {
		return userName;
	}
	public final void setUserName(String userName) {
		this.userName = userName;
	}
	public final String getPassWord() {
		return passWord;
	}
	public final void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	@Override
	public String toString() {
		return "User [userName=" + userName + ", passWord=" + passWord + "]";
	}
}
