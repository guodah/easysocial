package com.easysocial.tests;

import java.util.Date;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.StatusMessage;
import com.restfb.types.User;

public class Main {
	public static void main(String args[]){
		FacebookClient facebookClient = new DefaultFacebookClient(args[0]);
		
		Connection<StatusMessage> _statuses = facebookClient.fetchConnection("60605515/statuses", 
				StatusMessage.class, 
				Parameter.with("until", "1378250641"), Parameter.with("limit","25"),
				Parameter.with("__paging_token", "10100171045765317"));
//		System.out.println(_statuses.getData().size());
//		_statuses = facebookClient.fetchConnectionPage(_statuses.getNextPageUrl(), StatusMessage.class);
		List<StatusMessage> statuses = _statuses.getData();
		System.out.println(statuses.get(0).getMessage());
		
		
//		Date try1 = new Date(System.currentTimeMillis()-1379970951);
//		System.out.println(try1);
		
		
//		Date until = new Date(System.currentTimeMillis() - 
//				1000L * 60L * 60L * 24L * 92L);
		
//		System.out.println(until.getTime());
/*		Connection<StatusMessage> _statuses = facebookClient.fetchConnection(
				"60605515/statuses", StatusMessage.class, Parameter.with("limit", 100));
		System.out.println(_statuses.getNextPageUrl());
		
		List<StatusMessage> statuses = _statuses.getData();
		
		System.out.println(statuses.size());
		Date date = null;
		for(StatusMessage status:statuses){
	//		System.out.println(status.getUpdatedTime()+": "+status.getMessage());
			date = status.getUpdatedTime();
		}
*/		
		/*
		Connection<StatusMessage> _statuses = facebookClient.fetchConnection(
				"60605515/statuses", StatusMessage.class, Parameter.
				with("limit", 5000));
		System.out.println(_statuses.getNextPageUrl());
		
		List<StatusMessage> statuses = _statuses.getData();
		
		System.out.println(statuses.size());
		Date date = null;
		for(StatusMessage status:statuses){
			System.out.println(status.getUpdatedTime()+": "+status.getMessage());
			date = status.getUpdatedTime();
		}
		*/
	}
}
