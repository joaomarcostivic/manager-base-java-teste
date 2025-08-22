package com.tivic.manager.util;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.grl.ParametroServices;

public class LogUtils {
	
	public static final int VERBOSE_LEVEL_INFO = 0;
	public static final int VERBOSE_LEVEL_DEBUG = 1;
	
	public static final int TIMER_MILLISECOND = 0;
	public static final int TIMER_SECOND = 1;
	public static final int TIMER_MINUTE = 2;
	public static final int TIMER_HOUR = 3;
	public static final int TIMER_DAY = 4;
	
	private static int _LOG_VERBOSE_LEVEL = VERBOSE_LEVEL_DEBUG;
	private static boolean _LOG_FILE = false;
	
	private static ArrayList<HashMap<String, Long>> _timers = new ArrayList<HashMap<String, Long>>();
	
	public static final String[] verboseLevels = {"INFO", "DEBUG"};
	
	static {
		try {
			refreshLogConfiguration();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
		}
	}
	
	public static void refreshLogConfiguration() {
		_LOG_VERBOSE_LEVEL = ParametroServices.getValorOfParametroAsInteger("LOG_VERBOSE_LEVEL", _LOG_VERBOSE_LEVEL);
		_LOG_FILE = ParametroServices.getValorOfParametroAsInteger("LG_LOG_FILE", 0)==1;
		
		System.out.println("Nível de Log: ["+_LOG_VERBOSE_LEVEL+"] "+verboseLevels[_LOG_VERBOSE_LEVEL]);
		System.out.println("Log em arquivo? " + _LOG_FILE);
	}
	
	public static void info(String text){
		log("[INFO "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] "+text, VERBOSE_LEVEL_INFO);
	}
	
	public static void debug(String text){
		log("[DEBUG "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] "+text, VERBOSE_LEVEL_DEBUG);
	}
	
	public static void log(String text, int verboseLevel){
		if(verboseLevel<=_LOG_VERBOSE_LEVEL) {
			printLog(text);
			
			if(_LOG_FILE) 
				fileLog(text+"\n");
		}
	}
	
	public static void printLog(String text){
		System.out.println(text);
	}
	
	public static void fileLog(String text) {
		fileLog(null, text);
	}
		
	public static void fileLog(String fileName, String text) {
		try	{
			
			if(fileName==null || fileName.equals(""))
				fileName = verboseLevels[_LOG_VERBOSE_LEVEL].toLowerCase()+Util.formatDate(new GregorianCalendar(), "yyyyMMdd")+".log";
			
			RandomAccessFile arquivo = new RandomAccessFile(fileName, "rw");
			arquivo.seek(arquivo.length());
			arquivo.writeBytes(text);
			arquivo.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void createTimer(String id) {
		try{
			
			if(_timers==null)
				_timers = new ArrayList<HashMap<String, Long>>();
			
			boolean exists = false;
			for (HashMap<String, Long> timerMap : _timers) {
				if(timerMap.containsKey(id)){
					exists = true;
					break;
				}
			}
			
			if(!exists){
				HashMap<String, Long> timerMap = new HashMap<String, Long>();
				timerMap.put(id, System.currentTimeMillis());
				_timers.add(timerMap);
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public static void destroyTimer(String id) {
		try{
			if(_timers!=null) {
				for (HashMap<String, Long> timerMap : _timers) {
					if(timerMap.containsKey(id)){
						_timers.remove(timerMap);
						break;
					}
				}
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public static void resetTimer(String id) {
		try {
			if(_timers!=null) {
				for (HashMap<String, Long> timerMap : _timers) {
					if(timerMap.containsKey(id)){
						timerMap.put(id, System.currentTimeMillis());
						break;
					}
				}
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public static void logTimer(String id, int format, int verboseLevel) {
		try {
			long now = System.currentTimeMillis();
			
			if(_timers!=null) {
				for (HashMap<String, Long> timerMap : _timers) {
					if(timerMap.containsKey(id)){
						long initial = timerMap.get(id);
						long time = now - initial;
						
						String text = "[TIMER] "+id+": ";
						
						switch(format){
							case TIMER_MILLISECOND:
								text += time+"ms"; 
								break;
							case TIMER_SECOND:
								text += (time/1000)+"s"; 
								break;
							case TIMER_MINUTE:
								text += (time/1000/60)+"m"; 
								break;
							case TIMER_HOUR:
								text += (time/1000/60/60)+"ms"; 
								break;
							case TIMER_DAY:
								text += (time/1000/60/60/24)+"ms"; 
								break;
							default:
								text += (time/1000)+"s"; 
						}
						
						switch(verboseLevel){
							case VERBOSE_LEVEL_INFO:
								info(text);
								break;
							case VERBOSE_LEVEL_DEBUG:
								debug(text);
								break;
							default:
								debug(text);
						}
						
						break;
					}
				}
			}
		}
		catch(Exception e) {
			
		}
	}
}