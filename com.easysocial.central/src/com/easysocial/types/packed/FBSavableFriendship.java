package com.easysocial.types.packed;


public class FBSavableFriendship implements SavableObject{
	public String id1;
	public String id2;
	@Override
	public String getTableName() {
		return "FBFriendship";
	}
}
