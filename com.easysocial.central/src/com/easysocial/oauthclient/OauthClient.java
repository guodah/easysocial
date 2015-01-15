package com.easysocial.oauthclient;

import java.util.Map;

/**
 * Subclass should define how to download data from data source.
 * 
 * @author Dahai Guo
 *
 */
public class OauthClient {
	protected OauthParameters params;
	public OauthClient(OauthParameters params){
		this.params = params;
	}
}
