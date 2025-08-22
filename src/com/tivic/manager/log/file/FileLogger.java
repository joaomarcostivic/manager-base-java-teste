package com.tivic.manager.log.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

public class FileLogger {
	
	File file;
	
	public FileLogger() {
		String path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR");
		path += Util.formatDate(new GregorianCalendar(), "'/manager-log_-_'yyyyMMddHHmmss'.txt'");
		
		this.file = new File(path);
	}
	
	public FileLogger(String path) {
		this.file = new File(path);
	}
	
	public void log(String line) throws IOException {
		log(line, false);
	}
	
	public void log(String line, boolean print) {
		try {
			String timestamp = Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm:ss");
			
			StringBuilder str = new StringBuilder();
			str.append("[");
			str.append(timestamp);
			str.append("]");
			str.append("\t");
			str.append(line);
			str.append("\n");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.file, true));    
		    writer.write(str.toString());
		    writer.close();
			
			if(print)
				System.out.print(str.toString());
		} catch (IOException ioe) {
			ioe.printStackTrace(System.out);
		}
	}
	
}
