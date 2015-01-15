package com.easysocial.downloader;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

import com.easysocial.logging.EasySocialLogger;
import com.easysocial.oauthclient.OauthClient;
import com.easysocial.oauthclient.OauthParameters;
import com.easysocial.packing.DataPacker;
import com.easysocial.saver.DataSaver;
import com.easysocial.types.downloaded.DownloadedObject;

/**
 * Implementation defines what and how data is downloaded from data source.
 * 
 * This class needs to use a client program which concerns lower level action, 
 * such as authorization, multithreading, etc.
 * 
 * @author Dahai Guo
 *
 */
public abstract class DataDownloader {
	protected OauthClient client;
	protected DataPacker packer;
	protected OauthParameters params;
	protected DataDownloader(OauthClient client, DataPacker packer, 
			OauthParameters params){
		this.client = client;
		this.packer = packer;
		this.params = params;
	}
	
	public abstract void download();
	protected void pack(Collection<? extends DownloadedObject> objs){
		EasySocialLogger.log(Level.INFO,"DataDownloader.pack: "+packer);
		packer.pack(objs);
	}
}
