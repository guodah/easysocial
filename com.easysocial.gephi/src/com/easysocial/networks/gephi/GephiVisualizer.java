package com.easysocial.networks.gephi;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.List;
import java.util.ArrayList;

import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import processing.core.PApplet;

import com.easysocial.networks.Network;

public class GephiVisualizer {
	
	
	public void visualize(GephiNetwork network) {
		if(network==null){
			return;
		}
		
		GephiContext.openWorkSpace(network.getGephiWorkspace());
		
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.WHITE));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10f);
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.black);
        previewController.refreshPreview();
        
        //New Processing target, get the PApplet
        ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(
        		RenderTarget.PROCESSING_TARGET);
        PApplet applet = target.getApplet();
        applet.init();
        
        GephiUtils.sleep(1000); // wait for processing to complete initialization
        
        previewController.render(target);

        MouseListener [] mouseListeners = applet.getMouseListeners();
        for(MouseListener mouseListener: mouseListeners)
        	applet.removeMouseListener(mouseListener);
        
        MouseWheelListener [] wheelListeners = applet.getMouseWheelListeners();
        for(MouseWheelListener wheelListener:wheelListeners)	
        	applet.removeMouseWheelListener(wheelListener);

        MouseMotionListener [] mouseMotionListeners = applet.getMouseMotionListeners();
        for(MouseMotionListener mouseMotionListener:mouseMotionListeners)	
        	applet.removeMouseMotionListener(mouseMotionListener);

        ComponentListener [] componentListeners = applet.getComponentListeners();
        for(ComponentListener componentListener:componentListeners)	
        	applet.removeComponentListener(componentListener);
        MouseEventHandler handler = new MouseEventHandler(target, network.getGephiWorkspace());
        applet.addMouseListener(handler);
        applet.addMouseWheelListener(handler);
        applet.addMouseMotionListener(handler);

        target.refresh();
        target.resetZoom();

        
        //Add the applet to a JFrame and display
        JFrame frame = new JFrame(network.getName());
        frame.setLayout(new BorderLayout());
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.add(applet, BorderLayout.CENTER);
        frame.add(applet, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        
//        frame.addComponentListener(new ResizeHandler(target));
	}
	

	private static class ResizeHandler extends ComponentAdapter{
		ProcessingTarget target;
		ResizeHandler(ProcessingTarget target){
			this.target = target;
		}
		
		@Override
		public void componentResized(ComponentEvent e){
			target.refresh();
			
		}
	}

}
