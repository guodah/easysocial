package com.easysocial.eclipse.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import com.easysocial.oauthclient.OauthParameters;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

public class GoogleOauthService extends OauthService {

	public GoogleOauthService(OauthParameters params) {
		super(params);
	}

	@Override
	protected Map<String, String> findAccessToken(String oauth_code) {
		HttpTransport transport = new NetHttpTransport();
	    JacksonFactory jsonFactory = new JacksonFactory();

		GoogleTokenResponse response;
		Map<String, String> result = new HashMap<String, String>();
		try {
			response = new GoogleAuthorizationCodeTokenRequest(
					transport, jsonFactory, 
					params.getClientId(), params.getClientSecret(), 
					oauth_code, params.getRedirectURI()).execute();
			result.put("access_token", response.getAccessToken());
			params.setTokens(result);
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected String getOauthCodeName() {
		return "code";
	}

	@Override
	protected String findOauthUrl() {
		StringTokenizer st = new StringTokenizer(params.getScope());
		List<String> scopeList = new ArrayList<String>();
		while(st.hasMoreTokens()){
			scopeList.add(st.nextToken());
		}
		String oauth_url =
		        new GoogleAuthorizationCodeRequestUrl(
		        		params.getClientId(), 
		        		params.getRedirectURI(), 
		        		scopeList).setAccessType("offline").build();
		return oauth_url;
	}

}
