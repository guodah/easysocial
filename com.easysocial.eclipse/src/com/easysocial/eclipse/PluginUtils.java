package com.easysocial.eclipse;

import java.io.File;






import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.easysocial.eclipse.oauth.OauthService;
import com.easysocial.integrator.DataIntegrator;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.oauthclient.OauthParameters;
import com.easysocial.oauthclient.OauthParametersBuilder;
import com.easysocial.utils.EasySocialUtils;

public class PluginUtils {

	
	/**
	 * Builds an object for coordinating the process of get oauth authorized.
	 * 
	 * @param params oauth parameter
	 * @param className the class to be instantiated
	 * @return
	 */
	public static OauthService buildOauthService(OauthParameters params, 
			String className){
		try {
			OauthService service = (OauthService) Class.forName(className).getConstructor(
					OauthParameters.class).newInstance(params);
			EasySocialLogger.log(Level.INFO, "Oauth service initialized correctly.");
			return service;
		} catch (InstantiationException e){
			EasySocialLogger.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalStateException("Failed to start the authorization process.");			
		} catch (IllegalAccessException e){
			EasySocialLogger.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalStateException("Failed to start the authorization process.");
			
		} catch (IllegalArgumentException e){
			EasySocialLogger.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalStateException("Failed to start the authorization process.");
		} catch (InvocationTargetException e){
			EasySocialLogger.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalStateException("Failed to start the authorization process.");
		} catch (NoSuchMethodException e){
			EasySocialLogger.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalStateException("Failed to start the authorization process.");
		} catch (SecurityException e){
			EasySocialLogger.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalStateException("Failed to start the authorization process.");
		} catch (ClassNotFoundException e){
			EasySocialLogger.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalStateException("Failed to start the authorization process.");
		}

	}
	
	private static String getProperty(String name){
		return Activator.getDefault().getProperty(name);
	}
	
	/**
	 * Loads parameters for oauth data sources from easysocial.properties.
	 * 
	 * @param sources data source names
	 * @return a parameter map whose keys are the data source names
	 */
	public static Map<String, OauthParameters> loadParameters(String [] sources) {
		Map<String, OauthParameters> paramsMap =
				new HashMap<String, OauthParameters>();
		for(String source:sources){
			String client_id = String.format("%s_%s",source,
					EasySocialEclipseProperties.CLIENT_ID);
			String client_secret = String.format("%s_%s",source,
					EasySocialEclipseProperties.CLIENT_SECRET);
			String scope = String.format("%s_%s",source,
					EasySocialEclipseProperties.SCOPE);
			String redirect_uri = String.format("%s_%s",source,
					EasySocialEclipseProperties.REDIRECT_URI);
			String description = String.format("%s_%s", source,
					EasySocialEclipseProperties.DATASOURCE_DESCRIPTION);
			String integrator_params = String.format("%s_%s", source, 
					EasySocialEclipseProperties.INTEGRATOR_PARAMETERS);
			String integrator_params_desc = String.format("%s_%s", source,
					EasySocialEclipseProperties.INTEGRATOR_PARAMETERS_DESCRIPTION);
					
			OauthParametersBuilder paramsBuilder = OauthParametersBuilder.newBuilder(
					getProperty(client_id), 
					getProperty(client_secret),
					getProperty(scope), 
					getProperty(redirect_uri)
					);
			paramsBuilder.setDescription(getProperty(description));
			
			Map<String, String> integrator_params_map = EasySocialUtils.
					parsePropertyList(getProperty(integrator_params));
			EasySocialLogger.log(Level.INFO, String.format("Source %s: %s, got %d properties", 
					source, getProperty(integrator_params), integrator_params_map.size()));
			for(String name:integrator_params_map.keySet()){
				paramsBuilder.setIntegratorParam(name, integrator_params_map.get(name));
			}
			paramsBuilder.setIntegratorParamDesc(getProperty(integrator_params_desc));
			OauthParameters params = paramsBuilder.build();
			paramsMap.put(source, params);
			
			EasySocialLogger.log(Level.INFO, String.format("Load parameters for %s:\n%s",
					source, params.toString()));
		}
		return paramsMap;
	}
	
