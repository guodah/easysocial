package com.easysocial.saver;

import java.util.Collection;

import com.easysocial.types.packed.SavableObject;

/**
 * Implementations define how SavableObject objects are saved to a database.
 * 
 * @author Dahai Guo
 *
 */
public interface DataSaver {
	void save(Collection<SavableObject> objs);
	void close();
}
