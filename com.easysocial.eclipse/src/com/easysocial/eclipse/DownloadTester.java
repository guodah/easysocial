package com.easysocial.eclipse;

import java.util.logging.Level;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;

import com.easysocial.logging.EasySocialLogger;

public class DownloadTester extends PropertyTester{

	@Override
	public boolean test(Object object, String property, Object[] args,
			Object expectedValue) {

		if(!(object instanceof IJavaProject)){
			EasySocialLogger.log(Level.INFO, "DownloadTester: not selecting java project");
			return false;
		}
		
		EasySocialLogger.log(Level.INFO, "Tester property is "+property);
		
		return true;
	}

}
