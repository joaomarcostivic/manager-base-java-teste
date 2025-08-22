package com.tivic.manager.str;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataprom.radar.ws.infracao.TransmitirInfracaoIMTV1Request.ItemTransmitirInfracaoIMTV1;
import com.tivic.manager.util.GzipUtil;


public class RadarServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final int INFRACAO_IMT = 0;
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	
	    	String url = request.getRequestURL().toString();
	    	System.out.println("URL: " + url);
	    	
	    	int tpRequest = -1;
	    	
	    	if(url.indexOf("TransmitirInfracaoIMT")!=-1)
	    		tpRequest = INFRACAO_IMT;
	    	
	    	
	    	Enumeration<String> parameterNames = request.getParameterNames();
	    	while (parameterNames.hasMoreElements()) {
	    		String paramName = parameterNames.nextElement();
	    		System.out.println("K: "+paramName);
	    		
	    		String[] paramValues = request.getParameterValues(paramName);
	    		for (int i = 0; i < paramValues.length; i++) {
	    			String msg = paramValues[i];
	    			
	    			Object o = null;
	    			switch(tpRequest) {
	    				case INFRACAO_IMT:
	    					o = getInfracaoIMTObject(msg);
	    					break;
	    			}
	    			
	    			System.out.println("obj: " + o);
	    		}
	    	}
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
	
	private List<ItemTransmitirInfracaoIMTV1> getInfracaoIMTObject(String value) {
		try {
			ObjectInputStream ois = getObjectInputStream(value);
			List<ItemTransmitirInfracaoIMTV1> list = (List<ItemTransmitirInfracaoIMTV1>) ois.readObject();
			return list;
		}
        catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	private ObjectInputStream getObjectInputStream(String value) {
		try {
			byte[] objectBytes = decode(value);

			System.out.println(objectBytes);
			ByteArrayInputStream baos = new ByteArrayInputStream(objectBytes);  
			ObjectInputStream ois = new ObjectInputStream(baos);
			return ois;
		}
        catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}

	private byte[] decode(String msg){
        try {
            byte[] data = Base64.getDecoder().decode(msg);
            byte[] result = GzipUtil.decompress(data);
            
            return result;
            
        } 
        catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }

    }
}
