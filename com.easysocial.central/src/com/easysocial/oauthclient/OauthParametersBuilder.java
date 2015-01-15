package com.easysocial.oauthclient;

public class OauthParametersBuilder {
	private OauthParameters params;
	
	public static OauthParametersBuilder newBuilder(String clientId, String clientSecret,
			String scope, String callback){
		if(clientId==null || clientSecret==null || callback==null){
			return null;
		}
		OauthParametersBuilder builder = new OauthParametersBuilder(
				clientId, clientSecret, scope, callback);
		return builder;
	}
	
	private OauthParametersBuilder(String clientId, String clientSecret,
			String scope, String callback){
		params = new OauthParameters(clientId, clientSecret, scope, callback);
	}
	
	public void setIntegratorParam(String name, String value){
		params.setIntegratorParam(name, value);
	}
	
	public void setIntegratorParamDesc(String desc){
		params.setIntegratorParamDesc(desc);
	}
	
	public OauthParameters build(){
		return params;
	}

	public void setDescription(String desc) {
		params.setDescriptioin(desc);
	}
}
