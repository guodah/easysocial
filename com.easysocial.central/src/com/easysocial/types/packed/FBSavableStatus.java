package com.easysocial.types.packed;

import com.easysocial.types.packed.LongField;

public class FBSavableStatus  implements SavableObject{
	public String from_id;
	public String id;
	@LongField
	@SQLType(type = SQLType.LONG_TEXT)
	public String message; 
	public String update_time;
	@Override
	public String getTableName() {
		return "FBStatus";
	}
}
