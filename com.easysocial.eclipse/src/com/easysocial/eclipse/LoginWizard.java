package com.easysocial.eclipse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.easysocial.eclipse.oauth.CompleteHandler;
import com.easysocial.logging.EasySocialLogger;

/**
 * The login page where user logs in to their accounts with data sources for 
 * an access token.
 * 
 * @author Dahai Guo
 *
 */
public class LoginWizard extends WizardPage{
	
	private class CompletePageHandler implements CompleteHandler{

		private WizardPage page;
		private Map<String, Map<String, String>> tokens;
		CompletePageHandler(WizardPage page){
			this.page = page;
			tokens = new HashMap<String, Map<String, String>>();
		}
		
		@Override
		public void complete(Object obj) {
			EasySocialLogger.log(Level.INFO, "Complete handler completes");
			page.setPageComplete(true);
			tokens = (Map<String, Map<String, String>>) obj;
			resetWizardPage();
		}
		
		private void resetWizardPage(){
/*
			Composite component = (Composite) page.getControl();
			for(Control control:component.getChildren()){
				if(control instanceof Browser){
					control.dispose();
					break;
				}
			};
*/	 
			String summary="";
			if(tokens.size()==0){
				summary += Activator.getDefault().getProperty(
						EasySocialEclipseProperties.LOGIN_SUMMARY_NO_DATA);
			}else{
				summary += Activator.getDefault().getProperty(
						EasySocialEclipseProperties.LOGIN_SUMMARY_DATA);
				Iterator<String> keys = tokens.keySet().iterator();
				do{
					summary += keys.next()+
							((keys.hasNext())?", ":".");
				}while(keys.hasNext());
				summary += " Please click \"Next\" to continue!";
			}
/*			
			Label label = new Label(component, SWT.WRAP);
	        label.setText(summary);
	        
	        component.pack();
	        component.redraw();
*/
			((LoginWizard)page).getBrowser().setText(summary);
		}

		@Override
		public Object pickUp() {
			return tokens;
		}
	}

	private CompleteHandler completeHandler;
	
	private Browser browser;
	
	protected LoginWizard() {
		super(Activator.getDefault().getProperty(
				EasySocialEclipseProperties.LOGIN_PAGE_NAME));
		this.setTitle(Activator.getDefault().getProperty(
				EasySocialEclipseProperties.LOGIN_PAGE_TITLE));
		this.setDescription(Activator.getDefault().getProperty(
				EasySocialEclipseProperties.LOGIN_PAGE_DESCRIPTION));
		
		completeHandler = new CompletePageHandler(this);
	}
	
	public void setNewControl(Composite container) {
		setControl(container);
	}

	public Browser getBrowser(){
		return browser;
	}

	/**
	 * Finds the access token.
	 * @return
	 */
	public Map<String, Map<String, String>> getAccessTokens(){
		return (Map<String, Map<String, String>>) completeHandler.pickUp();
	}
	
	private Composite initControl=null;
	
	@Override
	public void createControl(Composite parent) {
		initControl = new Composite(parent, SWT.NULL);
		
		initControl.setLayout(new FillLayout());
		this.setPageComplete(false);

//		Browser.clearSessions();
//		browser = new Browser(initControl, SWT.NONE);
		
		BrowserWithStatusBar browserStatus = new BrowserWithStatusBar(initControl,SWT.NONE);
		browser = browserStatus.getBrowser();
		
		setControl(initControl);
		EasySocialLogger.log(Level.INFO, "createControl done");
	}

	public CompleteHandler getCompleteHandler() {
		return completeHandler;
	}

	public void initControl() {
		EasySocialLogger.log(Level.INFO, "In initControl: "+initControl);
		setControl(initControl);
	}


}
