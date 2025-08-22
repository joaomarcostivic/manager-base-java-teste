package com.tivic.manager.amf;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.ObjectUtils;
import com.tivic.manager.util.ReflectionUtils;
import com.tivic.manager.util.Util;

import flex.messaging.Destination;
import flex.messaging.MessageBroker;
import flex.messaging.config.ConfigMap;
import flex.messaging.services.ServiceAdapter;
import flex.messaging.services.remoting.RemotingDestination;

public class DestinationsManager {
	
	public static final int INIT_WITH_ANNOTATION = 1;
	public static final int INIT_WITHOUT_ANNOTATION = 2;

	private static int initType = INIT_WITH_ANNOTATION;
	
	private static MessageBroker messageBroker = null;
	
	private static ArrayList<Destination> destinations = new ArrayList<Destination>();
	
	private static ConfigMap serviceAdapterConfig = new ConfigMap();
	private static ConfigMap accessConfig = new ConfigMap();
	
	private static ArrayList<String> classes = null;
	private static ArrayList<String> disabledClasses = new ArrayList<>();
	
	
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static ScheduledFuture<?> initterHandler;
	
	public static void start(){
		
		initType = Util.getDestinationInitType();
		
		System.out.println("\tAguardando MessageBroker ser iniciado...");
		final Runnable destinationsStarter = new Runnable() {
			public void run() {
				startRemotingDestinations();
			}
		};
		
		/*
		 * Tenta iniciar os remoting destinations, se o servlet MessageBroket ainda nao 
		 * estiver iniciado, tenta novamente a cada 5 segundos
		 */
		initterHandler = scheduler.scheduleAtFixedRate(destinationsStarter, 0, 5, TimeUnit.SECONDS);
	}
	
	public static void startRemotingDestinations(){
		try{
			messageBroker = MessageBroker.getMessageBroker(null);
			
			if(messageBroker!=null){
				initterHandler.cancel(true);
			}
			else
				return;
			System.out.println("\tIniciando Remoting Destinations...");
			System.out.println("\tUtilizar anotações? " + ((initType==INIT_WITH_ANNOTATION) ? "Sim" : "Não"));
			
			accessConfig.addProperty("use-mappings", "true");
			accessConfig.addProperty("method-access-level", "remote");
			serviceAdapterConfig.addProperty("access", accessConfig);
			
			/*
			 * lista todas as classes do pacote principal da aplicacao
			 */
			classes = ReflectionUtils.getClassesNames(Util.getPackageRoot(), "Services");
			
			for(int i=0; i< classes.size(); i++){
				String destinationName = classes.get(i);
				createDestination(destinationName, classes.get(i));
			}
			
			System.out.println("\t[AMF] "+destinations.size()+" destinations em "+ContextManager.getContextPath()+" ("+(getMemoryUsage()/1024/1024)+"mb"+") ");
			if(initType==INIT_WITH_ANNOTATION)
				System.out.println("\t[AMF] "+disabledClasses.size() + " desabilitados de " + classes.size()+" classes");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void createDestination(String destinationName, String destinationSource){
		
		if(initType==INIT_WITH_ANNOTATION && isDisabledClass(destinationSource)) {
			disabledClasses.add(destinationSource);
			return;
		}
		
		RemotingDestination d = (RemotingDestination)messageBroker.getService("remoting-service").createDestination(destinationName);
		
		d.setSource(destinationSource);
		
		ServiceAdapter serviceAdapter = d.createAdapter("java-object");
		serviceAdapter.initialize("java-object", serviceAdapterConfig);
		
		d.setAdapter(serviceAdapter);
		d.addChannel("my-amf");
		d.start();
		
		destinations.add(d);
	}
	
	private static boolean isDisabledClass(String className) {
		try {
			Class<?> cls = Class.forName(className);
			
			if (cls.isAnnotationPresent(DestinationConfig.class)) {
			    DestinationConfig conf = cls.getAnnotation(DestinationConfig.class);
			    return !conf.enabled();
			}
			else 
				return false;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean existDestination(String destinationName) {
		for (Destination d : destinations) {
			if(d.getId().equals(destinationName))
				return true;
		}
		return false;
	}

	public static void cleanup(){
		if(messageBroker==null)
			return;
		
		for(int i=0; i< classes.size(); i++)
			messageBroker.getService("remoting-service").removeDestination(classes.get(i));
	}
	

	public static long getMemoryUsage(){
		try{
			return ObjectUtils.sizeOf(destinations);
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getSize(){
		try{
			return destinations.size();
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}
