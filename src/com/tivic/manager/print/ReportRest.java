package com.tivic.manager.print;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.RestData;

@Path("/print/report")
public class ReportRest {
	
	@Context private HttpServletRequest request;
		
	@POST
	@Path("/imprimir")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ "application/json" })
	public Response imprimir(RestData restData, @Context HttpServletRequest req){
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {	    	
			String reportPath =  objectMapper.convertValue(restData.getArg("reportPath"), String.class);	
			String reportName =  objectMapper.convertValue(restData.getArg("reportName"), String.class);	
			ResultSetMap rsm  =  objectMapper.convertValue(restData.getArg("rsm"), ResultSetMap.class);			
			@SuppressWarnings("unchecked")
			HashMap<String, Object> paramns =  objectMapper.convertValue(restData.getArg("paramns"), HashMap.class);
			
							
			if(paramns.get("LOGO_1") != null)
				paramns.put("LOGO_1", ImagemServices.getArrayBytesFromArrayList((ArrayList<Byte>)paramns.get("LOGO_1")));
			if(paramns.get("LOGO_2") != null)
				paramns.put("LOGO_2", ImagemServices.getArrayBytesFromArrayList((ArrayList<Byte>)paramns.get("LOGO_2")));
			
			byte[] print = ReportServices.getPdfReport(reportName, paramns, rsm);			
									
			return Response.ok(print).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReportRest.imprimir: " +  e);
			return null;
		}
	}

}
