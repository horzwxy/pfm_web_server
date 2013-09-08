package me.horzwxy.app.pfm.server.model;

public class User {
	
	private String accountName;
	private String nickname;
	
	public User( String accountName ) {
		this.accountName = accountName;
		this.nickname = accountName;	// default nickname is account name
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
