package com.easysocial.integrator;

import java.util.Map;
import java.util.Collection;
import java.util.logging.Level;

import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.packed.SavableObject;
import com.easysocial.downloader.DataDownloader;
import com.easysocial.downloader.FBDownloader;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.oauthclient.OauthParameters;
import com.easysocial.packing.DataPacker;
import com.easysocial.saver.DataSaver;
import com.easysocial.saver.sql.SQLDataSaver;

/**
 * Performs three actions
 * <ol>
 * <li> Downloads data from a data source
 * <li> Packs data to be compatible for saving to database
 * <li> Saves the data to a database
 * </ol>
 * @author Dahai Guo
 *
 */
public class DataIntegrator {
	protected DataDownloader downloader; 
	protected DataPacker packer;
	protected DataSaver saver;
	
	protected DataIntegrator(OauthParameters params, String dbName, String dbPath){
		downloader = null;
		packer = null;
		saver = null;
	}
	
	public void integrate(){
		if(downloader==null || packer==null || saver==null){
			EasySocialLogger.log(Level.SEVERE, 
					"Cannot integrate data because downloader, packer or saver is null");
			return;
		}
		Runtime runtime = Runtime.getRuntime();
//		System.out.println((runtime.totalMemory() - runtime.freeMemory())/1000000);
		EasySocialLogger.log(Level.INFO, "Memeory Usage before download: "+
				(runtime.totalMemory() - runtime.freeMemory())/1000000+" MB");
		downloader.download();
		EasySocialLogger.log(Level.INFO, "Memeory Usage after download: "+
				(runtime.totalMemory() - runtime.freeMemory())/1000000+" MB");

//		System.out.println((runtime.totalMemory() - runtime.freeMemory())/1000000);
/*
		Collection<SavableObject> savables = packer.pack(objs);
		objs.clear();
		System.gc();
		EasySocialLogger.log(Level.INFO, "Memeory Usage before save: "+
				(runtime.totalMemory() - runtime.freeMemory())/1000000+" MB");
//		System.out.println((runtime.totalMemory() - runtime.freeMemory())/1000000);
		saver.save(savables);
		savables.clear();
		System.gc();
		EasySocialLogger.log(Level.INFO, "Memeory Usage after save: "+
				(runtime.totalMemory() - runtime.freeMemory())/1000000+" MB");
//		System.out.println((runtime.totalMemory() - runtime.freeMemory())/1000000);
*/ 
 
	}
}