	/**
	 * Loads the class names for the data sources from easysocial.properties
	 * 
	 * @param sources data source names
	 * @return a class name map whose keys are the data source names
	 */
	public static Map<String, String> loadOauthServiceNames(String [] sources){
		Map<String, String> serviceMap =
				new HashMap<String, String>();
		for(String source:sources){
			String service = String.format("%s_%s",source,
					EasySocialEclipseProperties.OAUTH_SERVICE);
			String serviceClass = getProperty(service);
			
			if(serviceClass==null){
				String msg = "Failed to find the data service name for "+source;
				EasySocialLogger.log(Level.SEVERE,msg);
				throw new IllegalStateException(msg);
			}
			
			serviceMap.put(source, serviceClass);
			EasySocialLogger.log(Level.INFO, "Found data service for "+source+": "+serviceClass);
		}
		return serviceMap;
	}
	

	/**
	 * Uses Java reflection to instantiate data integrators at run-time.
	 * 
	 * In easysocial.properties, integrator classes are specified. It is possible for
	 * a data source not to be acompanied with an integrator class.
	 * 
	 * @param sources class names
	 * @return
	 */
	public static Map<String, Class<? extends DataIntegrator>> loadDataIntegrators(
			String[] sources) {
		Map<String, Class<? extends DataIntegrator>> integrators = 
				new HashMap<String, Class<? extends DataIntegrator>>();
		for(String source:sources){
			String integratorKey = String.format("%s_%s", source,
					EasySocialEclipseProperties.DATA_INTEGRATOR);
			String className = getProperty(integratorKey);
			if(className==null){
				continue;
			}
			try {
				Class<? extends DataIntegrator> integratorClass = 
						(Class<? extends DataIntegrator>) Class.forName(className);
				integrators.put(source, integratorClass);
				EasySocialLogger.log(Level.INFO, 
						"Found data integrator for "+source+": "+className);
			} catch (ClassNotFoundException e) {
				EasySocialLogger.log(Level.SEVERE, 
						"Failed to instantiate data integrator for"+source, e);
			}
		}
		return integrators;
	}
	
