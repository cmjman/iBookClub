package com.shining.ibookclub.bean;

import java.io.Serializable;

public class TimelineBean implements Serializable,Bean{

	private String avatar;
	
	private String nickname;
	
	private String message;

	private String timestamp;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
