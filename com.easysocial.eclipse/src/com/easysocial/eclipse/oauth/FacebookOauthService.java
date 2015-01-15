package com.easysocial.eclipse.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.easysocial.eclipse.PluginUtils;
import com.easysocial.oauthclient.OauthParameters;

public class FacebookOauthService extends OauthService{

	public FacebookOauthService(OauthParameters params) {
		super(params);
	}

	@Override
	protected Map<String, String> findAccessToken(String oauth_code) {
		String token_url = "https://graph.facebook.com/oauth/access_token";
		
		token_url = token_url+"?"+
				"client_id="+params.getClientId()+"&"+
				"redirect_uri="+params.getRedirectURI()+"&"+
				"client_secret="+params.getClientSecret()+"&"+
				"code="+oauth_code;

		Map<String, String> result = new HashMap<String, String>();
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(token_url);
		try {
			HttpResponse response=client.execute(request);
			
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			String line="";
			while((line=rd.readLine())!=null){
				if(line.contains("access_token=")){
					String token = PluginUtils.getParam(line, "access_token");
					result.put("access_token", token);
				}
			}
			params.setTokens(result);
			return result;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}	
	}

	@Override
	protected String getOauthCodeName() {
		return "code";
	}

	@Override
	protected String findOauthUrl() {
//		String oauth_url = "http://www.facebook.com/dialog/oauth"+"?"+
		String oauth_url = "http://itech.fgcu.edu/faculty/dguo/fbtest/fb_logout.html"+"?"+
				"client_id="+params.getClientId()+"&"+
				"redirect_uri="+params.getRedirectURI()+"&"+
				"scope="+params.getScope()+"&"+
				"response_type=code";//+"&"+
				//"auth_type=reauthenticate";
		return oauth_url;
	}

}
