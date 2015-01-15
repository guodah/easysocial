package com.easysocial.eclipse;

import java.util.logging.Level;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.easysocial.logging.EasySocialLogHandler;


/**
 * Logger which is for the plugin.
 * 
 * @author Dahai guo
 *
 */
public class EasySocialPluginLogger implements EasySocialLogHandler{
	private static void logInfo(String message) {
		log(IStatus.INFO, message, null);
	}

	private static void logError(String message, Throwable exception) {
		log(IStatus.ERROR,  message, exception);
	}

	private static void log(int severity, String message,
			Throwable exception) {
		log(createStatus(severity,  message, exception));
	}

    private static IStatus createStatus(int severity, String message,
			Throwable exception) {
		return new Status(severity, Activator.PLUGIN_ID,
				message, exception);
	}

	private static void log(IStatus status) {
		Activator.getDefault().getLog().log(status);
	}

	@Override
	public void log(Level level, String msg) {
		log(level,msg,null);
	}

	@Override
	public void log(Level level, String msg, Throwable thrown) {
		if(level==Level.SEVERE){
			logError(msg, thrown);
		}else if(level==Level.WARNING){
			log(IStatus.WARNING, msg, null);
		}else{
			logInfo(msg);
		}
	}

}
