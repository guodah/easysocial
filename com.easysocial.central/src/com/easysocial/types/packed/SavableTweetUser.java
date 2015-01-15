package com.easysocial.types.packed;

public class SavableTweetUser implements SavableObject{
	public String id;
	public String name;
	public String screenName;
	public String isFollowedBy;
	public String isFollowing;
	@Override
	public String getTableName() {
		return "tweet_user";
	}
}
