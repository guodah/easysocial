package com.easysocial.types.downloaded;

import com.restfb.types.User;

public class FBUser extends User implements DownloadedObject{

	private boolean isMe=false;
	public void isMe(boolean isMe){
		this.isMe = isMe;
	}
	
	public boolean isMe(){
		return this.isMe;
	}
}
