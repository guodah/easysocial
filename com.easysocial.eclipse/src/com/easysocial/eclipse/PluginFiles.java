package com.easysocial.eclipse;

import java.io.IOException;
import java.io.InputStream;

import org.osgi.framework.Bundle;

/**
 * A convenience class for get hold of files in the plugin bundle.
 * @author Dahai Guo
 *
 */
public class PluginFiles {
	private static Bundle bundle;
	
	public static void init(Bundle _bundle){
		bundle = _bundle;
	}
	
	public static InputStream findItem(String name){
		try {
			return bundle.getEntry(name).openStream();
		} catch (IOException e) {
			return null;
		}
	}
	
	public static InputStream findLib(String name){
		return findItem("lib/"+name);
	}
}
