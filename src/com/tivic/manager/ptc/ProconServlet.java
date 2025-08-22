package com.tivic.manager.ptc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sol.util.RequestUtilities;


public class ProconServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{	    	
	    	String nrDocumento = RequestUtilities.getParameterAsString(request, "cod", "");
	    	String nmRanking   = RequestUtilities.getParameterAsString(request, "ranking", "");
	    	String nrPeriodo   = RequestUtilities.getParameterAsString(request, "periodo", "");

	    	if(!nrDocumento.equals("")){	
	    		
		        String jsonDocumento = DocumentoServices.getDocumentoJSON(nrDocumento);	     
		        response.setContentType("application/json");		        
		        PrintWriter json = response.getWriter();
		        json.print(jsonDocumento);
		        json.flush();     
		        
	    	} else if (nmRanking.equals("reclamado")) {
	    		String jsonRanking = DocumentoPessoaServices.getRankingJSON("1", "RECLAMADO", nrPeriodo);
		        response.setContentType("application/json");		        
		        PrintWriter json = response.getWriter();
		        json.print(jsonRanking);
		        json.flush();     
	    	}
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
}
