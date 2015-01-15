package com.easysocial.networks.gephi;

import java.awt.Color;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.Project;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

class GephiContext {
	private static boolean started=false;
	public static boolean isStarted(){
		return started;
	}
	
	private static ProjectController pc;
	private static Project project;
	
	protected static void start(){
        pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        project = pc.getCurrentProject();
        
                
        PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.WHITE));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10f);
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.black);

        started = true;
	}
	
	protected static GraphModel getGraph(){
		if(!isStarted()){
			start();
		}
		
		Workspace workspace = pc.newWorkspace(project);
		pc.openWorkspace(workspace);
		return Lookup.getDefault().lookup(GraphController.class).getModel();
	}
	
	protected static void setWorkspace(Workspace workspace) {
		pc.openWorkspace(workspace);
	}
	

	public static void openWorkSpace(Workspace workspace) {
		pc.openWorkspace(workspace);
	}
	
	public static void closeWorkSpace(Workspace workspace) {
		Workspace old = pc.getCurrentWorkspace();
		pc.openWorkspace(workspace);
		pc.closeCurrentWorkspace();
		if(old!=null)
			pc.openWorkspace(old);
	}

}
