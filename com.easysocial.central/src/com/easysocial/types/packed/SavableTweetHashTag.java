package com.easysocial.types.packed;

public class SavableTweetHashTag implements SavableObject{
	public String tweet_id;
	public String hashTag;
	@Override
	public String getTableName() {
		return "tweet_hashtag";
	}
}
