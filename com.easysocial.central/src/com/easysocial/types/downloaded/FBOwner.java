package com.easysocial.types.downloaded;

import com.restfb.types.NamedFacebookType;

public class FBOwner extends NamedFacebookType implements DownloadedObject{
	private String owner;
	public String getOwner(){
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
