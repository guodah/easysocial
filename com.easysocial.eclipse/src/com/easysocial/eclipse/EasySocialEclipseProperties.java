package com.easysocial.eclipse;

import org.eclipse.core.runtime.IPath;

/**
 * Most of the properties are in easysocial.properties
 * @author Dahai Guo
 *
 */
public class EasySocialEclipseProperties {
	
	/**
	 * The property file which stores the following information:
	 * <ol>
	 * <li> oauth information: client_id, client_secret, etc.
	 * <li> dependencies for the plugin
	 * <li> resource files for the plugin
	 * <li> a list of data sources
	 * <li> strings used in the wizard
	 * </ol>
	 */
	public static final String EASYSOCIAL_PROPERTIES = "resources/easysocial.properties";

	/**
	 * Data sources
	 */
	public static final String OAUTH_SOURCES = "oauth_sources";
	
	/**
	 * OAuth parameter
	 */
	public static final String CLIENT_ID = "client_id";

	/**
	 * OAuth parameter
	 */
	public static final String CLIENT_SECRET = "client_secret";
	
	/**
	 * OAuth parameter
	 */
	public static final String SCOPE = "scope";
	
	/**
	 * OAuth parameter
	 */
	public static final String REDIRECT_URI = "redirect_uri";
	
	/**
	 * Property name for a subclass of OauthService for a specific data source
	 */
	public static final String OAUTH_SERVICE = "oauth_service";
	
	/**
	 * Data source description used in the plugin
	 */
	public static final String DATASOURCE_DESCRIPTION = "description";
	
	public static final String FOLDER_NAME = "easysocial";
	
	/**
	 * The file name that stores all the tokens
	 */
	public static final String TOKEN_FILE = FOLDER_NAME+"/tokens_file";
	
	/**
	 * The file name that stores database related information
	 */
	public static final String DB_FILE = FOLDER_NAME+"/db.properties";
	
	/**
	 * Dependecies for locating libs
	 */
	public static final String DEPENDENCIES = "dependencies";
	
	/**
	 * Path for downloaded data 
	 */
	public static final String DB_PATH = FOLDER_NAME+"/db";
	
	/**
	 * Property name for the class name that integrates data from data sources
	 */
	public static final String DATA_INTEGRATOR = "integrator";
	
	public static final String INTEGRATOR_PARAMETERS = "integrator_params";
	public static final String INTEGRATOR_PARAMETERS_DESCRIPTION = "integrator_params_description";
	
	public static final String LOGIN_PAGE_TITLE="login_page_title";
	public static final String LOGIN_PAGE_DESCRIPTION="login_page_description";
	public static final String LOGIN_PAGE_NAME="login_page_name";
	public static final String LOGIN_SUMMARY_DATA="login_summary_data";
	public static final String LOGIN_SUMMARY_NO_DATA="login_summary_no_data";


}
