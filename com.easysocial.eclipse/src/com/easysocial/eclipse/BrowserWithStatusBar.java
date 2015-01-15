package com.easysocial.eclipse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class BrowserWithStatusBar extends Composite{
	private Browser browser;
	private Label status;
	private ProgressBar progressBar;
	private boolean busy;
	public BrowserWithStatusBar(Composite parent, int style) {
		super(parent, style);
		busy = false;
		initControls();
	}

	private void initControls() {
		browser = new Browser(this, SWT.BORDER);
		status = new Label(this, SWT.NONE);
		progressBar = new ProgressBar(this, SWT.NONE);

		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
	    data.top = new FormAttachment(0, 0);
	    data.right = new FormAttachment(100, 0);
	    data.bottom = new FormAttachment(status, -5, SWT.DEFAULT);

		browser.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 5);
		data.right = new FormAttachment(progressBar, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment(100, -5);
	
		status.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(100, -5);
		data.bottom = new FormAttachment(100, -5);
		progressBar.setLayoutData(data);

		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				status.setText(event.text);
			}
		});
		
		browser.addProgressListener(new ProgressListener() {
			public void changed(ProgressEvent event) {
				if (event.total == 0)
					return;
				int ratio = event.current * 100 / event.total;
				if (progressBar != null)
					progressBar.setSelection(ratio);
			}

			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
			}
		});

		setLayout(new FormLayout());

	}

	public Browser getBrowser(){
		return browser;
	}	
}
