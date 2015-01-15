package com.easysocial.eclipse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.osgi.framework.Bundle;

import com.easysocial.eclipse.oauth.OauthService;
import com.easysocial.integrator.DataIntegrator;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.oauthclient.OauthParameters;


/**
 * This class handles the authorization jobs with the data sources involved 
 * with this plugin.
 * 
 * @author Dahai Guo
 *
 */
public class DataServices {
	private Map<String, OauthService> services;
	private Map<String, OauthParameters> serviceParameters;
	private Map<String, Class<? extends DataIntegrator>> dataIntegrators;
	
	/**
	 * Initializes the data services.
	 * @return
	 */
	public static DataServices loadDataServices(){
		
		String sources[] = PluginUtils.loadDataSourceNames();
		Map<String, String> serviceClasses = PluginUtils.loadOauthServiceNames(sources);
		Map<String, OauthParameters> params = PluginUtils.loadParameters(sources);
		Map<String, Class<? extends DataIntegrator>> dataIntegrators = 
				PluginUtils.loadDataIntegrators(sources);
		
		Map<String, OauthService> services = new HashMap<String, OauthService>();
		for(String source:sources){
			OauthService service = PluginUtils.buildOauthService(
					params.get(source), serviceClasses.get(source));
			services.put(source, service);
		}
		return new DataServices(services, params, dataIntegrators);
	}
	
	public Collection<String> getDataSources(){
		return services.keySet();
	}
	
	private DataServices(Map<String, OauthService> services,
			Map<String, OauthParameters> serviceParameters, 
			Map<String, Class<? extends DataIntegrator>> downloaders){
		this.services = services;
		this.serviceParameters = serviceParameters;
		this.dataIntegrators = downloaders;
	}
	
	public OauthService getOauthService(String dataSource){
		return services.get(dataSource);
	}

	public Map<String, OauthService> getOauthServices(List<String> dataSources){
		Map<String, OauthService> services = new HashMap<String, OauthService>();
		for(String name:dataSources){
			OauthService service = getOauthService(name);
			if(service!=null){
				services.put(name, service);
			}
		}
		return services;
	}
	
	public Map<String, Class<? extends DataIntegrator>> getDataIntegrator(){
		return dataIntegrators;
	}
	
	/**
	 * Downloads and saves to the local workspace.
	 * @param sources 
	 * @param dbPath
	 */
	public void integrate(Set<String> sources, String dbPath){
		for(String sourceName : dataIntegrators.keySet()){
			if(!sources.contains(sourceName)){
				continue;
			}
			Class<? extends DataIntegrator> dataIntegratorClass =
					dataIntegrators.get(sourceName);
			
			Downloader downloader = new Downloader(dataIntegratorClass, 
						serviceParameters.get(sourceName), sourceName, dbPath);
			
			EasySocialLogger.log(Level.INFO, "Scheduling the downloader for "+sourceName);			
			downloader.schedule(2000);
			EasySocialLogger.log(Level.INFO, "Downloader scheduled");
		}
	}
	
	public OauthParameters getParams(String name){
		return serviceParameters.get(name);
	}
}
