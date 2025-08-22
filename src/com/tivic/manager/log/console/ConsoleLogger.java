package com.tivic.manager.log.console;

import java.util.Collections;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class ConsoleLogger {
	
	private static ConsoleLogger instance;
	
	private ConsoleLogger() { }
	
	public static ConsoleLogger getInstance() {
		if(instance == null) {
            synchronized (ConsoleLogger.class) {
                if (instance == null) {
                	instance = new ConsoleLogger();
                }
            }
		}
		return instance;
	}
	

	public void printProgress(long startTime, long total, long current, String label) {
		printProgress(startTime, total, current, label, 0);
	}
		
	public void printProgress(long startTime, long total, long current, String label, long delay) {
		if(delay > 0) {
			try { Thread.sleep(delay); } catch (InterruptedException e) { }
		}
		
	    long eta = current == 0 ? 0 : 
	        (total - current) * (System.currentTimeMillis() - startTime) / current;

	    String etaHms = current == 0 ? "N/A" : 
	            String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
	                    TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
	                    TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

	    StringBuilder string = new StringBuilder(140);   
	    int percent = (int) (current * 100 / total);
	    string
	        .append('\r')
	        .append("[").append(label).append("]")
	        .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
	        .append(String.format(" %d%% [", percent))
	        .append(String.join("", Collections.nCopies(percent, "=")))
	        .append('>')
	        .append(String.join("", Collections.nCopies(100 - percent, " ")))
	        .append(']')
	        .append(String.join("", Collections.nCopies(current == 0 ? (int) (Math.log10(total)) : (int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
	        .append(String.format(" %d/%d, ETA: %s", current, total, etaHms));

	    System.out.print(string);
	}
}
