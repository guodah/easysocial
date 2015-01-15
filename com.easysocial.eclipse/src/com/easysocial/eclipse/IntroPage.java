package com.easysocial.eclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;












import org.eclipse.core.resources.IFolder;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.easysocial.logging.EasySocialLogger;

/**
 * Where users choose which data sources they would access in their 
 * project
 * 
 * @author Dahai Guo
 *
 */
public class IntroPage extends WizardPage{
	
	private Set<String> existingDbs; 
	
	private class DataSourceSelectionListener extends SelectionAdapter{
		 	private final Display display = Display.getCurrent();
			private IntroPage parent;

			public DataSourceSelectionListener(IntroPage parent){
				this.parent = parent;
			}
			
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		    	Button button = ((Button)e.getSource());
		    	
		    	if(button.getSelection()){
		    		checkExist(button.getText());
		    		parent.getSelectedServices().add(button.getText());
		    		integratorParamButtons.get(button.getText()).setEnabled(true);
		    		integratorParamsDescs.get(button.getText()).setEnabled(true);
		    	}else{
		    		parent.getSelectedServices().remove(button.getText());
		    		integratorParamButtons.get(button.getText()).setEnabled(false);
		    		integratorParamsDescs.get(button.getText()).setEnabled(false);;
		    	}
		    	
		    	if(parent.javaProject!=null && parent.getSelectedServices().isEmpty()){
		    		EasySocialLogger.log(Level.INFO, "intro page set complete false");
		    		parent.setPageComplete(false);
		    	}else{
		    		EasySocialLogger.log(Level.INFO, "intro page set complete true");
		    		parent.setPageComplete(true);
		    	}
		    }
	}
	 
	 private class LimitAccessSelectionListener extends SelectionAdapter{
		 private String dataSource;
		 public LimitAccessSelectionListener(String dataSource){
			 this.dataSource = dataSource;
		 }
		 
		 @Override
		 public void widgetSelected(SelectionEvent e) {
			Button button = ((Button)e.getSource());
			Activator.getDefault().dataServices.getParams(dataSource).
				setLimitAccess(button.getSelection());
		 }
	 }

	private LoginWizard loginWizard;
	private NewJavaProjectWizardPageOne _pageOne;
	private Composite container;
	private SelectionAdapter listener;
	private Button [] dataSourceSelection;
	private Map<String, Button> integratorParamButtons;
	private Map<String, Label> integratorParamsDescs;
	private IJavaProject javaProject;
	private List<String> selectedSources;
	
	protected IntroPage(LoginWizard loginWizard, 
			NewJavaProjectWizardPageOne _pageOne, IJavaProject javaProject) {
		super("Intro");
		
		this.loginWizard = loginWizard;
		this._pageOne = _pageOne;
		this.javaProject = javaProject;
		
		setTitle("Data sources");
	    setDescription("Please choose data source(s) "+
	    		"where you have an account with and would acccess in the project. "+
	    		"You could select none to just create a regular Java project.");
	    selectedSources = new ArrayList<String>();
	    listener = new DataSourceSelectionListener(this);
	    integratorParamButtons = new HashMap<String, Button>();
	    integratorParamsDescs = new HashMap<String, Label>();
	    
	    if(javaProject!=null)
	    	this.setPageComplete(false);
	    else
	    	this.setPageComplete(true);
	    
	}

	private void checkExist(String db) {
		if(this.existingDbs==null){
			this.existingDbs = PluginUtils.findExistingDbs(javaProject);
		}
		
		if(existingDbs.contains(db)){
			MessageDialog.openWarning(this.getShell(), "Warning...", 
					String.format("%s has already been downloaded. "+ 
							"It will be overwritten if downloading again", db));
		}
	}


	@Override
	public IWizardPage getNextPage(){
		if(selectedSources.size()>0){
			return loginWizard;
		}else{
			return _pageOne;
		}
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 1;
	    
	    Collection<String> dataSources = Activator.getDefault().
	    		dataServices.getDataSources();
	    dataSourceSelection = new Button[dataSources.size()];
	    int count=0;
	    for(String dataSource:dataSources){
	    	
	    	GridData gridData = new GridData();
	    	gridData.horizontalAlignment = GridData.FILL;
	    	
	    	Group group = new Group(container, SWT.SHADOW_ETCHED_IN);
	    	GridLayout grid = new GridLayout();
	    	grid.numColumns = 2;
	    	group.setLayout(grid);
	    	group.setText(dataSource);
	    	group.setLayoutData(gridData);
	    	
		    dataSourceSelection[count] = new Button(group, SWT.CHECK);
		    dataSourceSelection[count].setText(dataSource);
		    dataSourceSelection[count].addSelectionListener(listener);

		    count++;
		    
		    new Label(group, SWT.WRAP).setText(
		    		Activator.getDefault().dataServices.
		    			getParams(dataSource).getDescrition());
		    
		    addIntegrationParamButton(dataSource, group);
		    
	    }
	    setControl(container);
	}
	
	private void addIntegrationParamButton(String dataSource, Composite group) {
		
		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.horizontalIndent = 20;
		
		
		Map<String, String> integratorParams = Activator.getDefault().dataServices.
				getParams(dataSource).getIntegratorParams();
		if(integratorParams==null || integratorParams.size()==0){
			return;
		}
		
		String desc = Activator.getDefault().dataServices.
				getParams(dataSource).getIntegratorParamDesc();
		
		Button button = new Button(group, SWT.CHECK);
		button.setText("Limited Access");
		button.setLayoutData(gridData);
		button.addSelectionListener(new LimitAccessSelectionListener(dataSource));
		button.setEnabled(false);
		button.setSelection(true);
		
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.horizontalIndent = 20;
		
		Label label = new Label(group, SWT.WRAP);
		label.setText(desc);
		label.setLayoutData(gridData);
		
		integratorParamButtons.put(dataSource, button);
		integratorParamsDescs.put(dataSource, label);
	}

	public List<String> getSelectedServices(){
		return this.selectedSources;
	}

	public boolean currentPage() {
		return isCurrentPage();
	}
}
