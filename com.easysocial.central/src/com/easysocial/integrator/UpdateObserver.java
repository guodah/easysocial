package com.easysocial.integrator;

public interface UpdateObserver {
	void start(int load);
	void progress(int progress);
	void done();
}
