package com.easysocial.eclipse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.easysocial.integrator.DataIntegrator;
import com.easysocial.integrator.FBDataIntegrator;
import com.easysocial.integrator.UpdateObserver;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.oauthclient.OauthParameters;

/**
 * A parallel job in the EasySocial plugin for downloading Facebook data.
 * @author Dahai Guo
 *
 */
public class Downloader extends Job{

	private OauthParameters params;
	private String dbName = null;
	private String dbPath = null;
	private Class<? extends DataIntegrator> integratorClass;
	public Downloader(Class<?extends DataIntegrator> integratorClass,
			OauthParameters params, 
			String dbName, String dbPath) {
		super("Downloading "+dbName+" Data...");	
		
		if(params==null || dbName==null || dbPath==null 
				|| integratorClass==null){
			EasySocialLogger.log(Level.WARNING, 
					String.format("Invalid parameters: %s=%s, %s=%s, %s=%s, %s=%s",
							"integrator", integratorClass,
							"Oauth Parameters", params,
							"dbName", dbName,
							"dbPath", dbPath));
		}
		
		this.params = params;
		this.dbName = dbName;
		this.dbPath = dbPath;
		this.integratorClass = integratorClass;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if(params==null || dbName==null || dbPath==null 
				|| integratorClass==null){
			return Status.CANCEL_STATUS;
		}
		
		DataIntegrator integrator;
		try {
			integrator = integratorClass.getConstructor(
					OauthParameters.class, String.class, String.class).
					newInstance(params, dbName, dbPath);
			integrator.integrate();
			monitor.done();
			return Status.OK_STATUS;

		} catch (InstantiationException e){
			EasySocialLogger.log(Level.WARNING, String.format(
					"Downloader failed: %s=%s, %s=%s, %s=%s, %s=%s",
						"integrator", integratorClass,
						"Oauth Parameters", params,	
						"dbName", dbName,
						"dbPath", dbPath),e);
			return Status.CANCEL_STATUS;			
		} catch (IllegalAccessException e){
			EasySocialLogger.log(Level.WARNING, String.format(
					"Downloader failed: %s=%s, %s=%s, %s=%s, %s=%s",
						"integrator", integratorClass,
						"Oauth Parameters", params,	
						"dbName", dbName,
						"dbPath", dbPath),e);
			return Status.CANCEL_STATUS;
		} catch (IllegalArgumentException e){
			EasySocialLogger.log(Level.WARNING, String.format(
					"Downloader failed: %s=%s, %s=%s, %s=%s, %s=%s",
						"integrator", integratorClass,
						"Oauth Parameters", params,	
						"dbName", dbName,
						"dbPath", dbPath),e);
			return Status.CANCEL_STATUS;
		} catch (InvocationTargetException e){
			EasySocialLogger.log(Level.WARNING, String.format(
					"Downloader failed: %s=%s, %s=%s, %s=%s, %s=%s",
						"integrator", integratorClass,
						"Oauth Parameters", params,	
						"dbName", dbName,
						"dbPath", dbPath),e);
			return Status.CANCEL_STATUS;
		} catch (NoSuchMethodException e){
			EasySocialLogger.log(Level.WARNING, String.format(
					"Downloader failed: %s=%s, %s=%s, %s=%s, %s=%s",
						"integrator", integratorClass,
						"Oauth Parameters", params,	
						"dbName", dbName,
						"dbPath", dbPath),e);
			return Status.CANCEL_STATUS;
		} catch (SecurityException e){
			EasySocialLogger.log(Level.WARNING, String.format(
					"Downloader failed: %s=%s, %s=%s, %s=%s, %s=%s",
						"integrator", integratorClass,
						"Oauth Parameters", params,	
						"dbName", dbName,
						"dbPath", dbPath),e);
			return Status.CANCEL_STATUS;
		}

	}
}
