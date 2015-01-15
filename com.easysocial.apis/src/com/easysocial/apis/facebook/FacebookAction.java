package com.easysocial.apis.facebook;

/**
 * This class summarizes what EasySocial can do. 
 * 
 * The items in this enumeration class correspond to the methods in
 * EasySocial
 * 
 * @author Dahai Guo
 *
 */
enum FacebookAction {
	FIND_PEOPLE_FULL_NAME("find_people_full_name"),
	FIND_PEOPLE("find_people"),
	FIND_STATUSES_BY_MESSAGE("find_statuses_by_message"),
	FRIENDS("friends"),
	GET_FRIENDS("get_friends"),
	GET_NAME("get_name"),
	GET_FIRST_NAME("get_first_name"),
	GET_LAST_NAME("get_last_name"),
	IS_MALE("male"),
	GET_STATUS_AUTHOR("status_author"),
	GET_STATUS_IDS("status_id"),
	GET_STATUS_MESSAGE("status_msg"),
	GET_COMMENT("get_comment"),
	IN_NETWORK("in_network"),
	GET_LIKERS("get_likers"),
	GET_MY_FRIENDS("get_my_friends"),
	GET_ALL_USER("get_all_users"),
	GET_ME("get_me");

	private String actionCode;
	private FacebookAction(String s){
		actionCode = s;
	}
	
	public String getActionCode(){
		return actionCode;
	}
}
