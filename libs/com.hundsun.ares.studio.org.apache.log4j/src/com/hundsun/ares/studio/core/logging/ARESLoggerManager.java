package com.hundsun.ares.studio.core.logging;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class ARESLoggerManager {

	public static final String LOG_CONFIG_FILE = "aresLogger.properties";
	private static Logger log = Logger.getLogger(ARESLoggerManager.class);
	
	public static void start() {
		File file = new File(LOG_CONFIG_FILE);
		if (file.exists()) {
			PropertyConfigurator.configureAndWatch(LOG_CONFIG_FILE);
			log.info("Log4j��������...");
		} else {
			Bundle coreBundle = Platform.getBundle("com.hundsun.ares.studio.core");
			URL url = coreBundle.getEntry(LOG_CONFIG_FILE);
			PropertyConfigurator.configure(url);
			log.info("û���ҵ�log4j�����ļ�, ʹ��Ĭ������...");
		}
	}
	
}
