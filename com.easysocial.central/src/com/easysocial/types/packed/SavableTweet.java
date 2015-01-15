package com.easysocial.types.packed;

import java.sql.Date;

public class SavableTweet implements SavableObject{
	public String createdAt;
	public String id;
	public String retweetCount;
	@SQLType(type="varchar(200)")
	public String text;
	public String author;
	public String isRetweet;
	public String sourceAuthor;
	@Override
	public String getTableName() {
		return "Tweet";
	}
}
