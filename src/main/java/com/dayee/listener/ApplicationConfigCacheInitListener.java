package com.dayee.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.dayee.utils.ApplicationConfigCache;

public class ApplicationConfigCacheInitListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ApplicationConfigCache.init();
	}
}