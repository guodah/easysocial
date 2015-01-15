package com.easysocial.eclipse.oauth;

/**
 * Implementation of this class should define what needs to be done
 * when a process ends and what the user should pick up at that time.
 * 
 * @author Dahai Guo
 *
 */
public interface CompleteHandler {
	public Object pickUp();
	void complete(Object obj);
}
