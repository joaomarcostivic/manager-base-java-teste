package com.tivic.manager.sis;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tivic.manager.util.Util;


import sol.util.ConfManager;

public class ParametrosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	ConfManager conf = Util.getConfManager();
	    	
	    	String amfHost = conf.getProps().getProperty("AMF_HOST");
	    	String amfPort = conf.getProps().getProperty("AMF_PORT");
	    	String amfContext = conf.getProps().getProperty("AMF_CONTEXT");
	    	String amfServlet = conf.getProps().getProperty("AMF_SERVLET");
	    	String amfId = conf.getProps().getProperty("AMF_ID");
	    	
	    	String mapHost = conf.getProps().getProperty("MAP_HOST");
	    	String mapKey = conf.getProps().getProperty("MAP_KEY");
	    	
	    	String passUppercase = conf.getProps().getProperty("PASS_UPPERCASE");
	    	
	    	String sector = conf.getProps().getProperty("SECTOR");
	        
	    	String checkDST = conf.getProps().getProperty("CHECK_DST");
	    	
	        PrintWriter out = response.getWriter();
			out.write("<init amf_id='"+amfId+"' "+ 
							"amf_host='"+amfHost+"' " +
						    "amf_port='"+amfPort+"' " +
						    "amf_context='"+amfContext+"' " +
						    "amf_servlet='"+amfServlet+"' " +
						    "map_host='"+mapHost+"' " +
						    "map_key='"+mapKey+"' " +
						    "pass_uppercase='"+passUppercase+"' " +
						    "check_dst='"+checkDST+"' " +
						    "sector='" + (sector != null ? sector : "0") + "' />"); 
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
}
