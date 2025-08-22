package com.tivic.manager.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

import com.tivic.manager.conf.ManagerConf;

import sol.util.ConfManager;

public class TaskManager {

	private static ArrayList<Task> tasks = new ArrayList<Task>();
	
	public static final int TP_REPEAT_NONE    = 0;
	public static final int TP_REPEAT_DAY     = 1;
	public static final int TP_REPEAT_WEEK    = 2;
	public static final int TP_REPEAT_MONTH   = 3;
	public static final int TP_REPEAT_HOUR    = 4;
	public static final int TP_REPEAT_MINUTE  = 5;
	
	public static void initTasks(){
		try {
			ManagerConf conf = ManagerConf.getInstance();
			
	    	String taskIds = conf.get("TASKS");
	    	
	    	if(taskIds!=null && !taskIds.equals("")) {
	    		System.out.println("\tIniciando tarefas "+Util.getSystemName()+"...");
	    		System.out.println("\t["+ContextManager.getContextPath()+" : "+ContextManager.getRealPath()+"]");
	    		
		    	String[] ids = taskIds.split("\\|");
		    	
		    	System.out.println("\t[TASKS] "+ids.length+" tarefas encontradas. ");
		    	
		    	for(int i=0; i<ids.length; i++) {
		    		
		    		if(!conf.get(ids[i]+"_CLASS").equals("") &&
	    			   !conf.get(ids[i]+"_METHOD").equals("") &&
	    			   !conf.get(ids[i]+"_DATE").equals("") &&
	    			   !conf.get(ids[i]+"_PERIOD").equals("")) {
		    			
			    		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			    	    Date date = dateFormatter.parse(conf.get(ids[i]+"_DATE"));
			    	    GregorianCalendar dtExecucao = new GregorianCalendar();
			    	    dtExecucao.setTimeInMillis(date.getTime());
			    	    
			    	    String className = (String)conf.get(ids[i]+"_CLASS");
			    	    String methodCall = (String)conf.get(ids[i]+"_METHOD");
			    	    
			    	    int tpRepeat = new Integer((String)conf.get(ids[i]+"_PERIOD"));
			    	    
			    	    int interval = new Integer((String)conf.get(ids[i]+"_INTERVAL", "1"));
			    	    
			    	    long minuto = 60*1000;
			    	    long hora    = 60*minuto;
			    	    long dia    = hora * 24;
			    	    long semana = dia * 7;
			    	    GregorianCalendar dtProximaExecucao = (GregorianCalendar)dtExecucao.clone();
			    	    dtProximaExecucao.add(Calendar.MONTH, 1);
			    	    long mes    = dia * Util.getQuantidadeDias(dtExecucao, dtProximaExecucao);
			    	    
			    	    long vlPeriodo = 0;
			    	    switch(tpRepeat){
				    	    case TP_REPEAT_NONE:
			    	    		vlPeriodo = 0;
			    	    		break;
			    	    	case TP_REPEAT_DAY:
			    	    		vlPeriodo = dia;
			    	    		break;
			    	    	case TP_REPEAT_WEEK:		
			    	    		vlPeriodo = semana;
			    	    		break;
							case TP_REPEAT_MONTH:
								vlPeriodo = mes;	
								break;
							case TP_REPEAT_HOUR:
								vlPeriodo = hora;	
								break;
							case TP_REPEAT_MINUTE:
								vlPeriodo = minuto;	
								break;
							default:
								vlPeriodo = 0;	
								break;
			    	    }
			    	    
			    		Task task = new Task(className, methodCall, dtExecucao);
			    		
			    		tasks.add(task);
			    		
			    		Timer timer = new Timer();
			    	    timer.schedule(task, date, vlPeriodo*interval);
		    		}
		    	}
	    	}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
