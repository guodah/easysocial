package com.easysocial.types.packed;


public class FBSavableUser implements SavableObject{
	public String id;
	public String name;
	public String first_name;
	public String last_name;
	public String locale;
	public String male;
	public String isMe;
	@Override
	public String getTableName() {
		return "FBUser";
	}
}
