package com.easysocial.types.downloaded;
import java.util.Date;

import com.restfb.Facebook;
import com.restfb.types.NamedFacebookType;
import com.restfb.util.DateUtils;

public class FBLike extends FBOwner implements DownloadedObject{
	@Facebook
	private String category;
	public String getCategory(){
		return category;
	}
	
	@Facebook("created_time")
	private String createdTime;
	
	public Date getCreatedTime(){
		return DateUtils.toDateFromLongFormat(createdTime);
	}
	
}
