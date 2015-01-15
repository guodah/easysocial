package com.easysocial.eclipse.oauth;

import java.util.Map;

import com.easysocial.oauthclient.OauthParameters;

/**
 * Each implementation should define
 * <ol>
 * <li> The url where to get an authorization code
 * <li> The name of the authorization code, using which to get the actual
 *      code from the web url query parameters
 * <li> The way of getting access token given an authorization code
 * </ol> 
 * @author Dahai Guo
 *
 */
public abstract class OauthService {
	protected OauthParameters params;
	public OauthService(OauthParameters params){
		this.params = params;
	}
	
	protected abstract Map<String, String> findAccessToken(String oauth_code);

	protected abstract String getOauthCodeName();

	protected abstract String findOauthUrl();
	
}