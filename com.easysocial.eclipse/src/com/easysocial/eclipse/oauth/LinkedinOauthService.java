package com.easysocial.eclipse.oauth;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import com.easysocial.oauthclient.OauthParameters;


public class LinkedinOauthService extends OauthService{

	public LinkedinOauthService(OauthParameters params) {
		super(params);
	}

	@Override
	protected Map<String, String> findAccessToken(String oauth_code) {
		Map<String, String> result = new HashMap<String, String>();
		String tokenUrl = "https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code"
	            +"&code="+oauth_code
	            +"&redirect_uri="+params.getRedirectURI()
	            +"&client_id="+params.getClientId()
	            +"&client_secret="+params.getClientSecret();
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(tokenUrl);
		System.out.println(tokenUrl);
		org.apache.http.HttpResponse response;
		try {
			response = client.execute(request);
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			String line="", json="";
			while((line=rd.readLine())!=null){
				json += line;
			}
			System.out.println(json);
			String tag = "\"access_token\"";
			json = json.substring(json.indexOf(tag)+tag.length(), json.length());
			json = json.substring(json.indexOf('\"')+1);
			String access_token = json.substring(0, json.indexOf("\""));
			result.put("access_token", access_token);
			params.setTokens(result);
			return result;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

	}

	@Override
	protected String getOauthCodeName() {
		return "code";
	}

	@Override
	protected String findOauthUrl() {
		String oauth_url = "https://www.linkedin.com/uas/oauth2/authorization?"+
				"response_type=code"+
				"&client_id="+params.getClientId()+
				"&state=DCEEFWF45453sdffef424"+
				"&redirect_uri="+params.getRedirectURI();
		return oauth_url;
	}

}
