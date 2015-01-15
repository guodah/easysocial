package com.easysocial.oauthclient;

import java.util.HashMap;
import java.util.Map;

public class OauthParameters {
	private String clientId;
	private String clientSecret;
	private String scope;
	private String redirect_uri;
	
	protected String description;
	protected Map<String, String> tokens;
	protected Map<String, String> integrator_params;
	protected String integrator_params_desc;
	protected boolean limitAccess;

	protected OauthParameters(String clientId, String clientSecret,
			String scope, String callback){
		this.setClientId(clientId);
		this.setClientSecret(clientSecret);
		this.setScope(scope);
		this.setRedirect_uri(callback);
		integrator_params = new HashMap<String, String>();
		limitAccess = true;
	}
	
	@Override
	public String toString(){
		String result = String.format(
				"%s=%s\n%s=%s\n%s=%s\n%s=%s\n%s=%s", 
				"Client ID", clientId,
				"Client Secret", clientSecret,
				"Scope", scope,
				"Redirect URI", redirect_uri,
				"Description", description);
		
		for(String name :integrator_params.keySet()){
			result = String.format(result+"\n%s=%s", name, integrator_params.get(name));
		}
		return result;
	}
	
	public void setDescriptioin(String description){
		this.description = description;
	}
	
	public String getDescrition(){
		return description;
	}
	
	public void setTokens(Map<String, String> tokens){
		this.tokens = tokens;
	}
	
	public Map<String, String> getTokens(){
		return tokens;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRedirectURI() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setIntegratorParam(String name, String value) {
		integrator_params.put(name, value);
	}
	
	public void setIntegratorParamDesc(String desc){
		integrator_params_desc = desc;
	}
	
	public String getIntegratorParamDesc(){
		return integrator_params_desc;
	}
	
	public Map<String, String> getIntegratorParams(){
		return integrator_params;
	}
	
	public void setLimitAccess(boolean limit){
		this.limitAccess = limit;
	}
	
	public boolean getLimitAccess(){
		return this.limitAccess;
	}

	public String getIntegratorParam(String key) {
		return integrator_params.get(key);
	}
}
