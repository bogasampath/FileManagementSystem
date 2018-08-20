package com.fileupload.db;

import java.io.Serializable;

public class User implements Serializable {
	
	private int userId;
	
	private String userName;
	
	private String password;
	
	private String firstName;
	
	private String lastName;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("User - userName: ")
			.append(getUserName())
			.append(", userId: ")
			.append(getUserId())
			.append(", password: ")
			.append(getPassword())
			.append(", firstName: ")
			.append(getFirstName())
			.append(", lastName: ")
			.append(getLastName());
		return sb.toString();
	}
}
