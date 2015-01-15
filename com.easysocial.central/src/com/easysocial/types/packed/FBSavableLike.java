package com.easysocial.types.packed;

import com.easysocial.types.downloaded.DownloadedObject;

public class FBSavableLike implements SavableObject{
	public String id;

	@LongField
	@SQLType(type = SQLType.LONG_TEXT)
	public String name;
	public String category;
	public String created_time;
	public String from_id;
	@Override
	public String getTableName() {
		return "FBLike";
	}
}
