package com.easysocial.converter;

import java.util.Collection;

import com.easysocial.types.downloaded.DownloadedObject;
import com.easysocial.types.packed.SavableObject;

/**
 * Implementations need to define how a object downloaded from a data source
 * is converted to compatible objects for saving to database. When downloading
 * from a data source, a third-party library may be used. Then these objects may
 * not be easy to save to database.
 * 
 * @author Dahai Guo
 *
 */
public interface Converter {
	Collection<SavableObject> convert(DownloadedObject obj); 
}