	/**
	 * Loads properties from an input stream
	 * @param in
	 * @return property map
	 */
	public static Properties loadProperties(InputStream in){
		Properties properties = new Properties();
		try {
			properties.load(in);
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loads the data sources from easysocial.properties.
	 * 
	 * @return
	 */
	public static String [] loadDataSourceNames(){
		String _sources = getProperty(EasySocialEclipseProperties.OAUTH_SOURCES);
		String sources[] = (_sources==null)?null:_sources.split(",");
		if(sources==null || sources.length==0){
			String msg = "Failed to find any data sources";
			EasySocialLogger.log(Level.SEVERE, msg);
			throw new IllegalStateException(msg);
		}
		return sources;
	}
	
	/**
	 * Finds the value for a query parameter in a url string 
	 * @param url
	 * @param param
	 * @return the value for the param, null if it does not exist
	 */
	public static String getParam(String url, String param) {
		int start = url.indexOf(param+"=");
		if(start<0){
			return null;
		}
		String sub = url.substring(start);
		start = start+param.length()+1;
		int end = sub.indexOf("&");
		
		if(end<0){
			end = sub.length();
		}
		
		return sub.substring(param.length()+1, end);
	}

	/**
	 * Copies the resource files from the bundle to the workspace of the created project.
	 * 
	 * @param javaProject
	 * @param resources
	 * @param monitor
	 * @throws InterruptedException
	 */
	public static void addResourceFiles(IJavaProject javaProject, String[] resources,
			IProgressMonitor monitor) throws InterruptedException {

		String projectPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()+
				javaProject.getProject().getFullPath().makeAbsolute().toString();
		for(int i=0;i<resources.length;i++){
			String resource_file = resources[i];
			InputStream src = PluginFiles.findItem(resource_file);
			File target = new File(projectPath+"/"+
					resource_file.substring(resource_file.indexOf('/')+1));
			try {
				FileUtils.copyInputStreamToFile(src, target);
			} catch (IOException e) {
				String error="Failed to copy the resource: "+resource_file;
				EasySocialLogger.log(Level.WARNING, error);
				throw new InterruptedException(error);
			}
			
		}
	}

	/**
	 * Adds some library to the build path of the workspace of the created project.
	 * 
	 * @param javaProject
	 * @param libs
	 * @param monitor
	 * @throws CoreException
	 */
	public static void addMoreLibs(IJavaProject javaProject,List<String> libs,IProgressMonitor monitor) 
			throws CoreException{
	
		IClasspathEntry[] entries = javaProject.getRawClasspath();
		
		Set<String> exists = new HashSet<String>();
		for(IClasspathEntry entry:entries){
			exists.add(entry.getPath().toString().replace('\\', '/'));
		}
		
		List<String> _libs = new ArrayList<String>();
		for(String st:libs){
			_libs.add(st.replace('\\', '/'));
		}
		
		for(String st:exists){
			EasySocialLogger.log(Level.INFO, "addMoreLibs exists: "+st);
			if(_libs.contains(st)){
				_libs.remove(st);
			}
		}
		
		if(_libs.size()==0){
			return;
		}
		
		IClasspathEntry[] newEntries = new IClasspathEntry[entries.length+_libs.size()];
		for(int i=0;i<entries.length;i++){
			newEntries[i] = entries[i];
			EasySocialLogger.log(Level.INFO, "addMoreLibs:"+entries[i].getPath().toString());
			
		}
		
		for(int i=0;i<_libs.size();i++){
			String lib = _libs.get(i);
			IPath libPath = new Path(lib);
			newEntries[entries.length+i] = JavaCore.newLibraryEntry(libPath, null, null);			
		}
		javaProject.setRawClasspath(newEntries, monitor);	
	}
	
	private static String getOS(){
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("mac")){
			return "mac";
		}else if(os.contains("win")){
			return "win";
		}else if(os.contains("nix") || os.contains("nux") || os.contains("aix")){
			return "unix";
		}else{
			return null;
		}
	}
	
	/**
	 * From the plugins folder of the current Eclipse, finds the jar file for a specific 
	 * plugin.
	 * 
	 * @param plugin
	 * @param version
	 * @return
	 */
	private static String getPluginJarPath(String plugin, String version){
		
		EasySocialLogger.log(Level.INFO, "user.dir is "+
				System.getProperty("user.dir"));
		File pluginFolder = new File(System.getProperty("user.dir")+
				(getOS().equals("mac")?"/../../..":"")+"/plugins");
		String path;
		if(!pluginFolder.exists() || !pluginFolder.isDirectory()){
			path = null;
		}
		
		if(!plugin.contains("/")){
			return getPluginJarPath(pluginFolder, plugin, version);
		}else{
			String[] paths = plugin.split("/");
			return getPluginJarPath(pluginFolder, paths, version);
		}
		
	}
	
	private static String getPluginJarPath(File pluginFolder, String[] paths,
			String version) {
		File [] fileList = pluginFolder.listFiles();
		String plugin = paths[0];
		NavigableMap<String, File> map = new TreeMap<String, File>();
		for(File file:fileList){
			if(!file.isDirectory()){
				continue;
			}
			String filename = file.getName();
			String time="";
			if(filename.contains(plugin) && 
					filename.contains(version)){
				filename = filename.replace(plugin, "").
						replace(version, "");
				
				// gets the time stamp of the jar file
				for(int i=0;i<filename.length();i++){
					if(Character.isDigit(filename.charAt(i))){
						time+=filename.charAt(i);
					}
				}
				map.put(time, file);
			}
		}
		File dir = map.get(map.lastKey());
		if(dir!=null){
			String path = dir.getName();
			EasySocialLogger.log(Level.INFO, "looking for jar files in "+path);
			for(int i=1;i<paths.length;i++){
				path+=("/"+paths[i]);
			}
			return path;
		}else{
			String msg = "Failed to find a required plugin: "+plugin; 
			EasySocialLogger.log(Level.SEVERE, msg);
			throw new IllegalStateException(msg);
		}

		
	}

	private static String getPluginJarPath(File pluginFolder, String plugin,
			String version) {
		File [] fileList = pluginFolder.listFiles();
		
		NavigableMap<String, String> map = new TreeMap<String, String>();
		for(File file:fileList){
			if(file.isDirectory()){
				continue;
			}
			String filename = file.getName();
			String time="";
			if(filename.contains(plugin) && 
					filename.contains(version)){
				filename = filename.replace(plugin, "").
						replace(version, "");
				
				// gets the time stamp of the jar file
				for(int i=0;i<filename.length();i++){
					if(Character.isDigit(filename.charAt(i))){
						time+=filename.charAt(i);
					}
				}
				map.put(time, file.getName());
			}
		}
		String path;
		if(map.size()!=0 && map.get(map.lastKey())!=null){
			return map.get(map.lastKey());
		}else{
			String msg = "Failed to find a required plugin: "+plugin; 
			EasySocialLogger.log(Level.SEVERE, msg);
			throw new IllegalStateException(msg);
		}
	}

	public static void init(){
		PluginFiles.init(Activator.getDefault().getBundle());
		EasySocialLogger.log(Level.INFO, "Found project bundle.");

		URL url = Activator.getDefault().getBundle().getEntry(
				EasySocialEclipseProperties.EASYSOCIAL_PROPERTIES);
		
		EasySocialLogger.log(Level.INFO, "Properties url is "+url.toString());
		
		try {
			Properties properties = PluginUtils.loadProperties(url.openStream());
			
			EasySocialLogger.log(Level.INFO, "Properties is "+properties);
			
			Activator.getDefault().setProperties(properties);
			Activator.dataServices = DataServices.loadDataServices();
			EasySocialLogger.log(Level.INFO, "No error found in "+url);
		} catch (IOException e) {
			EasySocialLogger.log(Level.SEVERE, "Failed to load properties at "+url, e);
			throw new IllegalStateException(e);
		}
	}

	public static List<String> locateDependencyLibs() {
		StringTokenizer st = new StringTokenizer(Activator.getDefault().
				getProperty(EasySocialEclipseProperties.DEPENDENCIES),", \t\n\r");
		List<String> _bundles = new ArrayList<String>();
		
		while(st.hasMoreTokens()){
			_bundles.add(st.nextToken());
		}
		Map<String,String> bundles = new HashMap<String,String>();
		for(int i=0;i<_bundles.size()/2;i++){
			bundles.put(_bundles.get(2*i), _bundles.get(2*i+1));
		}
		
		List<String> libs = new ArrayList<String>();
		if(bundles.isEmpty()){
			return libs;
		}
		
		Set<String> keys = bundles.keySet();
		for(String key:keys){
			String lib = String.format("%s/%s",
					System.getProperty("user.dir")+(getOS().equals("mac")?"/../../..":"")+"/plugins",
					PluginUtils.getPluginJarPath(key,bundles.get(key)));
			EasySocialLogger.log(Level.INFO,"locateLibs:"+lib);
			libs.add(lib);
		}
		return libs;
	}
	
	public static Set<String> findExistingDbs(IJavaProject javaProject){
		Set<String> existingDbs = new HashSet<String>();
		if(javaProject==null){
			return existingDbs;
		}
		
		String path = String.format("%s/%s", 
				javaProject.getProject().getLocation().toString(), EasySocialEclipseProperties.DB_FILE);
		EasySocialLogger.log(Level.INFO, "findExistingDbs: "+path);
		File file = new File(path);
		if(!file.exists()){
			return existingDbs;
		}
		
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(file));
			String dbs[] = ((String)properties.get("db_names")).split(",");
			for(int i=0;i<dbs.length;i++){
				EasySocialLogger.log(Level.INFO, "findExistingDbs found "+dbs[i]);
				existingDbs.add(dbs[i].substring(dbs[i].lastIndexOf('/')+1, dbs[i].length()));
			}
			return existingDbs;
		} catch (IOException e) {
			return null;
		}
	}
}
