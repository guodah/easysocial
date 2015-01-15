package com.easysocial.eclipse;

import java.io.ByteArrayInputStream;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.Bundle;

import com.easysocial.eclipse.oauth.CompleteHandler;
import com.easysocial.eclipse.oauth.OauthBrowserControl;
import com.easysocial.eclipse.oauth.OauthService;
import com.easysocial.logging.EasySocialLogger;

/**
 * Implements the project creation wizard.
 * 
 * In this wizard, clients will first choose which data sources he would access 
 * in his programming. Then he will guided to login to his accounts with these accounts.
 * Then the wizard continues with normal process of creating a Java project.
 * 
 * @author Dahai Guo
 *
 */
public class EasySocialProject extends Wizard implements INewWizard, IPageChangingListener {
	private static final String WIZARD_NAME_NEW_PROJECT = "EasySocial Java Project";
	private static final String WIZARD_NAME_DOWNLOAD = "Download Your Social Network Data";
	
	private IJavaProject javaProject = null;
	/**
	 * Where clients choose which data sources he would access
	 */
	private IntroPage introPage;
	
	/**
	 * Where clients login to his accounts with the data sources
	 */
	private LoginWizard loginWizard;
	
	/**
	 * Normal page one for Java project creation
	 */
	private NewJavaProjectWizardPageOne _pageOne;
	
	/**
	 * Normal page two for Java project creation
	 */
	private NewJavaProjectWizardPageTwo _pageTwo;
	
	public EasySocialProject(IJavaProject javaProject){
		this.javaProject = javaProject;
	}
	
	private boolean finished=false;
	
	public EasySocialProject() {
	    setWindowTitle(WIZARD_NAME_NEW_PROJECT);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		PluginUtils.init();
	}

