package com.easysocial.packing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import com.easysocial.converter.Converter;
import com.easysocial.logging.EasySocialLogger;
import com.easysocial.saver.DataSaver;
import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.packed.SavableObject;

/**
 * DataPacker converts DownloadedObject objects to SavableObject objects. Because
 * downloading may use third-party libraries to download oauth data source, some 
 * downloaded objects may not be easy to save to database. DataPacker will normalize
 * these objects and find a list of SavableObject objects, saving which to database is
 * easier.
 * 
 * @author Dahai Guo
 *
 */
public class DataPacker {
	protected DataSaver saver;
	
	/**
	 * A converter map where each downloaded object type is registered a list of 
	 * converters 
	 */
	private Map<Class<?extends DownloadedObject>, 
		List<Converter>> converters;
	
	
	private DataPacker(
			Map<Class<? extends DownloadedObject>, 
				List<Converter>> converters,
			DataSaver saver) {
		this.converters = converters;
		this.saver = saver;
	}

	/**
	 * Builds DataPacker from converters.properties
	 * 
	 * Scans converters.properties for converter list for downloaded object types.
	 * @return
	 */
	public static DataPacker newInstance(DataSaver saver){
				
		Properties properties = new Properties();
		try {
			properties.load(DataPacker.class.getResourceAsStream("converters.properties"));
			DataPacker packer = newInstance(properties, "types.downloaded", saver);
			return packer;
		} catch (Exception e) {
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to build DataPacker",e);
			return null;
		}
	}
	
	private static DataPacker newInstance(Properties properties,
			String property, DataSaver saver) throws ClassNotFoundException, 
			InstantiationException, IllegalAccessException{
		Enumeration<String> propertyNames = (Enumeration<String>) 
				properties.propertyNames();
		
		Map<Class<?extends DownloadedObject>, List<Converter>> converters = 
				new HashMap<Class<?extends DownloadedObject>, List<Converter>>();
		
		while(propertyNames.hasMoreElements()){
			String className = propertyNames.nextElement();
			if(!className.contains(property)){
				continue;
			}
			
			String [] converterNames = properties.getProperty(className).split(",");
			
			List<Converter> converterList = new ArrayList<Converter>();
			Class<? extends DownloadedObject> cl = (Class<? extends DownloadedObject>) 
					Class.forName(className);
			
			for(String converterName:converterNames){
				converterList.add(
						(Converter) Class.forName(converterName).newInstance()
				);
			}
			converters.put(cl, converterList);
		}
		return new DataPacker(converters, saver);
	}
	
	public void pack(DownloadedObject obj){
		List<SavableObject> result = new ArrayList<SavableObject>();
		
		List<Converter> converterList = converters.get(obj.getClass());
		if(converterList==null){
			EasySocialLogger.log(Level.SEVERE, 
					"Failed to find converters for "+obj.getClass().getName());
		}
		for(Converter converter:converterList){
			result.addAll(converter.convert(obj));
		}
		saver.save(result);
	}
	
	public void pack(Collection<? extends DownloadedObject> objs){
	//	List<SavableObject> result = new ArrayList<SavableObject>();
		for(DownloadedObject obj:objs){
	//		result.addAll(pack(obj));
			pack(obj);
		}
	//	return result;
	}
	
	public void close(){
		saver.close();
	}
}
