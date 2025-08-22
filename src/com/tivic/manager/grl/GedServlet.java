package com.tivic.manager.grl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sol.util.RequestUtilities;


public class GedServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	String url = RequestUtilities.getParameterAsString(request, "url", null);
	    	
	    	URL connectURL = new URL(url);
	       	
	    	System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
	    	HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
	    	//conn.setRequestProperty("http.agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
	    	conn.connect();
	    	System.out.println("User-Agent: "+conn.getHeaderField("User-Agent"));
	    	InputStream in = conn.getInputStream();
	    	OutputStream out = response.getOutputStream();

	    	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    	
	        byte[] buffer = new byte[4096];
	        int length; 
	        while((length = in.read(buffer)) > 0) {
	            out.write(buffer, 0, length);
	            bout.write(buffer, 0, length);
	        }
	        
	        RandomAccessFile raf = new RandomAccessFile("C:\\Users\\Maurício\\Downloads\\"+new GregorianCalendar().getTimeInMillis()+".html", "rw");
	        raf.write(bout.toByteArray());
	        raf.close();
	        
	        in.close();
	        out.flush();
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
}
