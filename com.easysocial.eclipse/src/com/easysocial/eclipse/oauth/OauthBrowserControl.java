package com.easysocial.eclipse.oauth;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;

import com.easysocial.eclipse.PluginUtils;

/**
 * Responds to and controls a web browser for getting access tokens.
 *  
 * @author Dahai Guo
 *
 */
public class OauthBrowserControl implements ProgressListener{
	private Browser browser;
	
	/**
	 * authorization code
	 */
	private String code;
	
	/**
	 * How authorization code is referred to as, i.e. sometimes
	 * it is called "code" or "oauth_verifier"
	 */
	private String codeName;
	
	/**
	 * A map of services that defines the details of oauth for each 
	 * data source
	 */
	private Map<String, OauthService> services;
	
	/**
	 * The list of data sources, such as "Facebook", "Google", "Twitter"
	 */
	private Iterator<String> sources;
	
	/**
	 * The data source currently being served
	 */
	private String currentSource;
	
	/**
	 * Defines what to do when the entire process ends
	 */
	private CompleteHandler completeHandler;
	
	/**
	 * The result of the oauth process
	 */
	private Map<String, Map<String, String>> tokens;
	
	
	private OauthBrowserControl(Browser browser, Map<String, OauthService> services,
			CompleteHandler completeHandler){
		this.browser = browser;
		code = null;
		browser.addProgressListener(this);
		this.completeHandler = completeHandler;
		this.services = services;
		sources = services.keySet().iterator();
		tokens = new HashMap<String, Map<String, String>>();
	}
	

	/**
	 * no-op
	 */
	@Override
	public void changed(ProgressEvent e) {
//		browser.execute("document.cookie='c_user=; expires=Thu, 01-Jan-70 00:00:01 GMT;'");
	}

	/**
	 * See if the authorization code is in the url. If yes, trade it for
	 * an access token; otherwise, do nothing. When an access token is gotten,
	 * the browser may be set to authorize for the next data source if any.
	 */
	@Override
	public void completed(ProgressEvent e) {
		
		codeName = services.get(currentSource).getOauthCodeName();
		String url = browser.getUrl();
//		System.out.println(url);
		int codePos = url.indexOf(codeName+"=");
				
		if(codePos>=0){
			this.code = PluginUtils.getParam(url, codeName);
			done();
		}
	}
	
	private void done() {
		Map<String, String> result = services.get(currentSource).findAccessToken(code);
		tokens.put(currentSource, result);
	//	if(sources.hasNext()){
		restart();
	//	}else{
	//		completeHandler.complete(tokens);
	//	}
	}

	public void restart(){
		if(sources.hasNext()){
			currentSource = sources.next();
			String url = services.get(currentSource).findOauthUrl();
		//	System.out.println(url);
			browser.setUrl(url);
		}else{
			completeHandler.complete(tokens);
		}
	}
	
	/**
	 * Builds a controler that uses a browser to get access tokens for data sources.
	 * 
	 * @param browser
	 * @param serviceClasses a map of objects for handling oauth authorization 
	 * @param completeHandler defines how the caller and controller communicate
	 * @return
	 */
	public static OauthBrowserControl build(Browser browser, 
			Map<String, OauthService>serviceClasses, 
				CompleteHandler completeHandler){
		if(browser!=null && serviceClasses!=null && completeHandler!=null){
			OauthBrowserControl control = new 
				OauthBrowserControl(browser, serviceClasses, completeHandler);
			return control;
		}else{
			return null;
		}
	}
}