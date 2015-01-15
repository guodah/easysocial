package com.easysocial.types.packed;

public class SavableTweetURL implements SavableObject{
	public String tweet_id;
	public String url;
	@Override
	public String getTableName() {
		return "tweet_url";
	}

}
