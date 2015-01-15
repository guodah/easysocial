package com.easysocial.eclipse.oauth;

import java.util.HashMap;

import java.util.Map;
import java.util.logging.Level;

import com.easysocial.logging.EasySocialLogger;
import com.easysocial.oauthclient.OauthParameters;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterOauthService extends OauthService {

	private RequestToken requestToken;
	private Twitter twitter;
	public TwitterOauthService(OauthParameters params) {
		super(params);
//		init();
	}

	private void init(){
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(params.getClientId(), 
				params.getClientSecret());
	}
	
	@Override
	protected Map<String, String> findAccessToken(String oauth_code) {
		AccessToken token;
		try {
			token = twitter.getOAuthAccessToken(requestToken,oauth_code);
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put("token", token.getToken());
		result.put("token_secret", token.getTokenSecret());
		params.setTokens(result);
		return result;
	}

	@Override
	protected String getOauthCodeName() {
		return "oauth_verifier";
	}

	@Override
	protected String findOauthUrl() {
		init();
		
		try {
			requestToken = twitter.getOAuthRequestToken(
					params.getRedirectURI());
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EasySocialLogger.log(Level.SEVERE, "In TwitterOauthService.findOauthUrl",e);
			throw new IllegalStateException(e);
			
		}
		return requestToken.getAuthenticationURL()+"&force_login=true";
	}

}
