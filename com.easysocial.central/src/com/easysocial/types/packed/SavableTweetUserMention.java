package com.easysocial.types.packed;

public class SavableTweetUserMention implements SavableObject{
	public String tweet_id;
	public String user_id;
	@Override
	public String getTableName() {
		return "tweet_user_mention";
	}
}
