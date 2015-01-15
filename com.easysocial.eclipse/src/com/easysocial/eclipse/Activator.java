package com.easysocial.eclipse;

import java.io.File;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.easysocial.eclipse.EasySocialPluginLogger;
import com.easysocial.eclipse.DataServices;
import com.easysocial.logging.EasySocialLogger;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
//	public static final String PLUGIN_ID = "com.easysocial.eclipse"; //$NON-NLS-1$
	public static final String PLUGIN_ID = "com.easysocial.eclipse_1.0.0.qualifier"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * Controls all the actions related to authorization with data sources
	 */
	public static DataServices dataServices=null;
	
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		EasySocialLogger.addLogHandler(new EasySocialPluginLogger());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Global accessible properties
	 */
	private  Properties properties;
	
	/**
	 * Accesses the globally accessible properties
	 * 
	 * @param key key name
	 * @return
	 */
	public  String getProperty(String key){
		return properties.getProperty(key);
	}

	/**
	 * Sets the globally accessible properties
	 * 
	 * @param prop the properties
	 */
	public  void setProperties(Properties prop){
		properties = prop;
	}
	
	public static void main(String args[]) throws IOException{
	}
}
