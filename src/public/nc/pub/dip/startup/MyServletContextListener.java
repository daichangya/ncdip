package nc.pub.dip.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
	private java.util.Timer timer = null; 

	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		timer.cancel(); 
	    event.getServletContext().log("定时器销毁"); 

	}

	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		timer = new java.util.Timer(true); 
		 event.getServletContext().log("定时器已启动"); 
		 timer.schedule(new MyTask(event.getServletContext()),0,5000); 
		 event.getServletContext().log("已经添加任务调度表"); 
		}
}
