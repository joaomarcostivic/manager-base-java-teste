package com.tivic.manager.str;

import java.io.InputStream;

public class RadarSocketMultithreading implements Runnable {
	
	private InputStream is;
	private RadarSocketServer socketServer;	 

	public RadarSocketMultithreading(InputStream is, RadarSocketServer socketServer) {
		this.is = is;
		this.socketServer = socketServer;
	}

	@Override
	public void run() {
		try{
			
			int i;
		    char c;
		      
		    try {
		    	while((i = is.read())!=-1) {
		    		c = (char)i;
		    		System.out.print(c);
		    	}
		    } 
		    catch(Exception e) {
		    	e.printStackTrace();
		    } 
		    finally {
		    	if(is!=null)
		    		is.close();
		    }
		}
		catch (Exception e) {  
			e.printStackTrace();  
		}
	}
}
