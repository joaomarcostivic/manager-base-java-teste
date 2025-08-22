package com.tivic.manager.str;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sol.util.RequestUtilities;
import sol.util.Result;


public class SINESPServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static final int BUFFER_SIZE = 4096;
     
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	
	    	String placa = RequestUtilities.getParameterAsString(request, "placa", "");
	    	
	    	Result result = SINESPManager.consultar(placa);
            
            response.getWriter().print(result.getObjects().get("response"));
	    } 
        catch (Exception e) {  
            e.printStackTrace();
        }
    }
}
