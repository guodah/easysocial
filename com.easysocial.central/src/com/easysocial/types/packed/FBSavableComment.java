package com.easysocial.types.packed;

import java.sql.Date;

import com.easysocial.types.packed.LongField;


import com.easysocial.types.downloaded.DownloadedObject;

public class FBSavableComment implements SavableObject{
	public String id;
	public String from_id;
	public String target_id;
	public String target_type;
	
	public String created_time;
	
	@SQLType(type = "varchar(65536)")
	public String content;
	@Override
	public String getTableName() {
		return "FBComment";
	}

}
