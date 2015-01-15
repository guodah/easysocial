package com.easysocial.networks.gephi;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.gephi.preview.ProcessingApplet;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;


public class MouseEventHandler implements  MouseListener, MouseWheelListener, MouseMotionListener{

	private ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
	private PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
	private Workspace workspace;
	private ProcessingApplet applet;
	private ProcessingTarget target;

	public MouseEventHandler(ProcessingTarget target, Workspace workspace){
		this.applet = (ProcessingApplet) target.getApplet();
		this.target = target;
		this.workspace = workspace;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		pc.openWorkspace(workspace);
		applet.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		pc.openWorkspace(workspace);
		applet.mouseMoved(e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		pc.openWorkspace(workspace);
		applet.mouseWheelMoved(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		pc.openWorkspace(workspace);
		applet.mouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		pc.openWorkspace(workspace);
		applet.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		pc.openWorkspace(workspace);
		applet.mouseExited(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pc.openWorkspace(workspace);
		applet.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pc.openWorkspace(workspace);
		applet.mouseReleased(e);
	}


}
