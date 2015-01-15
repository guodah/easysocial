package com.easysocial.eclipse;
import java.awt.Point;
import java.util.Map;










import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;


public class DownloadHandler extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		
//		MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), 
//				"INFO", ((IStructuredSelection)HandlerUtil.getActiveMenuSelection(event)).getFirstElement().getClass().getName());
		Object sel = ((IStructuredSelection)HandlerUtil.getActiveMenuSelection(event)).getFirstElement();
		if(!(sel instanceof IJavaProject)){
			return null;
		}
		
		IJavaProject javaProject = (IJavaProject) sel;
		
		EasySocialProject easysocial = new EasySocialProject(javaProject);
		easysocial.init(null, null);
		WizardDialog wizardDialog = new WizardDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell(),
			      easysocial);
		wizardDialog.setPageSize(600,  600);
		wizardDialog.open();
		
		return null;
	}

}
