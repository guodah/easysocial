package com.easysocial.integrator;

import java.util.Map;




import java.util.logging.Level;

import com.easysocial.downloader.DataDownloader;
import com.easysocial.downloader.FBDownloader;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.oauthclient.OauthParameters;
import com.easysocial.packing.DataPacker;
import com.easysocial.saver.DataSaver;
import com.easysocial.saver.sql.SQLDataSaver;

public class FBDataIntegrator extends DataIntegrator{

	public FBDataIntegrator(OauthParameters params,
			String dbName, String dbPath){
		super(params, dbName, dbPath);
		saver = SQLDataSaver.newInstance(dbName, dbPath);
		packer = DataPacker.newInstance(saver);
		EasySocialLogger.log(Level.INFO, 
				"FBDataIntegrator: saver is "+saver);
		EasySocialLogger.log(Level.INFO, 
				"FBDataIntegrator: packer is "+packer);

		EasySocialLogger.log(Level.INFO, "Facebook integrator receives: "+params.toString());
		
		downloader = FBDownloader.newInstance(params, packer);
	}

}
