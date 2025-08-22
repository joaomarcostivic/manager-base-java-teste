package com.tivic.manager.util;

import javax.servlet.ServletContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.tivic.manager.amf.DestinationsManager;
import com.tivic.manager.quark.QuarkManager;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.cdi.InjectApplicationBuilder;
import com.tivic.manager.util.cdi.InjectMobileBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.cdi.ScopeEnum;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;
import com.tivic.sol.task.ManagerTasks;


public class ContextManager implements ServletContextListener {

	private static ServletContext context = null;
	private static String contextPath = null;
	private static String realPath = null;
	
	public ContextManager() {
	}
	
	//chamada quando o contexto é inicializado
	public void contextInitialized(ServletContextEvent event) {
		try {
			ContextManager.setContext(event.getServletContext());
			ContextManager.setContextPath(event.getServletContext().getContextPath());
			ContextManager.setRealPath(event.getServletContext().getRealPath("")); 

			System.out.println("\tIniciando contexto "+Util.getSystemName()+"...");
			System.out.println("\t["+ContextManager.getContextPath()+" : "+ContextManager.getRealPath()+"]");

			InicializationBeans.init(new InjectApplicationBuilder());
			InicializationBeans.add(ScopeEnum.MOBILE, new InjectMobileBuilder());

			if(Util.initTasks())
				initManagerTasks();
			
			if(Util.initDestinations())
				DestinationsManager.start();
			
			if(Util.initQuark())
				QuarkManager.start();
			
			if(Util.initDeviceMonitor())
				DeviceMonitor.start();
			
			TaskManager.initTasks();
			
			ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
			managerLog.showLog(new InfoLogBuilder("Inicialização", "Contexto iniciado").build());
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void initManagerTasks() throws Exception {
		new ManagerTasks(0, 60000);
	}
	
	
	//chamado quando o contexto é destruido
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("\tContexto "+Util.getSystemName()+" finalizado...");
	    
	    if(Util.initDestinations())
			DestinationsManager.cleanup();
	    
	    ContextManager.setContext(null);
	}


	public static ServletContext getContext() {
		return context;
	}
	
	public static void setContext(ServletContext context) {
		ContextManager.context = context;
	}

	public static String getContextPath() {
		return contextPath;
	}

	public static void setContextPath(String contextPath) {
		ContextManager.contextPath = contextPath;
		
	}

	public static String getRealPath() {
		return realPath;
	}

	public static void setRealPath(String realPath) {
		ContextManager.realPath = realPath;
	}
}