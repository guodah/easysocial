package com.easysocial.logging;

import java.util.logging.Level;

public interface EasySocialLogHandler {
	void log(Level level, String msg);
	void log(Level level, String msg, Throwable thrown);
}
