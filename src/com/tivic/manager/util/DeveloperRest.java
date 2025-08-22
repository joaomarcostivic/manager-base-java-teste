package com.tivic.manager.util;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.ddl.DDLManager;
import com.tivic.manager.util.ddl.DDLManagerFactory;
import com.tivic.sol.auth.jwt.JWTIgnore;

import sol.dao.ResultSetMap;
import sol.util.RestData;
import sol.util.Result;

@Path("/util/developer/")

public class DeveloperRest {
	
	@POST
	@Path("/getTables")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getTables() {
		try {
			ResultSetMap rsm = DeveloperServices.getTables();
			return Util.rsmToJSON(rsm);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@POST
	@Path("/getClassSources")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getClassSources(RestData restData) {
		try {
			String nmTable = String.valueOf(restData.getArg("nmTable"));
			String nmPackage = String.valueOf(restData.getArg("nmPackage"));
			String nmClass = String.valueOf(restData.getArg("nmClass"));
			String nmTableSuper = String.valueOf(restData.getArg("nmTableSuper"));
			String nmClassSuper = String.valueOf(restData.getArg("nmClassSuper"));
			String nmClassConnection = String.valueOf(restData.getArg("nmClassConnection"));
			boolean lgCompliance = String.valueOf(restData.getArg("lgCompliance")).equals("true");
			
			Result result = DeveloperServices.getClassSources(nmTable, nmPackage, nmClass, nmTableSuper, nmClassSuper, nmClassConnection, lgCompliance);
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(result);
			return json;
		} 
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response status() {
		return Response.status(200).entity("OK").build();
	}

}