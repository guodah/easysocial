package com.easysocial.types.packed;

/**
 * A maker interface. DataSaver only accepts SavableObject objects. 
 * DataPacker packs DownloadedObject to SavableObject when converters
 * are available.
 * 
 * @author Dahai Guo
 *
 */
public interface SavableObject {
	String getTableName();
}