	@Override
	public boolean canFinish(){
		if(javaProject!=null){
			return loginWizard.isPageComplete();
		}
		
		if(getContainer().getCurrentPage() == _pageOne){
			return _pageOne.isPageComplete();
		}else if(getContainer().getCurrentPage() == _pageTwo ){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	@Override
	public boolean performFinish() {
		EasySocialLogger.log(Level.INFO, "performFinish: "+finished);
		if(finished){
			return false;
		}
		EasySocialLogger.log(Level.INFO, "performFinish: entered");
		
		finished = true;		
		
		Map<String, Map<String, String>> tokens =
				loginWizard.getAccessTokens();
		IProgressMonitor monitor = new NullProgressMonitor();
		Set<String> existingDbs = PluginUtils.findExistingDbs(javaProject);
		try{
						
			if(javaProject==null){
				EasySocialLogger.log(Level.INFO, "performFinish: pageTwo.performFinish called");
				// complete _pageTwo
				_pageTwo.performFinish(monitor);
				EasySocialLogger.log(Level.INFO, "performFinish: pageTwo.performFinish done");
				// get the project
				javaProject= _pageTwo.getJavaProject();
			}
					
			
			IProject project = javaProject.getProject();
			
			EasySocialLogger.log(Level.INFO, "performFinish: pointer to project located");
			
			if(tokens!=null && tokens.size()>0){

				// add easysocial folder
				IFolder easysocialFolder = project.getFolder(EasySocialEclipseProperties.FOLDER_NAME);
				if(!easysocialFolder.exists()){
					easysocialFolder.create(true, true, monitor);
				}
				
				// add the token file to the project
//				IFile fb = project.getFile(EasySocialEclipseProperties.TOKEN_FILE);
//				fb.create(new ByteArrayInputStream(
//					getTokenFileContent(tokens).getBytes()), true, monitor);
//				EasySocialLogger.log(Level.INFO, "performFinish: token file added");
				
				// add the db.properties
				IFile db = project.getFile(EasySocialEclipseProperties.DB_FILE);
				if(db.exists()){
					db.setContents(new ByteArrayInputStream(getDBProperties(tokens.keySet(), existingDbs).getBytes()), true, true, monitor);
					EasySocialLogger.log(Level.INFO, "performFinish: db.properties modified");
				}else{
					db.create(new ByteArrayInputStream(getDBProperties(tokens.keySet(), existingDbs).getBytes()), true, monitor);
					EasySocialLogger.log(Level.INFO, "performFinish: db.properties added");
				}
			}
			
			// add the dependency libs to the new project
			List<String> libs = PluginUtils.locateDependencyLibs();
			PluginUtils.addMoreLibs(javaProject, libs, monitor);
						
			EasySocialLogger.log(Level.INFO, "performFinish: dependency libs added");
			
			// refresh the project
			project.refreshLocal(2, monitor);

			if(tokens!=null && tokens.size()>0){
				deleteObsoleteDbs(project, tokens.keySet(), existingDbs);
				
				Activator.getDefault().dataServices.integrate(tokens.keySet(),
					project.getLocation()+"/"+EasySocialEclipseProperties.DB_PATH);
			}
			EasySocialLogger.log(Level.INFO, "performFinish: almost done, before return");
		}catch(Exception e){
			String msg = "Errors in creating the EasySocial Java project";
			EasySocialLogger.log(Level.SEVERE, msg, e);
			return false;
		}

		return true;
	}

	private void deleteObsoleteDbs(IProject project, Set<String> newDataSources, Set<String> existingDbs) {
		Set<String> deletes = new HashSet<String>();
		for(String exist:existingDbs){
			if(newDataSources.contains(exist)){
				deletes.add(exist);
			}
		}
		
		if(deletes.isEmpty()){
			return;
		}
		
		String path = project.getLocation().toString()+
				"/"+EasySocialEclipseProperties.DB_PATH;
		EasySocialLogger.log(Level.INFO, "db is in "+path);
		File dbFolder = new File(path);
		if(!dbFolder.exists() || !dbFolder.isDirectory()){
			EasySocialLogger.log(Level.INFO, "Not able to locate obsolete dbs!!");
			return;
		}else{
			File [] list = dbFolder.listFiles();
			for(File f:list){
				for(String delete:deletes){
					if(f.getName().contains(delete)){
						f.delete();
					}
				}
			}
		}
	}

	private String getDBProperties(Set<String> newDataSources, Set<String> existingDbs) {
		Set<String> all = new HashSet<String>();
		all.addAll(newDataSources);
		all.addAll(existingDbs);
		
		String content = "db_names=";
		for(String key:all){
			content+=EasySocialEclipseProperties.DB_PATH+"/"+key+",";
		}
		return content.substring(0,content.length()-1);
	}

	private String getTokenFileContent(Map<String, Map<String, String>> tokenMap) {
		String content="";
		Set<String> keys = tokenMap.keySet();
		for(String key:keys){
			Map<String,String> tokens = tokenMap.get(key);
			Set<String> keys2 = tokens.keySet();
			for(String key2:keys2){
				content += String.format("%s_%s=%s\n",key,key2,tokens.get(key2));
			}
		}
		return content;
	}

	@Override
	public void addPages() {
	    _pageOne = new NewJavaProjectWizardPageOne();
//	    _pageTwo = new NewJavaProjectWizardPageTwo(_pageOne);
	    _pageTwo = new MyJavaTwo(_pageOne);

		
		loginWizard = new LoginWizard();
		introPage = new IntroPage(loginWizard, 
				_pageOne, javaProject);

	    addPage(introPage);
	    addPage(loginWizard);
	    if(javaProject==null){
	    	addPage(_pageOne);
	    	addPage(_pageTwo);
	    }else{
	    	_pageOne.dispose();
	    	_pageTwo.dispose();
	    }
	}

	@Override
	public void setContainer(IWizardContainer container){
			super.setContainer(container);
		    WizardDialog dialog = (WizardDialog)container;
		    if(dialog!=null)
		    	dialog.addPageChangingListener(this);
	}
	
	/**
	 * Only responds to the page change from introduction page to login wizard.
	 * 
	 * Gets the list of data sources that the client would access and starts
	 * the authorization process on loginWizard
	 */
	@Override
	public void handlePageChanging(PageChangingEvent event) {
		
		if(introPage.currentPage() && event.getTargetPage()==loginWizard){
			List<String> serviceClasses = introPage.getSelectedServices();
			Map<String, OauthService> services = Activator.getDefault().
					dataServices.getOauthServices(serviceClasses);
			Browser browser = loginWizard.getBrowser();
			CompleteHandler completeHandler = loginWizard.getCompleteHandler();
			
	//		System.out.println(services.size());
			
			OauthBrowserControl oauth = OauthBrowserControl.build(browser, services,
					completeHandler);
			if(oauth!=null){
				EasySocialLogger.log(Level.INFO, services.size()+" data sources being connected.");
				oauth.restart();
			}else{
				String msg = "Failed to build the OauthBrowserControl";
				EasySocialLogger.log(Level.SEVERE, msg);
				throw new IllegalStateException(msg);
			}
		}
	}

	
}
